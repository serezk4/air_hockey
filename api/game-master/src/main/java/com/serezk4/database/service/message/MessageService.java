package com.serezk4.database.service.message;

import com.serezk4.database.model.message.Message;
import com.serezk4.database.repository.message.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
@Log4j2
public class MessageService {
    MessageRepository messageRepository;

    public Mono<Message> save(Message message) {
        return messageRepository.save(message);
    }

    public Mono<Message> findById(UUID id) {
        return messageRepository.findById(id);
    }

    public Flux<Message> findAllByChatId(Long chatId, long offset, long limit) {
        return messageRepository.findAllByChatId(chatId, offset, limit);
    }

    public Flux<Message> findALlByChatIdAndTextContaining(Long chatId, String text) {
        return messageRepository.findALlByChatIdAndTextContaining(chatId, text);
    }
}
