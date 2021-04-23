package com.miguelmunozh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String createdDate;
    private String text;

    private Long postId;

    private Long userId;
    private String userName;
}
