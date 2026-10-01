package com.hotel.Hotel.dto.response;

import com.hotel.Hotel.domain.EstadoHabitacion;

import java.util.UUID;

public record SuitePresidencialResponse(
        UUID id,
        String numero,
        int capacidadMaxima,
        double precioPorNoche,
        EstadoHabitacion estado,
        boolean incluyeMayordomo,
        boolean jacuzziPrivado) implements HabitacionResponse {
}
