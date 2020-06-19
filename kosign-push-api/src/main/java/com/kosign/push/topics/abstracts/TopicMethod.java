package com.kosign.push.topics.abstracts;

import com.kosign.push.topics.Topic;


public interface TopicMethod {
    Topic subscribe(String authKey ,String token ,String topicName);

    Topic unsubscribe(String authKey ,String token ,String topicName);

    Topic getAllTopics();

    Topic findTopicByName(String topic);
}