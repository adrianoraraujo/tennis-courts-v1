package com.tenniscourts.reservations.model;

import com.tenniscourts.config.persistence.BaseEntity;
import com.tenniscourts.guests.model.Guest;
import com.tenniscourts.schedules.model.Schedule;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Reservation extends BaseEntity<Long> {

    @OneToOne
    private Guest guest;

    @ManyToOne
    @NotNull
    private Schedule schedule;

    @NotNull
    private BigDecimal value;

    @NotNull
    private ReservationStatus reservationStatus;

    private BigDecimal refundValue;
}
