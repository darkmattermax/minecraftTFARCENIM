package com.tfarcenim.portal;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TFARCENIM Portal - 传送门模块入口类。
 *
 * <p>负责以下功能：
 * <ul>
 *   <li>普通难度下末地传送门生成时删除一个框架方块</li>
 * </ul>
 *
 * <p>具体 Mixin 逻辑将在 T04 任务中实现。
 */
public class TfarcenimPortal implements ModInitializer {

    /** 模块日志器。 */
    public static final Logger LOGGER = LogManager.getLogger("TFARCENIM-Portal");

    @Override
    public void onInitialize() {
        LOGGER.info("TFARCENIM Portal initializing...");

        // EndPortalFeatureMixin handles removing one End Portal frame block
        // in NORMAL difficulty. No additional initialization needed here.
        LOGGER.info("TFARCENIM Portal initialized successfully.");
    }
}
