package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.guests.model.CreateGuestDTO;
import com.tenniscourts.guests.model.GuestDTO;
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
@RequestMapping("/guest")
@Api("Guests Resource, operations to manage guests")
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    @ApiOperation("Create a new guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a guest"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<Void> saveGuest(@RequestBody CreateGuestDTO createGuestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.saveGuest(createGuestDTO).getId())).build();
    }

    @GetMapping("/{guestId}")
    @ApiOperation("Find a guest by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the guest"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Guest not found")})
    public ResponseEntity<GuestDTO> findGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping("/find/{guestName}")
    @ApiOperation("Find a guest by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the guest"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Guest not found")})
    public ResponseEntity<List<GuestDTO>> findByGuestName(@PathVariable String guestName) {
        return ResponseEntity.ok(guestService.findByGuestName(guestName));
    }

    @GetMapping
    @ApiOperation("List all guests")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the guest"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<List<GuestDTO>> findAllGuests() {
        return ResponseEntity.ok(guestService.findAllGuests());
    }

    @PutMapping("/{guestId}")
    @ApiOperation("Update a guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the guest"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<GuestDTO> updateGuest(@PathVariable Long guestId, @RequestBody CreateGuestDTO createGuestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestId, createGuestDTO));
    }

    @DeleteMapping("/{guestId}")
    @ApiOperation("Delete a guest by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted the guest"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Guest not found")})
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }

}
