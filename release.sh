#!/bin/bash
# release.sh - 古诗翻牌游戏发布脚本
# 使用方法: ./release.sh [版本号]

set -e

# 版本号
VERSION=${1:-"1.0.0"}
echo "🚀 开始构建 PoemFlipGame v${VERSION}"

# 项目目录
PROJECT_DIR="/root/PoemFlipGame"
UPLOAD_DIR="/root/.openclaw/workspace-mistw/upload"

# 检查项目目录
if [ ! -d "$PROJECT_DIR" ]; then
    echo "❌ 错误: 项目目录不存在: $PROJECT_DIR"
    exit 1
fi

cd "$PROJECT_DIR"

# 创建上传目录
mkdir -p "$UPLOAD_DIR"

echo "📦 步骤1: 清理旧构建..."
./gradlew clean

echo "🔍 步骤2: 运行代码检查..."
./gradlew lintDebug

echo "🔨 步骤3: 构建Debug APK..."
./gradlew assembleDebug

echo "🔨 步骤4: 构建Release APK..."
./gradlew assembleRelease

echo "📋 步骤5: 复制APK到上传目录..."

# 复制Debug APK
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    cp "app/build/outputs/apk/debug/app-debug.apk" \
       "$UPLOAD_DIR/PoemFlipGame-v${VERSION}-debug.apk"
    echo "✅ Debug APK: PoemFlipGame-v${VERSION}-debug.apk"
fi

# 复制Release APK
if [ -f "app/build/outputs/apk/release/app-release-unsigned.apk" ]; then
    cp "app/build/outputs/apk/release/app-release-unsigned.apk" \
       "$UPLOAD_DIR/PoemFlipGame-v${VERSION}-release-unsigned.apk"
    echo "✅ Release APK: PoemFlipGame-v${VERSION}-release-unsigned.apk"
fi

echo ""
echo "🎉 构建完成!"
echo "📁 APK文件位置: $UPLOAD_DIR"
echo ""
ls -lh "$UPLOAD_DIR"/PoemFlipGame-v${VERSION}*.apk 2>/dev/null || true

echo ""
echo "💡 提示:"
echo "   - Debug版本用于测试"
echo "   - Release版本需要签名后才能发布"
echo "   - 安卓8及以上设备可直接安装"