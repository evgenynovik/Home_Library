package com.netcracker.education.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserDTO {

    @ApiModelProperty(hidden = true, position = 1)
    private Integer id;
    @NotNull(message = "Please provide a name")
    @Size(min = 2, max = 50)
    private String name;
    @NotNull(message = "Please provide a password")
    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 3, max = 50)
    private String password;
    @ApiModelProperty(hidden = true)
    private String role;
    @ApiModelProperty(hidden = true)
    private Integer userCardId;
}
