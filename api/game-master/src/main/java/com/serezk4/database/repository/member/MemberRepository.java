package com.serezk4.database.repository.member;

import com.serezk4.database.model.member.Member;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {
    @Query("SELECT * FROM members WHERE chat_id = :chatId LIMIT :limit OFFSET :offset")
    Flux<Member> findAllByChatId(Long chatId, long offset, long limit);

    /* WARN: NOT OPTIMAL */
    /* USE WITH PAGEABLE */
    Flux<Member> findALlByChatId(Long chatId);

    @Query("""
            SELECT * FROM (
                SELECT m.*, dm.created_at AS last_message_date
                FROM members m
                LEFT JOIN chats c ON m.chat_id = c.id
                LEFT JOIN default_messages dm ON c.last_message_id = dm.id
                WHERE m.user_id = :userId
                ORDER BY dm.created_at DESC NULLS LAST
            ) AS sorted_members
            OFFSET :offset 
            LIMIT :limit
            """)
    Flux<Member> findAllByUserId(String userId, long offset, long limit);

    /* WARN: NOT OPTIMAL */
    /* USE WITH PAGEABLE */
    Flux<Member> findAllByUserId(String userId);

    @Modifying
    @Query("DELETE from members WHERE chat_id = :chatId AND user_id = :userId")
    Mono<Void> deleteByChatIdAndUserId(Long chatId, String userId);

    Mono<Member> findByUserIdAndChatId(String userId, Long chatId);
}
