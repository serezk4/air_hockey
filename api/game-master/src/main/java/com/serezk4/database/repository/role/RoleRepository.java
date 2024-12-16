package com.serezk4.database.repository.role;

import com.serezk4.database.model.role.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
    Flux<Role> findAllByChatId(Long chatId);
}
