import os
from dotenv import load_dotenv
# 加载.env文件
load_dotenv()

class Config:
    # Neo4j数据库连接信息
    NEO4J_URI = os.getenv("NEO4J_URI")
    NEO4J_USERNAME = os.getenv("NEO4J_USERNAME")
    NEO4J_PASSWORD = os.getenv("NEO4J_PASSWORD")
    # Redis数据库连接信息
    REDIS_HOST = os.getenv("REDIS_HOST")
    REDIS_PORT = os.getenv("REDIS_PORT")
    REDIS_USERNAME = os.getenv("REDIS_USERNAME")
    REDIS_PASSWORD = os.getenv("REDIS_PASSWORD")
    REDIS_DATABASE = os.getenv("REDIS_DATABASE")
    # OpenAI API Key
    OPENAI_MODEL = os.getenv("OPENAI_MODEL")
    OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
    OPENAI_BASE_URL = os.getenv("OPENAI_BASE_URL")
