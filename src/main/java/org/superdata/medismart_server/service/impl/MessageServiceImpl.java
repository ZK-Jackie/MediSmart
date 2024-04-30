//package org.superdata.medismart.service.impl;
//
//import org.superdata.medismart.entity.Message;
//import org.superdata.medismart.entity.request.UserChatRequest;
//import org.superdata.medismart.mapper.MessageMapper;
//import org.superdata.medismart.service.MessageService;
//
//import javax.annotation.Resource;
//
//public class MessageServiceImpl implements MessageService {
//
//    @Resource
//    private MessageMapper messageMapper;
//
//
//    @Override
//    public void saveChatMessage(UserChatRequest userChatRequest, String aiMessage) {
//        Message message = new Message();
//        message.setUserId(Long.valueOf(userChatRequest.getUserId()));
//        message.setConversationId(Integer.valueOf(userChatRequest.getConversationId()));
//        message.setUserMessage(userChatRequest.getContent());
//        message.setAiMessage(aiMessage);
//        message.setIsDeleted((short) 0);
//        messageMapper.insertMessage(message);
//    }
//}
