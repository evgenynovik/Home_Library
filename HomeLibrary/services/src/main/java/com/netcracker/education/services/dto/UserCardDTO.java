package com.netcracker.education.services.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCardDTO {

    @ApiModelProperty(hidden = true, position = 1)
    private Integer id;
    @NotNull
    @Email
    @Size(min = 3, max = 50)
    private String email;
    @ApiModelProperty(hidden = true)
    private Integer userId;
    @ApiModelProperty(hidden = true)
    @Builder.Default
    private Boolean permission = false;
    @Builder.Default
    private Set<Integer> booksId = new HashSet<>();
    @ApiModelProperty(hidden = true)
    @Builder.Default
    private Set<String> booksTitles = new HashSet<>();
    @Builder.Default
    private Set<Integer> reviewsId = new HashSet<>();
}