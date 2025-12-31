#!/usr/bin/env bash
# 基于 debian 12

# 1 换源
sed -i 's|deb.debian.org|mirrors.aliyun.com|g' /etc/apt/sources.list.d/debian.sources
python3 -m pip install -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple --upgrade pip
pip3 config set global.index-url https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
# 2 如果 uv 命令不存在，那么安装 uv
mkdir -p "$HOME/.local/bin"
export PATH="$HOME/.local/bin:$PATH"

if ! command -v uv &> /dev/null
then
    # 2.1 下载必备包
    python3 -m pip install --user pipx
    # 2.2 安装 uv
    pipx install uv
fi
# 3 安装依赖，运行程序
uv sync
uv run -m app