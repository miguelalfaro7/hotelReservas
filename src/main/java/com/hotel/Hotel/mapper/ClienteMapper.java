package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.request.ActualizarClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import com.hotel.Hotel.dto.response.ClienteResumenResponse;
import com.hotel.Hotel.dto.response.ReservaItemResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteResponse toResponse(Cliente cliente);
    List<ClienteResponse> toResponseList(List<Cliente> clientes);

    @Mapping(target = "totalReservasRealizadas", expression = "java(cliente.getReservas().size())")
    @Mapping(target = "montoTotalGastado", expression = "java(cliente.getReservas().stream().mapToDouble(Reserva::getCostoTotal).sum())")
    @Mapping(target = "reservasRecientes", source = "reservas", qualifiedByName = "reservasRecientes")
    ClienteResumenResponse toResumenResponse(Cliente cliente);

    @Named("toReservaItem")
    @Mapping(target = "idReserva", source = "id")
    @Mapping(target = "numeroHabitacion", source = "habitacion.numero")
    @Mapping(target = "fechaInicio", source = "periodo.fechaInicio")
    @Mapping(target = "fechaFin", source = "periodo.fechaFin")
    @Mapping(target = "estado", expression = "java(reserva.getEstado().name())")
    ReservaItemResponse toReservaItem(Reserva reserva);

    @IterableMapping(qualifiedByName = "toReservaItem")
    List<ReservaItemResponse> toReservaItems(List<Reserva> reservas);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // id, activo, penalizaciones y reservas no exponen setter: quedan protegidos frente a Mass Assignment.
    void updateClienteFromDto(ActualizarClienteRequest dto, @MappingTarget Cliente entity);

    @Named("reservasRecientes")
    default List<Reserva> reservasRecientes(List<Reserva> reservas) {
        return reservas == null ? List.of() : reservas.stream()
                .sorted(Comparator.comparing((Reserva r) -> r.getPeriodo().getFechaInicio()).reversed())
                .toList();
    }
}
