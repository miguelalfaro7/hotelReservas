package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Habitacion;
import com.hotel.Hotel.domain.HabitacionEstandar;
import com.hotel.Hotel.domain.SuitePresidencial;
import com.hotel.Hotel.dto.request.CrearHabitacionEstandarRequest;
import com.hotel.Hotel.dto.request.CrearSuitePresidencialRequest;
import com.hotel.Hotel.dto.response.HabitacionEstandarResponse;
import com.hotel.Hotel.dto.response.HabitacionResponse;
import com.hotel.Hotel.dto.response.SuitePresidencialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.SubclassExhaustiveStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {
    HabitacionEstandarResponse toEstandarResponse(HabitacionEstandar habitacion);
    SuitePresidencialResponse toSuiteResponse(SuitePresidencial habitacion);

    @SubclassMapping(source = HabitacionEstandar.class, target = HabitacionEstandarResponse.class)
    @SubclassMapping(source = SuitePresidencial.class, target = SuitePresidencialResponse.class)
    @BeanMapping(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
    HabitacionResponse toResponse(Habitacion habitacion);

    default HabitacionEstandar toEntity(CrearHabitacionEstandarRequest request) {
        return request == null ? null : new HabitacionEstandar(request.numero(), request.capacidadMaxima(),
                request.precioPorNoche(), request.camasIndividuales());
    }

    default SuitePresidencial toEntity(CrearSuitePresidencialRequest request) {
        return request == null ? null : new SuitePresidencial(request.numero(), request.capacidadMaxima(),
                request.precioPorNoche(), request.incluyeMayordomo(), request.jacuzziPrivado());
    }

    List<HabitacionResponse> toResponseList(List<Habitacion> habitaciones);
}
