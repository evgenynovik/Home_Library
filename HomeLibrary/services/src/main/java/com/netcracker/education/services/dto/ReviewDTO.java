package com.netcracker.education.services.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    @ApiModelProperty(hidden = true, position = 1)
    private Integer id;
    @NotNull(message = "Please provide a review content")
    @Size(min = 2, max = 200)
    private String content;
    @NotNull(message = "Please provide a mark")
    @Min(1)
    @Max(10)
    private Integer rating;
    @NotNull(message = "Please provide a book id")
    @Min(0)
    private Integer bookId;
    @ApiModelProperty(hidden = true)
    private String bookTitle;
    @ApiModelProperty(hidden = true)
    private Integer userCardId;
}
