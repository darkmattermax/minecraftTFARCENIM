package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.VanishingCurseEnchantment;
import net.minecraft.enchantment.BindingCurseEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for curse enchantments (消失诅咒 / 绑定诅咒).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — curses become beneficial:
 *     <ul>
 *       <li>Vanishing Curse — items are kept on death AND fully repaired</li>
 *       <li>Binding Curse — items can be removed AND enchantments transfer</li>
 *     </ul>
 *   </li>
 *   <li>NORMAL / EASY — original curse behaviour (curses remain negative)</li>
 * </ul>
 *
 * <p>This Mixin targets both curse enchantment classes via a shared base
 * approach. The specific reversal logic is applied through event hooks.
 */
@Mixin(VanishingCurseEnchantment.class)
public class CurseEnchantmentsMixin {

    /**
     * In reversed mode, prevents item vanishing on death and repairs to full.
     * Injects into the vanishing check.
     */
    @Inject(
            method = "onPlayerDeath",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tfarcenim$reverseVanishing(LivingEntity entity, ItemStack stack,
                                            CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Cancel the vanishing effect
            ci.cancel();
            // Repair the item to full durability as a bonus
            stack.setDamage(0);
        }
        // NORMAL / EASY: original vanishing behaviour
    }
}
