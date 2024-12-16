package com.serezk4.database.repository.chat;

import com.serezk4.database.model.chat.Type;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TypeRepository extends ReactiveCrudRepository<Type, Long> {
    Mono<Type> findByNameAndUserId(String name, String userId);
}
