package com.tenniscourts.reservations.service;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.stubs.ScheduleStubs;
import com.tenniscourts.reservations.stubs.TennisCourtStubs;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import com.tenniscourts.tenniscourts.TennisCourtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.tenniscourts.reservations.stubs.TennisCourtStubs.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TennisCourtServiceTests {

    @Mock
    TennisCourtRepository tennisCourtRepository;

    @Mock
    TennisCourtMapper tennisCourtMapper;

    @Mock
    ScheduleService scheduleService;

    @InjectMocks
    TennisCourtService tennisCourtService;

    @Test
    void addTennisCourtShouldReturnTheTennisCourt() {
        var tennisCourtStub = tennisCourtStub();
        var tennisCourtDtoStub = tennisCourtDTOStub();

        when(tennisCourtRepository.saveAndFlush(tennisCourtStub)).thenReturn(tennisCourtStub);
        when(tennisCourtMapper.map(tennisCourtStub)).thenReturn(tennisCourtDtoStub);
        when(tennisCourtMapper.map(tennisCourtDtoStub)).thenReturn(tennisCourtStub);

        var actualStub = tennisCourtService.addTennisCourt(tennisCourtDtoStub);

        assertEquals(tennisCourtDtoStub, actualStub);
    }

    @Test
    void findTennisCourtByIdShouldReturnATennisCourt() {
        var tennisCourtStub = tennisCourtStub();
        var tennisCourtDtoStub = tennisCourtDTOStub();

        var tennisCourtId = 1L;

        when(tennisCourtRepository.findById(tennisCourtId)).thenReturn(Optional.of(tennisCourtStub));
        when(tennisCourtMapper.map(tennisCourtStub)).thenReturn(tennisCourtDtoStub);

        var actualStub = tennisCourtService.findTennisCourtById(tennisCourtId);

        assertEquals(tennisCourtDtoStub, actualStub);
    }

    @Test
    void findTennisCourtByIdShouldThrowAnException() {
        var tennisCourtId = 1L;

        when(tennisCourtRepository.findById(tennisCourtId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @Test
    void findTennisCourtWithSchedulesByIdShouldReturnATennisCourtWithAListOfSchedules() {
        var tennisCourtStub = tennisCourtStub();
        var tennisCourtDtoStub = tennisCourtDTOStub();
        tennisCourtDtoStub.setTennisCourtSchedules(List.of(ScheduleStubs.scheduleDTOStub()));

        var tennisCourtId = 1L;

        when(tennisCourtRepository.findById(tennisCourtId)).thenReturn(Optional.of(tennisCourtStub));
        when(tennisCourtMapper.map(tennisCourtStub)).thenReturn(tennisCourtDtoStub);
        when(scheduleService.findSchedulesByTennisCourtId(tennisCourtId)).thenReturn(List.of(ScheduleStubs.scheduleDTOStub()));

        var actualStub = tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId);

        assertEquals(tennisCourtDtoStub, actualStub);
    }
}
