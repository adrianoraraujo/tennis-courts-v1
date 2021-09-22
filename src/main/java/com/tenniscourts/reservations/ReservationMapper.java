package com.tenniscourts.reservations;

import com.tenniscourts.reservations.model.CreateReservationRequestDTO;
import com.tenniscourts.reservations.model.Reservation;
import com.tenniscourts.reservations.model.ReservationDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation map(ReservationDTO source);

    @InheritInverseConfiguration
    @Mapping(target = "guestId", source = "guest.id")
    @Mapping(target = "scheduledId", source = "schedule.id")
    ReservationDTO map(Reservation source);

    @Mapping(target = "guest.id", source = "guestId")
    @Mapping(target = "schedule.id", source = "scheduleId")
    Reservation map(CreateReservationRequestDTO source);
}
