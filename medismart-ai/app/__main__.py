import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.api.v1.chat import api_chat

app = FastAPI()

app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
)

app.include_router(api_chat)

if __name__ == "__main__":
    uvicorn.run("app.__main__:app", host="0.0.0.0", port=8080, reload=True)
    