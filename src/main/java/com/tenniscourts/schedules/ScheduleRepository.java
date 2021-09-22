package com.tenniscourts.schedules;

import com.tenniscourts.schedules.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourtIdOrderByStartDateTime(Long id);

    List<Schedule> findSchedulesByTennisCourtIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
            Long tennisCourtId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Schedule> findSchedulesByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
            LocalDateTime startDateTime, LocalDateTime endDateTime);

}