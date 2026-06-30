package com.tfarcenim.core;

import com.tfarcenim.core.rei.ReiCompat;
import net.fabricmc.api.ClientModInitializer;

/**
 * TFARCENIM Core - 客户端入口类.
 *
 * <p>负责客户端侧初始化：
 * <ul>
 *   <li>REI 兼容集成（注册自定义配方条目）</li>
 *   <li>REI 开关按键绑定（客户端按键监听）</li>
 *   <li>附魔台 UI 修改（金粒提示、盲盒模式、预览隐藏）</li>
 * </ul>
 */
public class TfarcenimCoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TfarcenimCore.LOGGER.info("TFARCENIM Core Client initializing...");

        // 1. Initialize REI compatibility (including toggle keybinding)
        ReiCompat.initialize();

        TfarcenimCore.LOGGER.info("TFARCENIM Core Client initialized successfully.");
    }
}
