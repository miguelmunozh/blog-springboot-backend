package com.miguelmunozh.controllers;

import com.miguelmunozh.dto.CommentDTO;
import com.miguelmunozh.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/createComment")
    public CommentDTO createComment(@RequestBody CommentDTO commentDTO){
        return commentService.save(commentDTO);
    }

    @GetMapping("/by-post/{id}")
    public List<CommentDTO> getAllCommentsByPostId(@PathVariable Long id){
        return commentService.getCommentsByPostId(id);
    }

    @GetMapping("/delete-comment/{id}")
    public void deleteCommentByPostId(@PathVariable Long id){
        commentService.deleteCommentById(id);
    }
    @PutMapping("/updateComment")
    public CommentDTO updateComment(@RequestBody CommentDTO commentDTO){
        return commentService.update(commentDTO);
    }
}
