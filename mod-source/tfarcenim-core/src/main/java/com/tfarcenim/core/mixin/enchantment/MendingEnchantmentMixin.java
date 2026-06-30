package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link MendingEnchantment} (经验修补).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — XP orbs accelerate durability loss instead of repairing</li>
 *   <li>NORMAL — improves repair efficiency by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(MendingEnchantment.class)
public class MendingEnchantmentMixin {

    /**
     * Injects into the repair method. In reversed mode, instead of repairing
     * the item, it damages the item by the same amount.
     */
    @Inject(
            method = "repair",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tfarcenim$reverseMending(ItemStack stack, int amount,
                                          CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Cancel the repair
            ci.cancel();
            // Instead, damage the item by the same amount
            stack.damage(amount, null, null);
        }
        // NORMAL: original repair logic (enhanced via multiplier handled elsewhere)
        // EASY: original behaviour
    }

    /**
     * Alternative injection: modifies the XP-based repair amount.
     * In enhanced mode, doubles the repair per XP point.
     */
    @Inject(
            method = "canRepair",
            at = @At("RETURN"),
            cancellable = true
    )
    private void tfarcenim$enhanceMending(ItemStack stack, int xpAmount,
                                          CallbackInfoReturnable<Integer> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× repair efficiency per XP
            cir.setReturnValue((int) (cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER));
        }
    }
}
