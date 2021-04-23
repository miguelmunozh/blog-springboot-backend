package com.miguelmunozh.services;

import com.miguelmunozh.dto.PostDTO;
import com.miguelmunozh.exceptions.PostNotFoundException;
import com.miguelmunozh.exceptions.TopicSectionNotFoundException;
import com.miguelmunozh.exceptions.UserNotFoundException;
import com.miguelmunozh.models.Post;
import com.miguelmunozh.models.TopicSection;
import com.miguelmunozh.models.User;
import com.miguelmunozh.repositories.PostRepository;
import com.miguelmunozh.repositories.TopicSectionRepository;
import com.miguelmunozh.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final TopicSectionRepository topicSectionRepository;


    public PostDTO save(PostDTO postDTO){
        return mapDTOToPost(postDTO);
    }

    public PostDTO update(PostDTO postDTO){
        Post post = postRepository.findById(postDTO.getPostId())
                .orElseThrow(()-> new PostNotFoundException("Post Not Found Exception"));
        post.setPostTitle(postDTO.getPostName());
        post.setVoteCount(postDTO.getVoteCount());
        post.setDescription(postDTO.getDescription());
        post.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        postRepository.save(post);
        return postDTO;
    }

    private PostDTO mapDTOToPost(PostDTO postDTO) {
        // get the user who created the post
        User user = userRepository.findById(authService.getCurrentUser().getUserId())
                .orElseThrow(()->new UserNotFoundException("User Not Found Exception in section creation"));

        TopicSection topicSection = topicSectionRepository.findById(postDTO.getTopicSectionId())
                .orElseThrow(()->new TopicSectionNotFoundException("Topic Section Was Not Found"));

        Post post = new Post();
        post.setUser(user);
        post.setTopicSection(topicSection);

        post.setPostTitle(postDTO.getPostName());
        post.setDescription(postDTO.getDescription());
        post.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        post.setVoteCount(postDTO.getVoteCount());
        postRepository.save(post);

        return postDTO;
    }


    public PostDTO getPostById(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException("Post Not Found Exception"));
        //map the post to DTO
        return mapPostToDTO(post);
    }

    private PostDTO mapPostToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getPostId());
        postDTO.setPostName(post.getPostTitle());
        postDTO.setDescription(post.getDescription());
        String s = new SimpleDateFormat("MM/dd/yyyy").format(post.getCreatedDate());
        postDTO.setCreatedDate(s);
        postDTO.setVoteCount(post.getVoteCount());
        postDTO.setTopicName(post.getTopicSection().getTitle());
        postDTO.setTopicSectionId(post.getTopicSection().getTopicId());
        postDTO.setUserId(post.getUser().getUserId());
        postDTO.setUserName(post.getUser().getUsername());
        return postDTO;
    }

    public List<PostDTO> getPostsByTopicSectionId(Long topicSectionId){
        TopicSection topicSection = topicSectionRepository.findById(topicSectionId)
                .orElseThrow(()->new TopicSectionNotFoundException("Topic section was not found"));
        List<PostDTO> list  = new ArrayList<>();
        for (Post post : postRepository.findAllByTopicSection(topicSection)){
            // map post to DTO
            PostDTO postDTO = mapPostToDTO(post);
            list.add(postDTO);
        }
        return list;
    }

    public void deletePostById(Long id){
        postRepository.deleteById(id);
    }
//    public List<Post> getPostByUsername(String username){
//        User user = userRepository.findByUsername(username);
//        return postRepository.findPostsByUser(user);
//    }

}
