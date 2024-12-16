package com.serezk4.database.service.member;

import com.serezk4.database.model.member.Member;
import com.serezk4.database.repository.member.MemberRepository;
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
public class MemberService {
    MemberRepository memberRepository;

    public Mono<Member> save(Member member) {
        return memberRepository.save(member);
    }

    public Flux<Member> findAllByChatId(Long chatId, long offset, long limit) {
        return memberRepository.findAllByChatId(chatId, offset, limit);
    }

    public Mono<Member> findById(Long id) {
        if (id == null) return Mono.empty();
        return memberRepository.findById(id);
    }

    public Flux<Member> findAllByUserId(String userId, long offset, long limit) {
        return memberRepository.findAllByUserId(userId, offset, limit);
    }

    public Mono<Void> deleteByChatIdAndUserId(Long chatId, String userId) {
        return memberRepository.deleteByChatIdAndUserId(chatId, userId);
    }

    public Flux<Member> findAllByChatId(Long chatId) {
        return memberRepository.findALlByChatId(chatId);
    }

    public Flux<Member> findAllByUserId(String userId) {
        return memberRepository.findAllByUserId(userId);
    }

    public Mono<Member> findByUserIdAndChatId(String userId, Long chatId) {
        return memberRepository.findByUserIdAndChatId(userId, chatId);
    }
}
