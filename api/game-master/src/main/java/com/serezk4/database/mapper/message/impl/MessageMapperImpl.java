package com.serezk4.database.mapper.message.impl;

import com.serezk4.database.dto.member.MemberDto;
import com.serezk4.database.dto.message.MessageDto;
import com.serezk4.database.mapper.member.MemberMapper;
import com.serezk4.database.mapper.message.MessageMapper;
import com.serezk4.database.model.message.Message;
import com.serezk4.database.service.member.MemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class MessageMapperImpl implements MessageMapper {
    MemberService memberService;
    MemberMapper memberMapper;

    @Override
    public Message toEntity(MessageDto messageDto) {
        if (messageDto == null) {
            return null;
        }

        Message.MessageBuilder message = Message.builder();

        message.id(messageDto.getId());
        message.chatId(messageDto.getChatId());
        message.createdAt(messageDto.getCreatedAt());
        message.initiatorId(messageDto.getInitiator().getId());
        message.victimId(messageDto.getVictim().getId());
        message.text(messageDto.getText());
        message.type(messageDto.getType());

        return message.build();
    }

    @Override
    public Mono<MessageDto> toDto(Message message) {
        if (message == null) {
            return Mono.empty();
        }

        Mono<MemberDto> initiatorDtoMono = Mono.justOrEmpty(message.getInitiatorId())
                .flatMap(memberService::findById)
                .map(memberMapper::toDto).doOnNext(memberDto -> log.info("initiatorDtoMono: {}", memberDto))
                .switchIfEmpty(Mono.just(new MemberDto(null, null, null, null)));

        Mono<MemberDto> victimDtoMono = Mono.justOrEmpty(message.getVictimId())
                .flatMap(memberService::findById)
                .map(memberMapper::toDto).doOnNext(memberDto -> log.info("victimDtoMono: {}", memberDto))
                .switchIfEmpty(Mono.just(new MemberDto(null, null, null, null)));

        return Mono.zip(initiatorDtoMono, victimDtoMono)
                .map(tuple -> new MessageDto(
                        message.getId(),
                        message.getChatId(),
                        message.getCreatedAt(),
                        tuple.getT1(), // initiatorDto
                        tuple.getT2(), // victimDto
                        message.getText(),
                        message.getType()
                ));
    }

    @Override
    public Message partialUpdate(MessageDto messageDto, Message message) {
        if (messageDto == null) {
            return message;
        }

        if (messageDto.getId() != null) {
            message.setId(messageDto.getId());
        }
        if (messageDto.getChatId() != null) {
            message.setChatId(messageDto.getChatId());
        }
        if (messageDto.getCreatedAt() != null) {
            message.setCreatedAt(messageDto.getCreatedAt());
        }
        if (messageDto.getText() != null) {
            message.setText(messageDto.getText());
        }
        if (messageDto.getType() != null) {
            message.setType(messageDto.getType());
        }

        return message;
    }
}
