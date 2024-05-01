import request from '@/utils/request'
import {Message} from "element-ui";

// Q&A方法
export function chat(conversationId, content) {
  if(content === '') {
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

// 查询聊天记录方法
export function getChatHistory(conversationId) {
  return request({
    url: `/chat/${conversationId}`,
    method: 'get',
  })
}