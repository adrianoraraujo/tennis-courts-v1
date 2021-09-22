package com.tenniscourts.reservations.service;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.ReservationMapper;
import com.tenniscourts.reservations.ReservationRepository;
import com.tenniscourts.reservations.ReservationService;
import com.tenniscourts.reservations.model.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.tenniscourts.reservations.stubs.ReservationStubs.*;
import static com.tenniscourts.reservations.stubs.ScheduleStubs.scheduleStub;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTests {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationMapper reservationMapper;

    @InjectMocks
    ReservationService reservationService;

    @Test
    void BookAReservationShouldReturnAReservationDTO() {
        var createReservationDtoStub = createReservationRequestDTOStub();
        var reservationDtoStub = reservationDTOStub();
        var reservationStub = reservationStub();

        when(reservationRepository.save(reservationStub)).thenReturn(reservationStub);
        when(reservationMapper.map(reservationStub)).thenReturn(reservationDtoStub);
        when(reservationMapper.map(createReservationDtoStub)).thenReturn(reservationStub);

        var actualStub = reservationService.bookReservation(createReservationDtoStub);

        assertEquals(reservationDtoStub, actualStub);
    }

    @Test
    void FindReservationByIdShouldReturnAReservation() {
        var reservationDtoStub = reservationDTOStub();
        var reservationStub = reservationStub();

        var reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationStub));
        when(reservationMapper.map(reservationStub)).thenReturn(reservationDtoStub);

        var actualStub = reservationService.findReservationById(reservationId);

        assertEquals(reservationDtoStub, actualStub);
    }

    @Test
    void FindByIdShouldThrowAnException() {
        var reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.findReservationById(reservationId));
    }

    @Test
    void findPastReservationsShouldReturnAListOfReservations() {
        var reservationDtoStub = reservationDTOStub();
        var reservationStub = reservationStub();

        when(reservationRepository.findByScheduleStartDateTimeLessThanEqual(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))))
                .thenReturn(List.of(reservationStub()));
        when(reservationMapper.map(reservationStub)).thenReturn(reservationDtoStub);

        var actualStub = reservationService.findPastReservations();

        assertEquals(List.of(reservationDtoStub), actualStub);
    }

    @Test
    void cancelReservationWithAlreadyChangedStatusShouldThrowAnException() {
        var reservationStub = reservationStub();
        reservationStub.setReservationStatus(ReservationStatus.CANCELLED);

        var reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationStub));

        assertThrows(IllegalArgumentException.class, () -> reservationService.cancelReservation(reservationId));
    }

    @Test
    void cancelReservationWithPastStartDateShouldThrowAnException() {
        var reservationStub = reservationStub();
        reservationStub.getSchedule().setStartDateTime(LocalDateTime.of(2020, 8, 8, 8, 0));

        var reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationStub));

        assertThrows(IllegalArgumentException.class, () -> reservationService.cancelReservation(reservationId));
    }

    @Test
    void cancelReservationThatNotExistsShouldThrowAnException() {
        var reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.cancelReservation(reservationId));
    }

    @Test
    void cancelReservationShouldReturnTheReservation() {
        var reservationDtoStub = reservationDTOStub();
        var reservationStub = reservationStub();

        var reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationStub));
        when(reservationRepository.save(reservationStub)).thenReturn(reservationStub);
        when(reservationMapper.map(reservationStub)).thenReturn(reservationDtoStub);

        var actualStub = reservationService.cancelReservation(reservationId);

        assertEquals(reservationDtoStub, actualStub);
    }

    @Test
    void getRefundValueShouldReturnFullRefund() {
        var scheduleStub = scheduleStub();
        scheduleStub.setStartDateTime(LocalDateTime.now().plusDays(2));
        var reservationStub = reservationStub();
        reservationStub.setSchedule(scheduleStub);

        var actualStub = reservationService.getRefundValue(reservationStub);

        assertEquals(new BigDecimal(10), actualStub);
    }

    @Test
    void getRefundValueShouldReturnThirdRefund() {
        var scheduleStub = scheduleStub();
        scheduleStub.setStartDateTime(LocalDateTime.now().plusHours(13));
        var reservationStub = reservationStub();
        reservationStub.setSchedule(scheduleStub);

        var actualStub = reservationService.getRefundValue(reservationStub);

        assertEquals(new BigDecimal("7.50"), actualStub);
    }

    @Test
    void getRefundValueShouldReturnHalfRefund() {
        var scheduleStub = scheduleStub();
        scheduleStub.setStartDateTime(LocalDateTime.now().plusHours(3));
        var reservationStub = reservationStub();
        reservationStub.setSchedule(scheduleStub);

        var actualStub = reservationService.getRefundValue(reservationStub);

        assertEquals(new BigDecimal("5.0"), actualStub);
    }

    @Test
    void getRefundValueShouldReturnQuarterRefund() {
        var scheduleStub = scheduleStub();
        scheduleStub.setStartDateTime(LocalDateTime.now().plusHours(1).plusMinutes(1));
        var reservationStub = reservationStub();
        reservationStub.setSchedule(scheduleStub);

        var actualStub = reservationService.getRefundValue(reservationStub);

        assertEquals(new BigDecimal("2.50"), actualStub);
    }

    @Test
    void getRefundValueShouldReturnNoneRefund() {
        var scheduleStub = scheduleStub();
        scheduleStub.setStartDateTime(LocalDateTime.now().minusHours(1));
        var reservationStub = reservationStub();
        reservationStub.setSchedule(scheduleStub);

        var actualStub = reservationService.getRefundValue(reservationStub);

        assertEquals(new BigDecimal(0), actualStub);
    }

    @Test
    void rescheduleReservationShouldReturnTheRescheduledReservation() {
        var reservationDtoStub = reservationDTOStub();
        var reservationStub = reservationStub();
        reservationStub.getGuest().setId(1L);

        var reservationId = 1L;
        var scheduleId = 2L;

        var createReservationDTO = createReservationRequestDTOStub();
        createReservationDTO.setScheduleId(scheduleId);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationStub));
        when(reservationMapper.map(reservationStub)).thenReturn(reservationDtoStub);
        when(reservationMapper.map(createReservationDTO)).thenReturn(reservationStub);
        when(reservationRepository.save(reservationStub)).thenReturn(reservationStub);

        var expectedStub = reservationService.rescheduleReservation(reservationId, scheduleId);

        reservationStub.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationStub.setValue(new BigDecimal(0));
        reservationStub.setRefundValue(new BigDecimal(10));
        reservationDtoStub.setPreviousReservation(reservationMapper.map(reservationStub));

        assertEquals(reservationDtoStub, expectedStub);
    }

    @Test
    void rescheduleReservationShouldThrowAnException() {
        var reservationDtoStub = reservationDTOStub();
        var reservationStub = reservationStub();

        var reservationId = 1L;
        var scheduleId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationStub));
        when(reservationMapper.map(reservationStub)).thenReturn(reservationDtoStub);

        var expectedStub = reservationService.rescheduleReservation(reservationId, scheduleId);

        assertEquals(reservationDtoStub, expectedStub);
    }

    @Test
    void shouldReturnIfResevationExistsByScheduleId() {
        var scheduleId = 1L;

        when(reservationRepository.existsBySchedule_Id(scheduleId)).thenReturn(true);

        assertTrue(reservationService.existsReservationByScheduleId(scheduleId));
    }
}