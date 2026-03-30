#!/bin/bash
# GitHub推送脚本 - 用户: ten2net
# 仓库: PoemFlipGame

set -e

echo "🚀 准备推送 PoemFlipGame 到 GitHub (ten2net)"
echo ""

# 项目目录
PROJECT_DIR="/root/PoemFlipGame"

# 检查项目目录
if [ ! -d "$PROJECT_DIR" ]; then
    echo "❌ 错误: 项目目录不存在: $PROJECT_DIR"
    exit 1
fi

cd "$PROJECT_DIR"

echo "📦 步骤1: 初始化Git仓库..."
if [ ! -d ".git" ]; then
    git init
    echo "✅ Git仓库已初始化"
else
    echo "✅ Git仓库已存在"
fi

echo ""
echo "📦 步骤2: 配置Git用户信息..."
echo "   请确保已配置Git用户名和邮箱"
echo "   git config --global user.name 'Your Name'"
echo "   git config --global user.email 'your.email@example.com'"

echo ""
echo "📦 步骤3: 添加文件到Git..."
git add .

echo ""
echo "📦 步骤4: 提交代码..."
git commit -m "Initial commit: PoemFlipGame v1.0.0

Features:
- 古诗翻牌游戏
- 3x3简单模式 / 5x5困难模式
- 木牌翻转动画效果
- 60秒倒计时
- 中国风UI设计
- 统计功能（胜率、平均用时）
- 兼容安卓8（API 26）及以上"

echo ""
echo "📦 步骤5: 添加远程仓库..."
git remote add origin https://github.com/ten2net/PoemFlipGame.git 2>/dev/null || echo "✅ 远程仓库已存在"

echo ""
echo "📦 步骤6: 推送到GitHub..."
echo "   注意: 如果提示输入密码，请使用GitHub Personal Access Token"
echo "   创建Token: https://github.com/settings/tokens"
git push -u origin main || git push -u origin master

echo ""
echo "🎉 推送完成!"
echo ""
echo "📋 下一步操作:"
echo "   1. 打开 https://github.com/ten2net/PoemFlipGame"
echo "   2. 点击 Actions 标签"
echo "   3. 等待构建完成 (约2-3分钟)"
echo "   4. 在 Artifacts 中下载APK"
echo ""
echo "💡 提示:"
echo "   - Debug APK: 用于测试"
echo "   - Release APK: 需要签名后发布"
