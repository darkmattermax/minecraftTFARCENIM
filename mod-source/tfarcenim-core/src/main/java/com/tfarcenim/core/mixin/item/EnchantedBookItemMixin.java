package com.tfarcenim.core.mixin.item;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Mixin for {@link EnchantedBookItem}.
 *
 * <p>In EASY mode, hides the enchantment book name — displays "???" instead
 * of the actual enchantment. This makes enchantment books a gamble in easy mode.
 */
@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {

    /**
     * Replaces the tooltip name of enchanted books with "???" in EASY mode.
     */
    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$hideEnchantmentName(ItemStack stack,
                                               CallbackInfoReturnable<Text> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();

        if (dm.isEasy()) {
            // Replace the actual name with "???"
            Text hiddenName = Text.literal("???")
                    .formatted(Formatting.DARK_PURPLE, Formatting.ITALIC);
            cir.setReturnValue(hiddenName);
        }
        // Other modes: show the real enchantment name
    }

    /**
     * In EASY mode, also hides the enchantment details in the tooltip.
     */
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void tfarcenim$hideEnchantmentTooltip(ItemStack stack,
                                                  CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();

        if (dm.isEasy()) {
            // Cancel the tooltip so enchantment details are not shown
            ci.cancel();
        }
    }
}
