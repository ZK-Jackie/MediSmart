package org.superdata.medismart.mapper.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.superdata.medismart.entity.ChatConversation;

/**
 * 聊天会话Mapper接口
 */
@Mapper
public interface ChatConversationMapper extends BaseMapper<ChatConversation> {
}
