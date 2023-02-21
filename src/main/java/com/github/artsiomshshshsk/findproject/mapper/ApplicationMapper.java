package com.github.artsiomshshshsk.findproject.mapper;

import com.github.artsiomshshshsk.findproject.domain.Application;
import com.github.artsiomshshshsk.findproject.dto.ApplicationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    ApplicationResponse toApplicationResponse(Application application);
}
