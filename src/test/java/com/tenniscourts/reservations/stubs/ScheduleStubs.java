package com.tenniscourts.reservations.stubs;

import com.tenniscourts.schedules.model.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.model.Schedule;
import com.tenniscourts.schedules.model.ScheduleDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tenniscourts.reservations.stubs.TennisCourtStubs.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleStubs {

    private static final LocalDateTime startDateTime = LocalDateTime.of(2021, 8, 8, 8, 0);
    private static final LocalDateTime endDateTime = LocalDateTime.of(2021, 8, 8, 9, 0);

    public static CreateScheduleRequestDTO createScheduleRequestDTOStub() {
        return new CreateScheduleRequestDTO(1L, startDateTime);
    }

    public static ScheduleDTO scheduleDTOStub() {
        return ScheduleDTO.builder()
                .id(1L)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .tennisCourt(tennisCourtDTOStub())
                .tennisCourtId(tennisCourtDTOStub().getId())
                .build();
    }

    public static Schedule scheduleStub() {
        return Schedule.builder()
                .tennisCourt(TennisCourtStubs.tennisCourtStub())
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }
}
