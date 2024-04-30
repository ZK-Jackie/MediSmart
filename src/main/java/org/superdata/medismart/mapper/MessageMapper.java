//package org.superdata.medismart.mapper;
//
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Mapper;
//import org.superdata.medismart.entity.Message;
//import org.superdata.medismart.entity.request.UserChatRequest;
//
//@Mapper
//public interface MessageMapper {
//    @Insert("INSERT INTO message (user_id, conversation_id, user_message, ai_message, is_deleted, create_time) VALUES (#{userId}, #{conversationId}, #{userMessage}, #{aiMessage}, #{isDeleted}, NOW())")
//    void insertMessage(Message message);
//}
