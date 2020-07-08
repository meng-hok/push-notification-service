package com.kosign.push.topics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity,String> {
    List<TopicEntity> findAllByApplicationIdAndStatus(String appId, Character active);

    List<TopicEntity>  findByNameAndApplicationId(String topicName, String appId);

    /* unique constraint*/
    TopicEntity findAllByApplicationIdAndNameAndStatus(String appId, String topicName, Character active);

    TopicEntity findByNameAndApplicationIdAndAgentAndStatus(String topicName, String appId, Character apns, Character status);
}
