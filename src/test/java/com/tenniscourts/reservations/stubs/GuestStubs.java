package com.tenniscourts.reservations.stubs;

import com.tenniscourts.guests.model.CreateGuestDTO;
import com.tenniscourts.guests.model.Guest;
import com.tenniscourts.guests.model.GuestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuestStubs {

    public static CreateGuestDTO createGuestDTOStub(){
        return new CreateGuestDTO("Andrews");
    }

    public static Guest guestStub(){
        return new Guest("Andrews");
    }

    public static GuestDTO guestDTOStub(){
        return new GuestDTO(1L ,"Andrews");
    }


}
