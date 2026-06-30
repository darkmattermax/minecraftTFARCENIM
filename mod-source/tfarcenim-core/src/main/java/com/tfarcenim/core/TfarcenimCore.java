package com.tfarcenim.core;

import com.tfarcenim.core.event.LogRightClickHandler;
import com.tfarcenim.core.event.RewardChestHandler;
import com.tfarcenim.core.registry.TfarcenimRegistry;
import com.tfarcenim.core.util.DifficultyManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TFARCENIM Core - 主入口类.
 *
 * <p>负责注册以下核心机制：
 * <ul>
 *   <li>附魔反转/增益/原版三态切换（通过 Mixin 注入各附魔类）</li>
 *   <li>亡灵实体白天不燃烧</li>
 *   <li>星辰之力自定义附魔词条</li>
 *   <li>奖励箱系统 + 指南书物品</li>
 *   <li>原木右键劈木板事件</li>
 *   <li>REI 兼容集成与开关按键</li>
 * </ul>
 */
public class TfarcenimCore implements ModInitializer {

    /** 统一日志前缀，用于全 mod 链路排查。 */
    public static final Logger LOGGER = LogManager.getLogger("TFARCENIM-Core");

    @Override
    public void onInitialize() {
        LOGGER.info("TFARCENIM Core initializing...");

        // 1. Register all custom content (enchantments, items)
        TfarcenimRegistry.registerAll();

        // 2. Register event handlers
        LogRightClickHandler.register();
        RewardChestHandler.register();

        // 3. Set up difficulty tracking — update on every server tick
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getWorlds().forEach(world -> {
                DifficultyManager.getInstance().updateDifficulty(world);
            });
        });

        LOGGER.info("TFARCENIM Core initialized successfully.");
    }
}
