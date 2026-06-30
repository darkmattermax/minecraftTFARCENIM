package com.tfarcenim.balance;

import com.tfarcenim.balance.config.BalanceConfig;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TFARCENIM Balance - 数值平衡模块入口类。
 *
 * <p>负责以下 18 项数值调整（实现于 {@code com.tfarcenim.balance.mixin}）：
 * <ul>
 *   <li>6 种材质工具耐久 / 挖掘速度 / 攻击伤害（木 / 金 / 石 / 铁 / 钻石 / 下界合金）</li>
 *   <li>5 种材质防具防御 / 耐久（皮革 / 铁 / 金 / 钻石 / 下界合金）</li>
 *   <li>弓耐久 450 / 弩耐久 380</li>
 *   <li>盾牌耐久 360 / 钓鱼竿耐久 75 / 剪刀耐久 260</li>
 *   <li>海龟壳防御 +1 / 耐久 300</li>
 *   <li>附魔金苹果饥饿 8 / 饱和 16</li>
 *   <li>下界合金星辰之力属性（剑攻速 1.9）</li>
 * </ul>
 *
 * <p>所有数值集中由 {@link BalanceConfig} 管理，不硬编码；Mixin 仅做分发。
 * 运行时可通过 {@code BalanceConfig.ENABLED} 一键关闭全部覆盖。
 */
public class TfarcenimBalance implements ModInitializer {

    /** 模块唯一标识。 */
    public static final String MOD_ID = "tfarcenim-balance";

    /** 模块日志器。 */
    public static final Logger LOGGER = LogManager.getLogger("TFARCENIM-Balance");

    @Override
    public void onInitialize() {
        LOGGER.info("TFARCENIM Balance initializing...");
        loadBalanceConfig();
        if (BalanceConfig.ENABLED) {
            LOGGER.info("TFARCENIM Balance active: tool/armor/ranged/food overrides loaded.");
        } else {
            LOGGER.warn("TFARCENIM Balance DISABLED by config — all values remain vanilla.");
        }
    }

    /**
     * Loads the balance configuration. Values are compiled-in constants in
     * {@link BalanceConfig}; this hook is reserved for a future JSON override
     * (e.g. {@code config/tfarcenim-balance.json}) and currently just validates
     * the enabled flag.
     */
    private void loadBalanceConfig() {
        // Reserved: parse an external config file here if/when added.
        // For now the compiled defaults in BalanceConfig are authoritative.
        LOGGER.debug("BalanceConfig loaded: ENABLED={}", BalanceConfig.ENABLED);
    }
}
