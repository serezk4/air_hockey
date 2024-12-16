package com.serezk4.database.repository.message;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.serezk4.database.model.message.Message;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, UUID> {
    @Query("""
        SELECT * FROM (
            SELECT * FROM default_messages 
            WHERE chat_id = :chatId 
            ORDER BY created_at DESC
        ) AS sorted_messages
        OFFSET :offset 
        LIMIT :limit
    """)
    Flux<Message> findAllByChatId(Long chatId, long offset, long limit);

    @Query("SELECT * FROM default_messages WHERE chat_id = :chatId AND text LIKE %:text%")
    Flux<Message> findALlByChatIdAndTextContaining(Long chatId, String text);
}
