package com.example.itDa.infra.chat.controller;

import com.example.itDa.infra.chat.ChatRoom;
import com.example.itDa.infra.chat.ChatRoomForm;
import com.example.itDa.infra.chat.repository.ChatRoomRepository;
import com.example.itDa.infra.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/test/room")
    public String rooms(Model model, @AuthenticationPrincipal UserDetailsImpl user){
        model.addAttribute("rooms",chatRoomRepository.findAllRoom());
        return "rooms";
    }

    @GetMapping("/rooms/{id}")
    public String room(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetailsImpl user){
//        ChatRoom room = chatRoomRepository.findRoomById(id);
        ChatRoom room = chatRoomRepository.findRoomById(user.getUser().getSocialId());
        model.addAttribute("room",room);
        return "room";
    }

    @GetMapping("/new")
    public String make(Model model,@AuthenticationPrincipal UserDetailsImpl user){
        ChatRoomForm form = new ChatRoomForm();
        model.addAttribute("form",form);
        return "newRoom";
    }

    @PostMapping("/room/new")
    public String makeRoom(ChatRoomForm form,@AuthenticationPrincipal UserDetailsImpl user){
        chatRoomRepository.createChatRoom(form.getName(),user);
        return "redirect:/test/room";
    }

}