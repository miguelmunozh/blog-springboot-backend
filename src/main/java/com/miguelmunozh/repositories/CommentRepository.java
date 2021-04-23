package com.miguelmunozh.repositories;

import com.miguelmunozh.models.Comment;
import com.miguelmunozh.models.Post;
import com.miguelmunozh.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentsByPost(Post post);
//    List<Comment> findAllByUser(User user);
//    List<Comment> findByPost();
}
