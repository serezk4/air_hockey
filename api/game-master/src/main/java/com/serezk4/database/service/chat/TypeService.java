package com.serezk4.database.service.chat;

import com.serezk4.database.model.chat.Type;
import com.serezk4.database.repository.chat.TypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
@Log4j2
public class TypeService {
    TypeRepository typeRepository;

    public Mono<Type> save(Type type) {
        return typeRepository.save(type);
    }

    public Mono<Type> findById(Long id) {
        return typeRepository.findById(id);
    }
}
