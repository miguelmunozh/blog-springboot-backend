package com.miguelmunozh.controllers;

import com.miguelmunozh.dto.TopicSectionDTO;
import com.miguelmunozh.services.TopicSectionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Topics")
@AllArgsConstructor
public class TopicSectionController {
    private final TopicSectionService topicSectionService;

    @PostMapping("/createTopic")
    public TopicSectionDTO save(@RequestBody TopicSectionDTO topicSectionDTO){
        return topicSectionService.save(topicSectionDTO);
    }
    @PutMapping("/updateTopic")
    public TopicSectionDTO updateTopic(@RequestBody TopicSectionDTO topicSectionDTO){
        return topicSectionService.updateTopic(topicSectionDTO);
    }
    @GetMapping("")
    public List<TopicSectionDTO> getAllTopicSections(){
        return topicSectionService.getAllTopicSections();
    }

    @GetMapping("/{id}")
    public TopicSectionDTO getTopicSectionById(@PathVariable Long id){
        return topicSectionService.getTopicSectionById(id);
    }
    @GetMapping("/delete-topic/{id}")
    public void deleteCommentByPostId(@PathVariable Long id){
        topicSectionService.deleteTopicById(id);
    }
}
