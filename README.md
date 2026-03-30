# 古诗翻牌游戏 - 安卓应用

一个使用 Kotlin + Jetpack Compose 开发的古诗翻牌记忆游戏。

## 功能特性

- ✅ 3x3 简单模式和 5x5 复杂模式
- ✅ 精美的木牌翻转动画效果
- ✅ 60秒倒计时挑战
- ✅ 翻转5块验证是否构成古诗
- ✅ 记录成功/失败次数和游戏时长
- ✅ 100次内诗句不重复
- ✅ 统计页面展示游戏数据

## 项目结构

```
app/src/main/java/com/example/poemflipgame/
├── MainActivity.kt              # 主Activity
├── model/
│   ├── GameModels.kt            # 游戏数据模型
│   └── WoodColors.kt            # 木牌颜色定义
├── data/
│   ├── PoemRepository.kt        # 古诗数据仓库
│   └── StatsRepository.kt       # 统计数据仓库
├── ui/
│   ├── GameScreen.kt            # 游戏主界面
│   ├── MenuScreen.kt            # 菜单界面
│   ├── StatsScreen.kt           # 统计界面
│   └── components/
│       ├── WoodCard.kt          # 木牌组件
│       └── FlipAnimation.kt     # 翻转动画
└── viewmodel/
    └── GameViewModel.kt         # 游戏ViewModel
```

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构**: MVVM
- **数据存储**: DataStore (Preferences)
- **导航**: Navigation Compose
- **动画**: Compose Animation API

## 游戏玩法

1. 选择游戏模式（简单 3x3 / 复杂 5x5）
2. 点击木牌翻转，查看下面的汉字
3. 在60秒内翻转5块木牌
4. 如果翻出的5个字能组成一句古诗，则成功！
5. 游戏会记录你的成功次数、失败次数和最快完成时间

## 已创建的文件

项目基础结构已创建，包括：
- build.gradle.kts (项目级和应用级)
- settings.gradle.kts
- AndroidManifest.xml
- colors.xml
- GameModels.kt (数据模型)
- WoodColors.kt (颜色定义)
- StatsRepository.kt (统计存储)

## 待完成文件

需要继续创建以下核心文件：
1. PoemRepository.kt - 100首古诗数据
2. GameViewModel.kt - 游戏逻辑和状态管理
3. WoodCard.kt - 木牌UI组件
4. FlipAnimation.kt - 翻转动画
5. GameScreen.kt - 游戏主界面
6. MenuScreen.kt - 菜单界面
7. StatsScreen.kt - 统计界面
8. MainActivity.kt - 主Activity

## 构建说明

1. 使用 Android Studio Hedgehog 或更新版本
2. 打开项目
3. 同步 Gradle
4. 运行应用到模拟器或真机

## 最低要求

- minSdk: 24 (Android 7.0)
- targetSdk: 34 (Android 14)
- Kotlin: 1.9.20
- Compose BOM: 2023.10.01