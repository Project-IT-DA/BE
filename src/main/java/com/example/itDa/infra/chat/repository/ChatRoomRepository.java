package com.example.itDa.infra.chat.repository;

import com.example.itDa.infra.chat.ChatRoom;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRoomMap;
    private final ObjectMapper objectMapper;

    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom(){
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findRoomById(String roomId){
        return chatRoomMap.get(roomId);
    }

    public ChatRoom createChatRoom(String chatName, UserDetailsImpl user){
        ChatRoom chatRoom = ChatRoom.create(chatName,user.getUsername());
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message){
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

//    public ChatRoom createChatRoom(String chatName,String name){
//        ChatRoom chatRoom = ChatRoom.create(chatName,name);
//        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
//        return chatRoom;
//    }
}
