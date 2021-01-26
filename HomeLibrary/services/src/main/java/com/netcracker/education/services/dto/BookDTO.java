package com.netcracker.education.services.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    @ApiModelProperty(hidden = true, position = 1)
    private Integer id;
    @NotNull(message = "Please provide a title")
    @Size(min = 3, max = 100, message = "Please provide a correct title")
    private String title;
    @NotNull(message = "Please provide a year")
    @Min(500)
    @Max(2021)
    private Integer year;
    @NotNull(message = "Please provide a description")
    @Size(min = 3, max = 200, message = "Please provide a correct description")
    private String description;
    @ApiModelProperty(hidden = true)
    @Builder.Default
    private Double rating = 0.00;
    @Singular("authorId")
    private Set<Integer> authorsId = new HashSet<>();
    @ApiModelProperty(hidden = true)
    @Singular("authorName")
    private Set<String> authorsName = new HashSet<>();
    private Integer genreId;
    @ApiModelProperty(hidden = true)
    private String genreTitle;
    private Integer bookSeriesId;
    @ApiModelProperty(hidden = true)
    private String bookSeriesTitle;
    @ApiModelProperty(hidden = true)
    private Integer userCardId;
}
