package com.tenniscourts.guests.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGuestDTO {

    @NotNull
    private String name;
}
