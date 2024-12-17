package com.serezk4.controller.rest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/chat")
@Validated
@Log4j2
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST}
)
public class ChatController {
//    MessageService messageService;
//    MemberService memberService;
//    RoleService roleService;
//
//    MessageMapperImpl messageMapper;
//    ChatMapperImpl chatMapper;
//    MemberMapper memberMapper;
//    ChatService chatService;
//
//    ChatSubscriptionManager chatSubscriptionManager;
//
//    @PostMapping("/create")
//    public Mono<?> createChat(
//            @RequestBody CreateChatRequest request,
//            @AuthenticationPrincipal final CustomUserDetails user
//    ) {
//        log.info(user);
//        return chatService
//                .save(Chat.builder()
//                        .title(request.getTitle())
//                        .build())
//                .doOnNext(chat -> chatSubscriptionManager.subscribe(chat.getId(), user).subscribe())
//                .flatMap(chat -> roleService.save(Role.builder()
//                                .name("admin")
//                                .chatId(chat.getId())
//                                .canDelete(true).canInvite(true).canManage(true).canRead(true).canWrite(true)
//                                .build())
//                        .flatMap(role -> memberService.save(
//                                Member.builder()
//                                        .chatId(chat.getId())
//                                        .userId(user.getSub())
//                                        .preferredName(user.getPreferredUsername())
//                                        .roleId(role.getId())
//                                        .build()
//                        )))
//                .doOnNext(member -> messageService
//                        .save(Message.builder()
//                                .text("New chat created")
//                                .type(Message.Type.CHAT_CREATE)
//                                .chatId(member.getChatId())
//                                .initiatorId(member.getId())
//                                .build())
//                        .flatMap(messageMapper::toDto)
//                        .doOnNext(message ->
//                                chatSubscriptionManager.subscribe(member.getChatId(), user).subscribe())
//                        .doOnNext(message ->
//                                chatSubscriptionManager.notifySubscribers(member.getChatId(), message.toString()))
//                        .subscribe())
//                .map(member -> ApiResponse.ok(memberMapper.toDto(member)));
//    }
//
//    @PostMapping("/{chatId}/add")
//    public Flux<ResponseEntity<ApiResponse.Body<ChatMemberResponse>>> addMember(
//            @PathVariable @Min(value = 0, message = "chat_id.min:0") final Long chatId,
//            @RequestParam @Min(value = 0, message = "user_id.min:0") final String userId,
//            @AuthenticationPrincipal final CustomUserDetails user
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new NotFoundException("member.not_found")))
//                .flatMap(_member -> checkRoleManagePermissions(_member.getRoleId()))
//                .thenMany(memberService.findByUserIdAndChatId(userId, chatId))
//                .switchIfEmpty(memberService.save(
//                        Member.builder()
//                                .chatId(chatId)
//                                .userId(userId)
//                                .build()
//                ))
//                .doOnNext(member -> messageService
//                        .save(Message.builder()
//                                .text("New user in chat")
//                                .type(Message.Type.MEMBER_ADD)
//                                .chatId(chatId)
//                                .victimId(member.getId())
//                                .build())
//                        .map(messageMapper::toDto)
//                        .doOnNext(message -> chatSubscriptionManager.subscribe(chatId, user).subscribe())
//                        .doOnNext(message -> chatSubscriptionManager.notifySubscribers(chatId, message.toString()))
//                        .subscribe())
//                .map(memberMapper::toDto)
//                .map(_memberDto -> ApiResponse.ok(new ChatMemberResponse(_memberDto)));
//    }
//
//    @PostMapping("/{chatId}/remove")
//    public Flux<ResponseEntity<ApiResponse.Body<ChatMemberRemoveResponse>>> removeMember(
//            @PathVariable @Min(value = 0, message = "chat_id.min:0") final Long chatId,
//            @RequestParam @Min(value = 0, message = "user_id.min:0") final String userId,
//            @AuthenticationPrincipal final CustomUserDetails user
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new NotFoundException("member.not_found")))
//                .flatMap(_member -> checkRoleManagePermissions(_member.getRoleId()))
//                .thenMany(memberService.findByUserIdAndChatId(userId, chatId))
//                .doOnNext(member -> messageService
//                        .save(Message.builder()
//                                .text("User removed")
//                                .type(Message.Type.MEMBER_ADD)
//                                .chatId(chatId)
//                                .victimId(member.getId())
//                                .build())
//                        .doOnNext((message) ->
//                                chatSubscriptionManager.notifySubscribers(message.getChatId(), message.toString()))
//                        .subscribe())
//                .switchIfEmpty(Mono.error(new NotFoundException("member.not_found")))
//                .flatMap(member -> memberService.deleteByChatIdAndUserId(chatId, userId))
//                .map(_memberDto -> ApiResponse.ok(new ChatMemberRemoveResponse(true)));
//    }
//
//    @PostMapping("{chatId}/enableNotifications")
//    public Mono<?> enableNotifications(
//            final @AuthenticationPrincipal CustomUserDetails user,
//            final @PathVariable @Min(value = 0, message = "chat_id.min:0") Long chatId
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new InvalidCredentialsException("member.not.found")))
//                .map(member -> member.setEnableNotifications(true))
//                .flatMap(memberService::save).map(Member::isEnableNotifications)
//                .map(NotificationsResponse::new)
//                .map(ApiResponse::ok);
//    }
//
//    @PostMapping("{chatId}/disableNotifications")
//    public Mono<?> disableNotifications(
//            final @AuthenticationPrincipal CustomUserDetails user,
//            final @PathVariable @Min(value = 0, message = "chat_id.min:0") Long chatId
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new InvalidCredentialsException("member.not.found")))
//                .map(member -> member.setEnableNotifications(false))
//                .flatMap(memberService::save).map(Member::isEnableNotifications)
//                .map(NotificationsResponse::new)
//                .map(ApiResponse::ok);
//    }
//
//    @GetMapping("/list")
//    public Mono<ResponseEntity<ApiResponse.Body<ChatListResponse>>> listChats(
//            @AuthenticationPrincipal final CustomUserDetails user,
//            @RequestParam @Min(value = 0, message = "offset.min:0") final long offset,
//            @RequestParam @Min(value = 0, message = "limit.min:0") final long limit
//    ) {
//        return memberService.findAllByUserId(user.getSub(), offset, limit)
//                .flatMap(_member -> checkRoleReadPermissions(_member.getRoleId()))
//                .thenMany(memberService.findAllByUserId(user.getSub()))
//                .map(Member::getChatId).flatMap(chatService::findById).flatMap(chatMapper::toDto)
//                .collectList().map(_chats -> new ChatListResponse(_chats, _chats.size()))
//                .map(ApiResponse::ok);
//    }
//
//    @GetMapping("/{chatId}/me")
//    public Mono<?> getMe(
//            @PathVariable @Min(value = 0, message = "chat_id.min:0") final Long chatId,
//            @AuthenticationPrincipal final CustomUserDetails user
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new NotFoundException("member.not_found")))
//                .flatMap(_member -> checkRoleReadPermissions(_member.getRoleId()).thenReturn(_member))
//                .map(memberMapper::toDto).map(_memberDto -> ApiResponse.ok(new ChatMemberResponse(_memberDto)));
//    }
//
//    @GetMapping("/{chatId}/members")
//    public Mono<ResponseEntity<ApiResponse.Body<MemberListResponse>>> getMembers(
//            @PathVariable @Min(value = 0, message = "chat_id.min:0") final Long chatId,
//            @RequestParam @Min(value = 0, message = "offset.min:0") final long offset,
//            @RequestParam @Min(value = 0, message = "limit.min:0") final long limit,
//            @AuthenticationPrincipal final CustomUserDetails user
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new NotFoundException("member.not_found")))
//                .flatMap(_member -> checkRoleReadPermissions(_member.getRoleId()))
//                .thenMany(memberService.findAllByChatId(chatId, offset, limit))
//                .map(memberMapper::toDto)
//                .collectList()
//                .map(_members -> ApiResponse.ok(new MemberListResponse(_members, _members.size())));
//    }
//
//    @GetMapping("/{chatId}")
//    public Mono<ResponseEntity<ApiResponse.Body<ChatDto>>> getChat(
//            @PathVariable @Min(value = 0, message = "chat_id.min:0") final Long chatId,
//            @AuthenticationPrincipal final CustomUserDetails user
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new NotFoundException("member.not_found")))
//                .flatMap(_member -> checkRoleReadPermissions(_member.getRoleId()))
//                .then(chatService.findById(chatId))
//                .flatMap(chatMapper::toDto)
//                .map(ApiResponse::ok);
//    }
//
//    @GetMapping("/{chatId}/messages")
//    public Mono<ResponseEntity<ApiResponse.Body<ChatMessageResponse>>> getMessages(
//            @PathVariable @Min(value = 0, message = "chat_id.min:0") final Long chatId,
//            @RequestParam @Min(value = 0, message = "offset.min:0") final long offset,
//            @RequestParam @Min(value = 0, message = "limit.min:0") final long limit,
//            @AuthenticationPrincipal final CustomUserDetails user
//    ) {
//        return memberService.findByUserIdAndChatId(user.getSub(), chatId)
//                .switchIfEmpty(Mono.error(new NotFoundException("member.not_found")))
//                .flatMap(_member -> checkRoleReadPermissions(_member.getRoleId()))
//                .thenMany(messageService.findAllByChatId(chatId, offset, limit))
//                .flatMap(messageMapper::toDto)
//                .collectList()
//                .map(_messages -> ApiResponse.ok(new ChatMessageResponse(_messages, _messages.size())));
//    }
//
//    private Mono<Role> checkRoleReadPermissions(final Long roleId) {
//        return roleService.findById(roleId)
//                .switchIfEmpty(Mono.error(new NotFoundException("role.not_found")))
//                .flatMap(role -> {
//                    if (Boolean.FALSE.equals(role.getCanRead()))
//                        return Mono.error(new ForbiddenException("role.cannot_read"));
//                    return Mono.just(role);
//                });
//    }
//
//    private Mono<Role> checkRoleWritePermissions(final Long roleId) {
//        return roleService.findById(roleId)
//                .switchIfEmpty(Mono.error(new NotFoundException("role.not_found")))
//                .flatMap(role -> {
//                    if (Boolean.FALSE.equals(role.getCanWrite()))
//                        return Mono.error(new ForbiddenException("role.cannot_write"));
//                    return Mono.just(role);
//                });
//    }
//
//    private Mono<Role> checkRoleManagePermissions(final Long roleId) {
//        return roleService.findById(roleId)
//                .switchIfEmpty(Mono.error(new NotFoundException("role.not_found")))
//                .flatMap(role -> {
//                    if (Boolean.FALSE.equals(role.getCanManage()))
//                        return Mono.error(new ForbiddenException("role.cannot_manage"));
//                    return Mono.just(role);
//                });
//    }
}
