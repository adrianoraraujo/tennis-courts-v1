package com.tenniscourts.guests.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {

    @ApiModelProperty("Guest identifier")
    private Long id;

    @ApiModelProperty("Guest name")
    private String name;
}
