package com.kosign.push.topics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic,String> {
    List<Topic> findAllByApplicationIdAndStatus(String appId, Character active);

    List<Topic>  findByNameAndApplicationId(String topicName, String appId);

    /* unique constraint*/
    Topic findAllByApplicationIdAndNameAndStatus(String appId, String topicName, Character active);

    Topic findByNameAndApplicationIdAndAgentAndStatus(String topicName, String appId, Character apns, Character status);
}
