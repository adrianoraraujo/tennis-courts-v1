package com.tenniscourts.schedules.model;

import com.tenniscourts.config.persistence.BaseEntity;
import com.tenniscourts.reservations.model.Reservation;
import com.tenniscourts.tenniscourts.model.TennisCourt;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "reservations")
public class Schedule extends BaseEntity<Long> {

    @ManyToOne
    @NotNull
    private TennisCourt tennisCourt;

    @Column
    @NotNull
    private LocalDateTime startDateTime;

    @Column
    @NotNull
    private LocalDateTime endDateTime;

    @OneToMany
    private List<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }

        reservation.setSchedule(this);
        this.reservations.add(reservation);
    }
}
