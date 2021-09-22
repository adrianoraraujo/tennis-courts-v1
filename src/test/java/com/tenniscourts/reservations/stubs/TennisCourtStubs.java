package com.tenniscourts.reservations.stubs;

import com.tenniscourts.tenniscourts.model.TennisCourt;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TennisCourtStubs {
    public static TennisCourtDTO tennisCourtDTOStub() {
        return TennisCourtDTO.builder()
                .id(1L)
                .name("Test Court")
                .build();
    }

    public static TennisCourt tennisCourtStub(){
        return new TennisCourt("Test Court");
    }

}
