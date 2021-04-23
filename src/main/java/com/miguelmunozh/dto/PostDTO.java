package com.miguelmunozh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long postId;
    private String postName;
    private String description;
    private String createdDate;
    private int voteCount;

    private Long topicSectionId;
    private String topicName;

    private Long userId;
    private String userName;
}
