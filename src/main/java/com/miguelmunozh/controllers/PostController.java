package com.miguelmunozh.controllers;

import com.miguelmunozh.dto.PostDTO;
import com.miguelmunozh.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/createPost")
    public PostDTO createPost(@RequestBody PostDTO postDTO){
        return postService.save(postDTO);
    }

    @PutMapping("/updatePost")
    public PostDTO updatePost(@RequestBody PostDTO postDTO){
        return postService.update(postDTO);
    }

    @GetMapping("/by-Topic/{id}")
    public List<PostDTO> getPostsByTopicSectionId(@PathVariable Long id){
        return postService.getPostsByTopicSectionId(id);
    }
    @GetMapping("/delete-post/{id}")
    public void deletePostById(@PathVariable Long id){
        postService.deletePostById(id);
    }

    @GetMapping("/get-post/{id}")
    public PostDTO getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }
}
