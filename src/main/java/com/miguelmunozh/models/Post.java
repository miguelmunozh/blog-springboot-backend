package com.miguelmunozh.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;
    @NotBlank(message = "Post Title cannot be empty or Null")
    private String postTitle;
    @Nullable
    @Lob
    @Column
    private String description;
    private Integer voteCount = 0;
    private Timestamp createdDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "topicId", referencedColumnName = "topicId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TopicSection topicSection;
}
