package com.miguelmunozh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "topics")
public class TopicSection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long topicId;
    @NotBlank(message = "Title cannot be empty or Null")
    private String title;
    @Lob
    @Column
    private String description;
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<Post> posts;
    private Timestamp createdDate;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
