package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TopicDomainService {
    private List<TopicModel> topics = new ArrayList<>();

    public TopicModel createTopic(TopicModel topic) {
        topic.setId(topics.size() + 1L);
        topics.add(topic);
        return topic;
    }

    public Optional<TopicModel> getTopicById(Long id) {
        return topics.stream().filter(topic -> topic.getId().equals(id)).findFirst();
    }

    public Optional<TopicModel> getTopicByName(String name) {
        return topics.stream().filter(topic -> topic.getName().equals(name)).findFirst();
    }

    public List<TopicModel> getAllTopics() {
        return topics;
    }

    public Optional<TopicModel> updateTopic(Long id, TopicModel updatedTopic) {
        return topics.stream().filter(topic -> topic.getId().equals(id)).findFirst().map(topic -> {
            int index = topics.indexOf(topic);
            updatedTopic.setId(id);
            topics.set(index, updatedTopic);
            return updatedTopic;
        });
    }

    public void deleteTopic(Long id) {
        topics.removeIf(topic -> topic.getId().equals(id));
    }
}
