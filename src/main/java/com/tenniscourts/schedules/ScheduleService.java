package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.ReservationService;
import com.tenniscourts.schedules.model.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.model.Schedule;
import com.tenniscourts.schedules.model.ScheduleDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ReservationService reservationService;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        var schedule = scheduleMapper.map(createScheduleRequestDTO);
        schedule.setEndDateTime(schedule.getStartDateTime().plusHours(1));
        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> findFreeSchedulesByDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return scheduleRepository.
                findSchedulesByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDateTime, endDateTime).stream()
                .map(this::verifyScheduleReservation)
                .filter(Objects::nonNull)
                .map(schedule -> this.addTennisCourtIdAndMapToDTO(schedule.getTennisCourt().getId(), schedule))
                .collect(Collectors.toList());
    }

    public ScheduleDTO findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
                    throw new EntityNotFoundException("Schedule not found");
                }
        );
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleRepository.findByTennisCourtIdOrderByStartDateTime(tennisCourtId).stream()
                .map(scheduleMapper::map)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> findFreeSchedulesByTennisCourtAndDates(Long tenniCourtId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return scheduleRepository.findSchedulesByTennisCourtIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                        tenniCourtId, startDateTime, endDateTime).stream()
                .map(this::verifyScheduleReservation)
                .filter(Objects::nonNull)
                .map(schedule -> this.addTennisCourtIdAndMapToDTO(schedule.getTennisCourt().getId(), schedule))
                .collect(Collectors.toList());
    }

    private Schedule verifyScheduleReservation(Schedule schedule) {
        if (reservationService.existsReservationByScheduleId(schedule.getId())) {
            return null;
        }
        return schedule;
    }

    private ScheduleDTO addTennisCourtIdAndMapToDTO(Long tenniCourtId, Schedule schedule) {
        var scheduleDto = scheduleMapper.map(schedule);
        scheduleDto.setTennisCourtId(tenniCourtId);
        return scheduleDto;
    }

}
