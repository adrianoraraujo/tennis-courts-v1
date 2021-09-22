package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.schedules.model.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.model.ScheduleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedule")
@Api("Schedule resource, operations to manage schedules")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ApiOperation("Add a schedule to a tennis court")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a schedule"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @GetMapping("/{scheduleId}")
    @ApiOperation("Find schedule by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the schedule"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Schedule not found")})
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findScheduleById(scheduleId));
    }

    @GetMapping("/freeslots")
    @ApiOperation("Find all schedules in a given date period.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved free slots on period"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<List<ScheduleDTO>> findFreeSchedulesByDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findFreeSchedulesByDates(
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @GetMapping("/freeslots/{tennisCourtId}")
    @ApiOperation("Find Schedules by tennis court given a date period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved court free slots on period"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<List<ScheduleDTO>> findFreeSchedulesByTennisCourtAndDates(
            @PathVariable Long tennisCourtId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findFreeSchedulesByTennisCourtAndDates(tennisCourtId,
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }
}
