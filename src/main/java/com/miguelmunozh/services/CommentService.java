package com.miguelmunozh.services;

import com.miguelmunozh.dto.CommentDTO;
import com.miguelmunozh.exceptions.CommentNotFoundException;
import com.miguelmunozh.exceptions.PostNotFoundException;
import com.miguelmunozh.exceptions.UserNotFoundException;
import com.miguelmunozh.models.Comment;
import com.miguelmunozh.models.Post;
import com.miguelmunozh.models.User;
import com.miguelmunozh.repositories.CommentRepository;
import com.miguelmunozh.repositories.PostRepository;
import com.miguelmunozh.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    public CommentDTO save(CommentDTO commentDTO){
        // set who created the comment and from which post
        User user = userRepository.findById(authService.getCurrentUser().getUserId())
                .orElseThrow(()->new UserNotFoundException("User Not Found Exception in section creation"));
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(()->new PostNotFoundException("Post Was Not Found"));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);

        comment.setText(commentDTO.getText());
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);

        return commentDTO;
    }

    public CommentDTO update(CommentDTO commentDTO){
        // set who created the comment and from which post
        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(()->new CommentNotFoundException("Comment Not Found"));
        //
        comment.setText(commentDTO.getText());
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);

        return commentDTO;
    }

    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public Comment findCommentById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(()->new CommentNotFoundException("Comment Not Found"));
    }

    public void deleteCommentById(Long id){
        commentRepository.deleteById(id);
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new CommentNotFoundException("Post Was Not Found"));
        List<CommentDTO> list = new ArrayList<>();
        // get the list of comments for this post, convert them to dto and return the list of dto
        for(Comment comment: commentRepository.findCommentsByPost(post)){
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setText(comment.getText());
            String s = new SimpleDateFormat("MM/dd/yyyy").format(comment.getCreatedDate());
            commentDTO.setCreatedDate(s);
            commentDTO.setPostId(comment.getPost().getPostId());
            commentDTO.setUserId(comment.getUser().getUserId());
            commentDTO.setUserName(comment.getUser().getUsername());
            list.add(commentDTO);
        }
        return list;
    }
}
