package com.serezk4.database.service.role;

import com.serezk4.database.model.role.Role;
import com.serezk4.database.repository.role.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
@Log4j2
public class RoleService {
    RoleRepository roleRepository;

    public Mono<Role> save(Role role) {
        return roleRepository.save(role);
    }

    public Flux<Role> findAllByChatId(Long chatId) {
        return roleRepository.findAllByChatId(chatId);
    }

    public Mono<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}
