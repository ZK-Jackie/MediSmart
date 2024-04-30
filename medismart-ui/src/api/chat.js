import request from '@/utils/request'

// Q&A方法
export function chat(conversationId, content) {
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