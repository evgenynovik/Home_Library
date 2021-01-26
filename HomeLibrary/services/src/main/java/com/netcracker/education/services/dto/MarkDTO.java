package com.netcracker.education.services.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkDTO {
    @JsonFormat(pattern = "##.##")
    private Double mark;
}
