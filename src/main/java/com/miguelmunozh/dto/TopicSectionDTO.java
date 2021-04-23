package com.miguelmunozh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicSectionDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String description;
    private String createdDate;
}
