package com.tfarcenim.balance.mixin;

import com.tfarcenim.balance.config.BalanceConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Boosts the <b>enchanted</b> golden apple to nutrition 8 / saturation 16
 * (vanilla 4 / 9.6) by adding the residual delta on top of the vanilla
 * {@code FOOD} component at {@code finishUsing} time.
 *
 * <p>The delta is applied directly to the player's {@link HungerManager} so the
 * result is exact regardless of how the {@code FOOD} component encodes its
 * saturation modifier. Only the enchanted variant is affected; the ordinary
 * golden apple keeps its vanilla values.
 *
 * <p><b>Implementation note:</b> the injection targets {@link Item#finishUsing}
 * (guarded by an enchanted-golden-apple identity check) so the override fires
 * whether or not the enchanted golden apple is registered as a
 * {@code GoldenAppleItem} instance in the current mappings. The class name
 * follows the architecture design ({@code GoldenAppleMixin}).
 */
@Mixin(Item.class)
public class GoldenAppleMixin {

    /**
     * After the vanilla eat logic has applied the base food values, top the
     * player up with the TFARCENIM delta for enchanted golden apples.
     *
     * @param stack the item stack being consumed (still carries its item type).
     * @param world the world the consumption occurred in.
     * @param user  the entity that ate the item.
     * @param cir   the return-value callback carrying the remainder stack.
     */
    @Inject(method = "finishUsing", at = @At("RETURN"))
    private void tfarcenim$enchantedGoldenAppleBonus(ItemStack stack, World world,
                                                     LivingEntity user,
                                                     CallbackInfoReturnable<ItemStack> cir) {
        if (!BalanceConfig.ENABLED) {
            return;
        }
        if (!stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            return;
        }
        if (!(user instanceof PlayerEntity player)) {
            return;
        }
        HungerManager hunger = player.getHungerManager();
        // Vanilla already added nutrition 4 / saturation 9.6; add the residual
        // delta (capped at 20 food / food-level saturation).
        int newFood = Math.min(hunger.getFoodLevel()
                + BalanceConfig.EGA_NUTRITION_BONUS, 20);
        hunger.setFoodLevel(newFood);
        hunger.setSaturationLevel(Math.min(
                hunger.getSaturationLevel() + BalanceConfig.EGA_SATURATION_BONUS,
                newFood));
    }
}
