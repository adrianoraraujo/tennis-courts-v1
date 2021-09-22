package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.model.CreateReservationRequestDTO;
import com.tenniscourts.reservations.model.Reservation;
import com.tenniscourts.reservations.model.ReservationDTO;
import com.tenniscourts.reservations.model.ReservationStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        var reservation = reservationMapper.map(createReservationRequestDTO);
        reservation.setValue(new BigDecimal(10));
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        return reservationMapper.map(reservationRepository.save(reservation));
    }

    public ReservationDTO findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public List<ReservationDTO> findPastReservations() {
        var dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0));
        return reservationRepository.findByScheduleStartDateTimeLessThanEqual(dateTime)
                .stream().map(reservationMapper::map).collect(Collectors.toList());
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue) {
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());


        if (hours > 24) {
            return reservation.getValue();
        } else if (hours <= 23.59 && hours >= 12) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.75));
        } else if (hours <= 11.59 && hours >= 2) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.5));
        } else if (hours <= 1.59 && hours > 0.01) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.25));
        }

        return BigDecimal.ZERO;
    }

    //I think that to resolve it, Only need to return the same reservation without cancel or reschedule it
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {

        ReservationDTO previousReservationToCheck = findReservationById(previousReservationId);

        if (scheduleId.equals(previousReservationToCheck.getSchedule().getId())) {
            return previousReservationToCheck;
        }

        var previousReservation = cancel(previousReservationId);

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation =
                bookReservation(new CreateReservationRequestDTO(previousReservation.getGuest().getId(), scheduleId));
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    public boolean existsReservationByScheduleId(Long scheduleId) {
        return reservationRepository.existsBySchedule_Id(scheduleId);
    }
}
