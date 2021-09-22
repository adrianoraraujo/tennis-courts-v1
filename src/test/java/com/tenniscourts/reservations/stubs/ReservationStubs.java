package com.tenniscourts.reservations.stubs;

import com.tenniscourts.reservations.model.CreateReservationRequestDTO;
import com.tenniscourts.reservations.model.Reservation;
import com.tenniscourts.reservations.model.ReservationDTO;
import com.tenniscourts.reservations.model.ReservationStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.tenniscourts.reservations.stubs.ScheduleStubs.scheduleDTOStub;
import static com.tenniscourts.reservations.stubs.ScheduleStubs.scheduleStub;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationStubs {

    public static CreateReservationRequestDTO createReservationRequestDTOStub() {
        return new CreateReservationRequestDTO(1L, 1L);
    }

    public static ReservationDTO reservationDTOStub() {
        return ReservationDTO.builder()
                .id(1L)
                .guestId(1L)
                .reservationStatus(ReservationStatus.READY_TO_PLAY.toString())
                .schedule(scheduleDTOStub())
                .scheduledId(scheduleDTOStub().getId())
                .value(new BigDecimal(10))
                .build();
    }

    public static Reservation reservationStub() {
        return Reservation.builder()
                .guest(GuestStubs.guestStub())
                .schedule(scheduleStub())
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .value(new BigDecimal(10))
                .build();
    }
}
