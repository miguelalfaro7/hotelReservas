package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.domain.HabitacionEstandar;
import com.hotel.Hotel.domain.RangoFechas;
import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.request.ActualizarClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResumenResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ClienteMapperTest {
    private final ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);

    @Test
    void patchSoloNombreConservaEmailEstadoYPenalizaciones() {
        Cliente cliente = clienteConPenalizacion();
        mapper.updateClienteFromDto(new ActualizarClienteRequest("Nuevo", null), cliente);
        assertEquals("Nuevo", cliente.getNombre());
        assertEquals("ana@example.com", cliente.getEmail());
        assertFalse(cliente.isActivo());
        assertEquals(3, cliente.getPenalizaciones());
    }

    @Test
    void patchConCamposNulosNoModificaNada() {
        Cliente cliente = clienteConPenalizacion();
        mapper.updateClienteFromDto(new ActualizarClienteRequest(null, null), cliente);
        assertEquals("Ana", cliente.getNombre());
        assertEquals("ana@example.com", cliente.getEmail());
        assertFalse(cliente.isActivo());
        assertEquals(3, cliente.getPenalizaciones());
    }

    @Test
    void resumenCalculaTotalesYProyectaReservasSinRecursion() {
        Cliente cliente = new Cliente("Ana", "ana@example.com");
        HabitacionEstandar una = new HabitacionEstandar("E-1", 2, 100.0, 2);
        HabitacionEstandar dos = new HabitacionEstandar("E-2", 2, 150.0, 1);
        Reserva reservaUna = new Reserva(cliente, una, new RangoFechas(LocalDateTime.parse("2026-01-01T15:00:00"),
                LocalDateTime.parse("2026-01-03T15:00:00")));
        Reserva reservaDos = new Reserva(cliente, dos, new RangoFechas(LocalDateTime.parse("2026-02-01T15:00:00"),
                LocalDateTime.parse("2026-02-04T15:00:00")));
        agregarReservas(cliente, List.of(reservaUna, reservaDos));

        ClienteResumenResponse resumen = mapper.toResumenResponse(cliente);
        assertEquals(2, resumen.totalReservasRealizadas());
        assertEquals(650.0, resumen.montoTotalGastado());
        assertEquals(2, resumen.reservasRecientes().size());
        assertEquals("E-2", resumen.reservasRecientes().get(0).numeroHabitacion());
    }

    private Cliente clienteConPenalizacion() {
        Cliente cliente = new Cliente("Ana", "ana@example.com");
        cliente.registrarPenalizacion();
        cliente.registrarPenalizacion();
        cliente.registrarPenalizacion();
        return cliente;
    }

    @SuppressWarnings("unchecked")
    private void agregarReservas(Cliente cliente, List<Reserva> reservas) {
        try {
            Field field = Cliente.class.getDeclaredField("reservas");
            field.setAccessible(true);
            ((List<Reserva>) field.get(cliente)).addAll(reservas);
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }
}
