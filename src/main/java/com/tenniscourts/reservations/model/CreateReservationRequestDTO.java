package com.tenniscourts.reservations.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequestDTO {

    @NotNull
    private Long guestId;

    @NotNull
    private Long scheduleId;

}
