# TFARCENIM 整合包

> **TFARCENIM = MINECRAFT 倒写**
> MC 1.21.10 / Fabric / 全反转设计 / 4档难度

## 这是什么？

TFARCENIM 是一个"全反转"主题的 Minecraft 整合包。你越熟悉原版MC，在这里就越惨：
- 所有配方全改了（1原木只出2木板，火把煤在上棍在下...）
- 所有掉落全改了（牛掉羽毛、史莱姆掉末影珍珠...）
- 所有附魔全反转（耐久=不耐久、击退=拉近、保护=增伤...）
- 4档难度：简单(坑你笑)/普通(末地门少框不可通关)/困难(极致坐牢)/极限(10HP回血废)

## 快速开始

### 方式一：一键安装（推荐）

1. 先编译自写Mod：双击 `scripts\build-mods.bat`（需要联网，会自动下载JDK）
2. 再一键安装：双击 `scripts\install.bat`
3. 用 PCL2 启动 MC 1.21.10 + Fabric 0.17.2
4. 创建新存档，把 `datapacks\TFARCENIM-datapack` 复制到存档的 `datapacks\` 目录
5. 游戏内输入 `/reload`

### 方式二：手动安装

1. 编译自写Mod（见下方）
2. 把 `mods\` 所有jar复制到 `.minecraft\mods\`
3. 把 `shaderpacks\` 所有zip复制到 `.minecraft\shaderpacks\`
4. 把 `config\` 所有文件复制到 `.minecraft\config\`
5. 创建存档后，把 `datapacks\TFARCENIM-datapack` 复制到 `.minecraft\saves\存档名\datapacks\`
6. 用 PCL2 启动 MC 1.21.10 + Fabric 0.17.2

## 编译自写Mod

自写Mod需要JDK 21编译：

```cmd
:: 方式一：双击 scripts\build-mods.bat（自动下载JDK+编译）

:: 方式二：手动
cd mod-source\tfarcenim-core && gradlew build
cd ..\tfarcenim-portal && gradlew build
cd ..\tfarcenim-recipebook && gradlew build
cd ..\tfarcenim-balance && gradlew build
```

编译后的jar在各自 `build\libs\` 目录，复制到 `mods\`。

## 目录结构

```
TFARCENIM-RELEASE/
├── README.md                 ← 本文件
├── mods/                     ← 社区Mod（15个jar，已下载好）
├── shaderpacks/              ← 光影（3个zip，已下载好）
├── datapacks/                ← 数据包
│   └── TFARCENIM-datapack/   ← 590个JSON（配方345+战利品124+进度112+函数4+标签4）
├── config/                   ← 配置文件（Sodium/Iris/REI/JEI）
├── mod-source/               ← 4个自写Mod源码（需编译）
│   ├── tfarcenim-core/       ← 核心：附魔反转/亡灵不烧/星辰之力/奖励箱/REI
│   ├── tfarcenim-portal/     ← 普通难度末地门少框
│   ├── tfarcenim-recipebook/ ← 配方书6阶段解锁
│   └── tfarcenim-balance/    ← 数值平衡
└── scripts/                  ← 脚本
    ├── build-mods.bat        ← 一键编译自写Mod
    └── install.bat           ← 一键安装到.minecraft
```

## 4个自写Mod说明

| Mod | 修改项数 | 核心功能 |
|-----|---------|---------|
| tfarcenim-core | 23项 | 附魔分难度反转/亡灵不烧/原木右键/星辰之力/奖励箱/REI兼容/附魔台盲盒 |
| tfarcenim-portal | 1项 | 普通难度末地门少一个框 |
| tfarcenim-recipebook | 1项 | 配方书6阶段渐进解锁 |
| tfarcenim-balance | 18项 | 全物品数值平衡（复杂配方=更强属性） |

## 难度说明

| 难度 | 配方 | 附魔 | 掉落 | 通关 |
|------|------|------|------|------|
| 简单 | 复杂但打怪掉成品 | 原版效果 | 额外掉成品 | ✅ 可速通 |
| 普通 | 标准 | 增益放大×1.5 | 标准 | ❌ 末地门少框 |
| 困难 | 标准 | 全反转 | 标准 | ✅ 理论可通关 |
| 极限 | 标准 | 全反转 | 标准 | ✅ 10HP+回血废 |

## 技术信息

- **MC版本**: 1.21.10
- **加载器**: Fabric Loader 0.17.2
- **Java**: 21（强制）
- **数据包格式**: 88（1.21.10新格式 min_format/max_format）
- **社区Mod**: 15个（Sodium/Iris/Lithium等优化mod + JEI/REI配方浏览器）
- **光影**: 3套（Photon/ComplementaryUnbound/ComplementaryReimagined）

## 已知限制

1. 自写Mod的Mixin映射基于1.21.4，1.21.10可能需要微调方法名（gradle build报错时按报错信息调整）
2. ModernFix和Cyanide Shaders未找到1.21.10 Fabric版（不影响运行）
3. guide_book.png为占位纹理（需美工制作16×16像素纹理）

## License

MIT

---

*TFARCENIM = MINECRAFT 倒写 · 你以为你懂Minecraft？在这个世界里，你什么都不懂。*
