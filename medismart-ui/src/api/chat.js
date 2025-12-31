import request from '@/utils/request'
import {Message} from "element-ui";
import {getToken} from "@/utils/auth";
import { EventSourcePolyfill } from 'event-source-polyfill';


// Q&A方法
export function chat(conversationId, content) {
  if (content === '') {
    Message({
      message: '请输入内容',
      type: 'warning'
    })
    return Promise.reject('请输入要发送的内容')
  }
  const data = {
    content,
    conversationId
  }
  return request({
    url: '/chat',
    method: 'post',
    data: data
  })
}

export function chatStream(conversationId, content) {
  if (content === '') {
    Message({
      message: '请输入内容',
      type: 'warning'
    })
    return Promise.reject('请输入要发送的内容')
  }
  const data = {
    content,
    conversationId
  }
  return request({
    url: '/stream',
    method: 'post',
    data: data
  })
}

export function connectSse(sessionId) {
  return new EventSource(
    `${process.env.VUE_APP_BASE_API}/connect/${sessionId}`,
  );
}

// 查询聊天记录方法
export function getChatHistory(conversationId) {
  return request({
    url: `/chat/${conversationId}`,
    method: 'get',
  })
}
// 获取会话列表
export function getConversationList() {
  return request({
    url: '/conversations',
    method: 'get',
  })
}

// 删除会话
export function deleteConversation(conversationId) {
  return request({
    url: `/conversation/${conversationId}`,
    method: 'delete',
  })
}

// 更新会话标题
export function updateConversationTitle(conversationId, title) {
  return request({
    url: `/conversation/${conversationId}/title`,
    method: 'put',
    params: { title }
  })
}
