package com.netcracker.education.services.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {

    @ApiModelProperty(hidden = true, position = 1)
    private Integer id;
    @ApiModelProperty(hidden = true)
    private LocalDate date;
    @ApiModelProperty(hidden = true)
    private Integer userCardId;
}
