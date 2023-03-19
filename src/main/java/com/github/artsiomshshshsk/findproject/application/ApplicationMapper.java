package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.project.dto.AppliedResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    ApplicationResponse toApplicationResponse(Application application);
    default AppliedResponse toAppliedResponse(Application application){
        return  AppliedResponse.builder()
                .applicationId(application.getId())
                .resumeURL(application.getResumeURL())
                .message(application.getMessage())
                .roleRequest(application.getRoleRequest().getName())
                .applicationDate(application.getApplicationDate())
                .status(application.getStatus())
                .build();
    }
}
