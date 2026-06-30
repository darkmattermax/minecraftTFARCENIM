package com.tfarcenim.core.mixin.screen;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link EnchantmentScreen} (client-side).
 *
 * <p>Modifies the enchantment table UI:
 * <ul>
 *   <li>Displays "Cost: XP + Gold Nuggets" instead of lapis lazuli</li>
 *   <li>Shows gold nugget icon (instead of lapis icon)</li>
 *   <li>In EASY mode: hides enchantment previews (blind box)</li>
 * </ul>
 */
@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {

    /**
     * Injects into the render method to overlay custom UI text.
     * Shows the gold nugget cost hint and handles blind-box mode.
     */
    @Inject(method = "render", at = @At("RETURN"))
    private void tfarcenim$modifyEnchantmentUI(DrawContext context, int mouseX, int mouseY,
                                               float delta, CallbackInfo ci) {
        EnchantmentScreen self = (EnchantmentScreen) (Object) this;

        // Display "Cost: XP + Gold Nuggets" hint
        Text costHint = Text.translatable("tfarcenim.enchant.cost_hint")
                .formatted(Formatting.GOLD);
        context.drawText(
                self.getTextRenderer(),
                costHint,
                self.width / 2 - 60,
                self.height / 2 - 70,
                0xFFD700, // Gold color
                true
        );

        DifficultyManager dm = DifficultyManager.getInstance();

        // In EASY mode: display "???" for blind-box enchantments
        if (dm.isEasy()) {
            Text blindHint = Text.translatable("tfarcenim.enchant.blind_box")
                    .formatted(Formatting.GRAY, Formatting.ITALIC);
            context.drawText(
                    self.getTextRenderer(),
                    blindHint,
                    self.width / 2 - 40,
                    self.height / 2 - 55,
                    0xAAAAAA,
                    true
            );
        }

        // In NORMAL mode: show enhanced enchantment hint
        if (dm.isEnhancedEnchantments()) {
            Text enhancedHint = Text.translatable("tfarcenim.enchant.enhanced")
                    .formatted(Formatting.GREEN);
            context.drawText(
                    self.getTextRenderer(),
                    enhancedHint,
                    self.width / 2 - 50,
                    self.height / 2 - 55,
                    0x55FF55,
                    true
            );
        }

        // In HARD/HARDCORE mode: show reversed warning
        if (dm.isReversedEnchantments()) {
            Text reversedHint = Text.translatable("tfarcenim.enchant.reversed")
                    .formatted(Formatting.RED, Formatting.BOLD);
            context.drawText(
                    self.getTextRenderer(),
                    reversedHint,
                    self.width / 2 - 50,
                    self.height / 2 - 55,
                    0xFF5555,
                    true
            );
        }
    }
}
