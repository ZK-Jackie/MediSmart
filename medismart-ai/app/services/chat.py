from typing import AsyncIterator
from app.services.ai.agent import agent
from langchain_core.messages import HumanMessage, AIMessage


async def invoke_with_message_history(
        input, session_id=None, conversation_id=None
):
    if session_id is None or conversation_id is None:
        config = get_default_config()
    else:
        config = get_config(session_id, conversation_id)
    return await agent.ainvoke(
        {"messages": [("user", input)]},
        config=config
    )


async def stream_with_message_history(
        input, session_id=None, conversation_id=None
) -> AsyncIterator:
    if session_id is None or conversation_id is None:
        config = get_default_config()
    else:
        config = get_config(session_id, conversation_id)
    return agent.astream(
        {"messages": [("user", input)]},
        stream_mode="messages",
        config=config
    )


async def message_history(
        session_id=None, conversation_id=None
) -> list[dict]:
    if session_id is None or conversation_id is None:
        config = get_default_config()
    else:
        config = get_config(session_id, conversation_id)
    snapshot = await agent.aget_state(config)
    values = snapshot.values
    if not values:
        raise RuntimeError("查询失败，当前没有任何消息记录")
    existing_messages = snapshot.values["messages"]
    return trim_messages(existing_messages)


def trim_messages(messages: list[HumanMessage | AIMessage]):
    ret = []
    for m in messages:
        if isinstance(m, HumanMessage):
            ret.append({
                "type": "user",
                "content": str(m.content)
            })
        elif isinstance(m, AIMessage) and not m.tool_calls:
            ret.append({
                "type": "ai",
                "content": str(m.content)
            })
    return ret


def get_config(session_id, conversation_id):
    return {"configurable": {"thread_id": f"{session_id}-{conversation_id}"}}


def get_default_config():
    return get_config("test", "1")


__all__ = ['invoke_with_message_history', 'get_config']
