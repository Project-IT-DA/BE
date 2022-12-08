package com.example.itDa.infra.chat.repository;

import com.example.itDa.infra.chat.ChatRoom;
import com.example.itDa.infra.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom(){
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findRoomById(String id){
        return chatRoomMap.get(id);
    }

    public ChatRoom createChatRoom(String chatName, UserDetailsImpl user){
        ChatRoom chatRoom = ChatRoom.create(chatName,user.getUsername());
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

//    public ChatRoom createChatRoom(String chatName,String name){
//        ChatRoom chatRoom = ChatRoom.create(chatName,name);
//        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
//        return chatRoom;
//    }
}
