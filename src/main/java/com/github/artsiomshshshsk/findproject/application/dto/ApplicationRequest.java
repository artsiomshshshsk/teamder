package com.github.artsiomshshshsk.findproject.application.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request object for creating a new application")
@Data
@Builder
public class ApplicationRequest{
        @NotBlank
        @ApiModelProperty(value = "The application message", required = true)
        String applicationMessage;

        @NotBlank
        @ApiModelProperty(value = "The role request", required = true)
        String roleRequest;

        @ApiModelProperty(value = "The CV file", required = false)
        MultipartFile cv;

        @ApiModelProperty(value = "contact info", required = false)
        String contact;

}

