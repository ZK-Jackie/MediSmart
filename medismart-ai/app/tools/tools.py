from typing import Optional, Type
from pydantic import BaseModel, Field
from langchain.callbacks.manager import (
    AsyncCallbackManagerForToolRun,
    CallbackManagerForToolRun,
)
from langchain_openai import ChatOpenAI
from langchain_neo4j import GraphCypherQAChain, Neo4jGraph
from langchain.tools import BaseTool
from app.core.config import Config

_graph: Neo4jGraph = Neo4jGraph(
    url=Config.NEO4J_URI,
    username=Config.NEO4J_USERNAME,
    password=Config.NEO4J_PASSWORD
)

_chain: GraphCypherQAChain = GraphCypherQAChain.from_llm(
    ChatOpenAI(
        model=Config.OPENAI_MODEL,
        temperature=0,
        api_key=Config.OPENAI_API_KEY,
        base_url=Config.OPENAI_BASE_URL
    ),
    graph=_graph,
    verbose=True,
    allow_dangerous_requests=True
)


class InformationInput(BaseModel):
    query: str = Field(
        description="the question concerning the disease, check, department, drug, food, producer, symptom",
    )


class InformationTool(BaseTool):
    name: str = "query_medical_information"
    description: str = (
        "useful for when you need to get information about various diseases, checks, departments, drugs, foods, producers, symptoms"
    )
    args_schema: Type[BaseModel] = InformationInput

    def _run(
            self,
            query: str,
            run_manager: Optional[CallbackManagerForToolRun] = None,
    ) -> str:
        """Use the tool."""
        return _chain.invoke({"query": query})["result"]

    async def _arun(
            self,
            query: str,
            run_manager: Optional[AsyncCallbackManagerForToolRun] = None,
    ) -> str:
        """Use the tool asynchronously."""
        return (await _chain.ainvoke({"query": query}))["result"]
