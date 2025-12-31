import json

from fastapi import APIRouter
from fastapi.responses import StreamingResponse, JSONResponse
from pydantic import BaseModel, Field, ConfigDict
from app.services import chat
from langchain_core.messages import AIMessageChunk


class ChatInput(BaseModel):
    input: str = Field(alias="content")
    user_id: str = Field(alias="userId")
    conv_id: str = Field(alias="conversationId")

    model_config = ConfigDict(populate_by_name=True, validate_by_alias=True)


api_chat = APIRouter()


@api_chat.post("/invoke")
async def post_chat(chat_input: ChatInput):
    res = (await chat.invoke_with_message_history(
        input=chat_input.input,
        session_id=chat_input.user_id,
        conversation_id=chat_input.conv_id
    ))["output"]
    return {"output": res}


@api_chat.post("/stream")
async def post_chat_stream(chat_input: ChatInput):
    try:
        async def generate():
            iters = await chat.stream_with_message_history(
                input=chat_input.input,
                session_id=chat_input.user_id,
                conversation_id=chat_input.conv_id
            )
            async for chunk in iters:
                if (
                        chunk[1].get("langgraph_node") == "agent" and
                        isinstance(chunk[0], AIMessageChunk) and
                        not chunk[0].tool_calls
                ):
                    formatter_data = json.dumps({
                        "output": chunk[0].content
                    }, ensure_ascii=False)
                    yield f"data: {formatter_data}\n\n"

        return StreamingResponse(
            generate(),
            media_type="text/event-stream",
            headers={
                "Cache-Control": "no-cache",
                "Connection": "keep-alive",
                "Content-Type": "text/event-stream",
                "X-Accel-Buffering": "no"  # 防止Nginx等代理缓冲
            }
        )
    except Exception as e:
        return JSONResponse(content={"error": str(e)}, status_code=500)


@api_chat.post("/history")
async def get_chat_history(chat_input: ChatInput):
    try:
        history = await chat.message_history(
            session_id=chat_input.user_id,
            conversation_id=chat_input.conv_id
        )
        return {"history": history}
    except Exception as e:
        return JSONResponse(content={"error": str(e)}, status_code=500)
