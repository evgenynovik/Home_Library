package com.netcracker.education.services.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSeriesDTO {

    @ApiModelProperty(hidden = true, position = 1)
    private Integer id;
    @NotNull(message = "Please provide a title")
    @Size(min = 2, max = 50)
    private String title;
    @NotNull(message = "Please provide a description")
    @Size(min = 2, max = 120)
    private String description;
}
