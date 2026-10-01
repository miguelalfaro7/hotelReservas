package com.hotel.Hotel.dto.response;

import com.hotel.Hotel.domain.EstadoHabitacion;

import java.util.UUID;

public record HabitacionEstandarResponse(
        UUID id,
        String numero,
        int capacidadMaxima,
        double precioPorNoche,
        EstadoHabitacion estado,
        int camasIndividuales) implements HabitacionResponse {
}
