package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.HabitacionEstandar;
import com.hotel.Hotel.domain.SuitePresidencial;
import com.hotel.Hotel.dto.response.HabitacionEstandarResponse;
import com.hotel.Hotel.dto.response.SuitePresidencialResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HabitacionMapperTest {
    private final HabitacionMapper mapper = Mappers.getMapper(HabitacionMapper.class);

    @Test
    void mapeaHabitacionEstandarPolimorficamente() {
        HabitacionEstandarResponse response = assertInstanceOf(HabitacionEstandarResponse.class,
                mapper.toResponse(new HabitacionEstandar("E-101", 2, 120.0, 2)));
        assertEquals(2, response.camasIndividuales());
    }

    @Test
    void mapeaSuitePolimorficamente() {
        SuitePresidencialResponse response = assertInstanceOf(SuitePresidencialResponse.class,
                mapper.toResponse(new SuitePresidencial("P-501", 2, 350.0, true, true)));
        assertTrue(response.jacuzziPrivado());
        assertTrue(response.incluyeMayordomo());
    }
}
