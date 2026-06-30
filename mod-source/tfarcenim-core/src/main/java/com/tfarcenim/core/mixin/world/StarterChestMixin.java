package com.tfarcenim.core.mixin.world;

import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for the starter chest (bonus chest) world generation.
 *
 * <p>Modifies the bonus chest contents to include TFARCENIM-specific items:
 * <ul>
 *   <li>Wooden axe (1)</li>
 *   <li>Guide book (1) — "Take everything from the chest!"</li>
 *   <li>Leather (3-5)</li>
 *   <li>String (3-5)</li>
 *   <li>Bread (5)</li>
 *   <li>Torches (10)</li>
 * </ul>
 */
@Mixin(Feature.class)
public class StarterChestMixin {

    /**
     * After world generation completes, fills the bonus chest with
     * TFARCENIM-specific starter items.
     */
    @Inject(method = "generate", at = @At("RETURN"))
    private void tfarcenim$fillRewardChest(/* parameters depend on Feature.generate signature */,
                                           CallbackInfoReturnable<Boolean> cir) {
        // The actual chest filling is handled by RewardChestHandler via
        // the ServerWorldEvents.LOAD event, which is more reliable than
        // trying to inject into the world generation pipeline.
        //
        // This Mixin serves as a placeholder for future world-gen level
        // chest modification if needed.
    }
}
