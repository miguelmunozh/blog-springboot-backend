package com.miguelmunozh.repositories;

import com.miguelmunozh.models.Post;
import com.miguelmunozh.models.TopicSection;
import com.miguelmunozh.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTopicSection(TopicSection topicSection);
//    List<Post> findPostsByUser(User user);
}
