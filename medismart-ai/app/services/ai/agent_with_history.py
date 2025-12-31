from langchain_community.chat_message_histories import RedisChatMessageHistory
from langchain_core.runnables.history import RunnableWithMessageHistory
from langchain_core.runnables import ConfigurableFieldSpec
from app.core.config import Config
from app.services.ai.agent import agent_executor


# 从Redis数据库中获取和存储消息历史。
def get_message_history(session_id: str, conversation_id: str):
    key = f"{session_id}:{conversation_id}"
    return RedisChatMessageHistory(key, url=Config.REDIS_URL)


with_message_history = RunnableWithMessageHistory(
    agent_executor,
    get_message_history,
    input_messages_key="input",
    history_messages_key="history",
    history_factory_config=[
        ConfigurableFieldSpec(
            id="session_id",
            annotation=str,
            name="Session ID",
            description="Unique identifier for the session.",
            default="",
            is_shared=True,
        ),
        ConfigurableFieldSpec(
            id="conversation_id",
            annotation=str,
            name="Conversation ID",
            description="Unique identifier for the conversation.",
            default="",
            is_shared=True,
        ),
    ],
)

__all__ = ['with_message_history', "get_message_history"]
