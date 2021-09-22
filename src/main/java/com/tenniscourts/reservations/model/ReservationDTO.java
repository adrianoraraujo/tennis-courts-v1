package com.tenniscourts.reservations.model;

import com.tenniscourts.schedules.model.ScheduleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ReservationDTO {

    @ApiModelProperty("Reservation identifier")
    private Long id;

    @ApiModelProperty("The schedule time of the reservation")
    private ScheduleDTO schedule;

    @ApiModelProperty("READY_TO_PLAY, RESCHEDULED or CANCELED")
    private String reservationStatus;

    @ApiModelProperty("If rescheduled, holds the previous reservation")
    private ReservationDTO previousReservation;

    @ApiModelProperty("If canceled or rescheduled, holds the refund value")
    private BigDecimal refundValue;

    @ApiModelProperty("The value to do a reservation")
    private BigDecimal value;

    @NotNull
    @ApiModelProperty("The identifier of the schedule associated to this reservation")
    private Long scheduledId;

    @NotNull
    @ApiModelProperty("The identifier of the guest associated to this reservation")
    private Long guestId;
}
