package com.tenniscourts.guests;

import com.tenniscourts.guests.model.CreateGuestDTO;
import com.tenniscourts.guests.model.Guest;
import com.tenniscourts.guests.model.GuestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    Guest map(GuestDTO source);

    GuestDTO map(Guest source);

    Guest map(CreateGuestDTO source);
}
