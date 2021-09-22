package com.tenniscourts.schedules.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {

    @ApiModelProperty("Schedule identifier")
    private Long id;

    @ApiModelProperty("The tennis court related to this schedule")
    private TennisCourtDTO tennisCourt;

    @NotNull
    @ApiModelProperty("The identifier of tennis courted related to thid schedule")
    private Long tennisCourtId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    @ApiModelProperty("The schedule start date and time")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty("The schedule start date and time")
    private LocalDateTime endDateTime;

}
