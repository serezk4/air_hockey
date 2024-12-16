package com.serezk4.database.repository.chat;

import com.serezk4.database.model.chat.Chat;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {
    @Query("""
    SELECT * FROM (
        SELECT c.*, m.created_at AS last_message_date 
        FROM chats c 
        LEFT JOIN default_messages m ON c.last_message_id = m.id
        ORDER BY m.created_at DESC NULLS LAST
    ) AS sorted_chats
    OFFSET :offset 
    LIMIT :limit
    """)
    Flux<Chat> findAllWithPageable(int offset, int limit);
}
