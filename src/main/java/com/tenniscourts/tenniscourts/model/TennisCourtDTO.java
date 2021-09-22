package com.tenniscourts.tenniscourts.model;

import com.tenniscourts.schedules.model.ScheduleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TennisCourtDTO {

    @ApiModelProperty("Tennis court identifier")
    private Long id;

    @NotNull
    @ApiModelProperty("Tennis court name")
    private String name;

    @ApiModelProperty("Tennis court schedules")
    private List<ScheduleDTO> tennisCourtSchedules;

}
