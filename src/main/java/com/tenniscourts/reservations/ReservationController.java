package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.reservations.model.CreateReservationRequestDTO;
import com.tenniscourts.reservations.model.ReservationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
@Api("Reservation resource, operations to manage reservations")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation("Book a reservation given a guest and a scheduleId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully booked a reservation"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{reservationId}")
    @ApiOperation("Find a reservation given a reservationId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the reservation"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Reservation not found")})
    public ResponseEntity<ReservationDTO> findReservationById(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservationById(reservationId));
    }

    @GetMapping("/cancel/{reservationId}")
    @ApiOperation("Cancel a reservation given a reservation Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully canceled the reservation"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Reservation not found")})
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @GetMapping("/reschedule")
    @ApiOperation("Reschedule a reservation given a reservationId and a new scheduleId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully rescheduled the reservation"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Reservation not found")})
    public ResponseEntity<ReservationDTO> rescheduleReservation(@RequestParam Long reservationId,
                                                                @RequestParam Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }

    @GetMapping("/history")
    @ApiOperation("Return the all the reservations before the actual date")
    @ApiResponses(@ApiResponse(code = 200, message = "Successfully retrieved the reservation history"))
    public ResponseEntity<List<ReservationDTO>> reservationHistoric() {
        return ResponseEntity.ok(reservationService.findPastReservations());
    }
}
