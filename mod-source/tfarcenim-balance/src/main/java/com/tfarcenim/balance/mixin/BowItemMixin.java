package com.tfarcenim.balance.mixin;

import com.tfarcenim.balance.config.BalanceConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Raises the bow durability to 450 (vanilla 384) — the bone-laminated bow
 * "star power" reinforcement.
 *
 * <p><b>Implementation note:</b> {@code getMaxDamage} is declared on {@link Item}
 * and inherited by {@code BowItem}; a {@code @Mixin(BowItem.class)} injection
 * would land in the shared superclass method and corrupt <em>every</em> item's
 * durability. We therefore target {@link Item} directly and scope the override
 * with an item-identity guard, so only bows are affected.
 *
 * <p><b>Mapping note (Yarn 1.21.10):</b> the descriptor
 * {@code getMaxDamage(Lnet/minecraft/item/ItemStack;)I} resolves the
 * {@code MAX_DAMAGE} component accessor.
 */
@Mixin(Item.class)
public class BowItemMixin {

    @Inject(method = "getMaxDamage(Lnet/minecraft/item/ItemStack;)I",
            at = @At("RETURN"), cancellable = true)
    private void tfarcenim$bowDurability(ItemStack stack,
                                         CallbackInfoReturnable<Integer> cir) {
        if (BalanceConfig.ENABLED && stack.isOf(Items.BOW)) {
            cir.setReturnValue(BalanceConfig.BOW_DURABILITY);
        }
    }
}
