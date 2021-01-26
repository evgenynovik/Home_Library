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
public class AuthorDTO {

    @ApiModelProperty(hidden = true, position = 1)
    private Integer id;
    @NotNull(message = "Please provide a firstName")
    @Size(min = 2, max = 50, message = "Please provide a correct firstName")
    private String firstName;
    @NotNull(message = "Please provide a firstName")
    @Size(min = 2, max = 50, message = "Please provide a correct lastName")
    private String lastName;
    @NotNull(message = "Please provide a description")
    @Size(min = 2, max = 120)
    private String description;
    @NotNull(message = "Please provide a country")
    @Size(min = 2, max = 50, message = "Please provide a correct country")
    private String country;
}
