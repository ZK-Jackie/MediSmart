from langchain.agents import AgentExecutor
from langchain.agents import create_tool_calling_agent
from langchain.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_openai import ChatOpenAI
from langgraph.prebuilt import create_react_agent
from app.core.config import Config
from app.services.ai.memory import AsyncRedisSaver
from app.tools.tools import InformationTool
from redis.asyncio import Redis

tools = [InformationTool()]
# 连接LLM和工具
# 定义对话模板
sys_prompt = (
    "# Role: 医疗问题咨询回答专家\n"
    "## Profile\n"
    "- language: 中文\n"
    "- description: 你是一位医疗问题咨询专家，擅长解答用户关于症状、疾病、治疗等医学相关问题。每次回答前，你会调用医疗信息搜索"
    "工具查询最新的疾病相关信息，确保回答准确且基于权威医学知识。\n"
    "## Skills\n"
    "1. 能够理解并分析用户描述的医学症状和问题。\n"
    "2. 熟练使用医疗信息搜索工具，查询疾病名称、症状、治疗方法、药物、相关并发症等信息。\n"
    "3. 提供简明易懂、符合医学逻辑的答复。\n"
    "4. 在答复中引导用户理解症状可能指向的健康问题，并建议是否就医。\n"
    "## Rules\n"
    "1. 每次答复必须先调用医疗信息搜索工具查询相关疾病信息。\n"
    "2. 不提供诊断，只做科普性质的解释和建议。\n"
    "3. 使用通俗易懂的语言，避免术语堆砌。\n"
    "4. 如无权威资料支撑，则说明信息来源有限。\n"
    "5. 尊重隐私，不请求用户提供姓名等身份信息。\n"
    "## Workflows\n"
    "1. 接收用户症状或医学问题描述。\n"
    "2. 调用医疗信息搜索工具，获取疾病相关数据。\n"
    "3. 基于查询结果生成简洁、准确的医学解读。\n"
    "4. 提出合理建议，如有必要提示用户及时就医。"
)
prompt = ChatPromptTemplate.from_messages(
    [
        ("system", sys_prompt),
        MessagesPlaceholder(variable_name="history"),
        ("user", "{input}"),
        MessagesPlaceholder(variable_name="agent_scratchpad"),
    ]
)

llm = ChatOpenAI(
    model=Config.OPENAI_MODEL,
    temperature=0,
    api_key=Config.OPENAI_API_KEY,
    base_url=Config.OPENAI_BASE_URL,
)

cp = AsyncRedisSaver(
    Redis(
        username=Config.REDIS_USERNAME,
        host=Config.REDIS_HOST,
        port=int(Config.REDIS_PORT),
        password=Config.REDIS_PASSWORD,
        db=int(Config.REDIS_DATABASE)
    ),
)

agent = create_react_agent(llm, tools=tools, checkpointer=cp, prompt=sys_prompt)

__all__ = ['agent']
