package com.tenniscourts.tenniscourts;

import com.tenniscourts.tenniscourts.model.TennisCourt;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TennisCourtMapper {
    TennisCourtDTO map(TennisCourt source);

    @InheritInverseConfiguration
    TennisCourt map(TennisCourtDTO source);
}
