package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController()
@RequestMapping("/tennis-court")
@Api("Tennis Court resource, Cceate and find operations")
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @PostMapping
    @ApiOperation("Create a new tennis court")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added a tennis court"),
            @ApiResponse(code = 400, message = "Something wrong with the request")})
    public ResponseEntity<Void> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }


    @GetMapping("/{tennisCourtId}")
    @ApiOperation("Find a tennis court by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the tennis court"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Tennis court not found")})
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }


    @GetMapping("/{tennisCourtId}/schedules")
    @ApiOperation("Find tennis court by id with schedules")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the tennis court"),
            @ApiResponse(code = 400, message = "Something wrong with the request"),
            @ApiResponse(code = 404, message = "Tennis court not found")})
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
