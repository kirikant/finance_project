package com.telegram.repo;

import com.telegram.entity.ChatEntityMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRepoMongo extends MongoRepository<ChatEntityMongo,Long> {
    Optional<ChatEntityMongo> findChatEntityMongoByChatId(String chatId);
}
