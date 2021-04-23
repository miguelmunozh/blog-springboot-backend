package com.miguelmunozh.services;

import com.miguelmunozh.dto.TopicSectionDTO;
import com.miguelmunozh.exceptions.TopicSectionNotFoundException;
import com.miguelmunozh.exceptions.UserNotFoundException;
import com.miguelmunozh.models.TopicSection;
import com.miguelmunozh.models.User;
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
public class TopicSectionService {

    private final TopicSectionRepository topicSectionRepository;
    private final AuthService authService;
    private final UserRepository userRepository;


    public TopicSectionDTO save(TopicSectionDTO topicSectionDTO){
        return mapDTOSaveTopicSection(topicSectionDTO);
    }

    public TopicSectionDTO updateTopic(TopicSectionDTO topicSectionDTO){
        TopicSection topicSection = topicSectionRepository.findById(topicSectionDTO.getId())
                .orElseThrow(()->new TopicSectionNotFoundException("Topic Not Found"));
        topicSection.setTitle(topicSectionDTO.getTitle());
        topicSection.setDescription(topicSectionDTO.getDescription());
        topicSection.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        topicSectionRepository.save(topicSection);
        return topicSectionDTO;
    }

    public List<TopicSectionDTO> getAllTopicSections(){
        List<TopicSectionDTO> list  = new ArrayList<>();
        for (TopicSection obj:topicSectionRepository.findAll()){
            list.add(mapTopicSectionToDTO(obj));
        }
        return list;
    }

    public TopicSectionDTO getTopicSectionById(Long TopicSectionId){
        TopicSection topicSection = topicSectionRepository.findById(TopicSectionId)
                .orElseThrow(()->new TopicSectionNotFoundException("Topic Section not found"));
        return mapTopicSectionToDTO(topicSection);
    }

    private TopicSectionDTO mapDTOSaveTopicSection(TopicSectionDTO topicSectionDTO){
        TopicSection topicSection = new TopicSection();
        // set the user it belongs to, by getting the current user logged in
        User user =userRepository.findById(authService.getCurrentUser().getUserId())
                .orElseThrow(()->new UserNotFoundException("User Not Found Exception in section creation"));
        topicSection.setUser(user);

        topicSection.setTopicId(topicSectionDTO.getId());
        topicSection.setTitle(topicSectionDTO.getTitle());
        topicSection.setDescription(topicSectionDTO.getDescription());
        topicSection.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        // save the topic section in the db
        topicSectionRepository.save(topicSection);

        // but im returning a dto with more info encapsulated
        topicSectionDTO.setUserName(user.getUsername());
        return topicSectionDTO;
    }
    private TopicSectionDTO mapTopicSectionToDTO(TopicSection obj) {
        TopicSectionDTO topicSectionDTO = new TopicSectionDTO();
        User user = obj.getUser();

        topicSectionDTO.setId(obj.getTopicId());
        topicSectionDTO.setTitle(obj.getTitle());
        topicSectionDTO.setDescription(obj.getDescription());
        // format the date to return to the client, date in dto is a string
        String s = new SimpleDateFormat("MM/dd/yyyy").format(obj.getCreatedDate());
        topicSectionDTO.setCreatedDate(s);
        topicSectionDTO.setUserName(user.getUsername());
        topicSectionDTO.setUserId(user.getUserId());
        return topicSectionDTO;
    }

    public void deleteTopicById(Long id) {
        topicSectionRepository.deleteById(id);
    }
}
