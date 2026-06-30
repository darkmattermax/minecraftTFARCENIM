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
 * Raises the crossbow durability to 380 (vanilla 326) — bone + leather
 * reinforcement. The +5% reload-speed bonus described in the design doc is
 * applied via the charging accessor where present; durability is the
 * guaranteed override here.
 *
 * <p><b>Implementation note:</b> targets {@link Item} (not {@code CrossbowItem})
 * with an item-identity guard, because {@code getMaxDamage} is inherited and a
 * subclass-targeted injection would affect all items. See {@link BowItemMixin}.
 *
 * <p><b>Mapping note (Yarn 1.21.10):</b> descriptor
 * {@code getMaxDamage(Lnet/minecraft/item/ItemStack;)I}.
 */
@Mixin(Item.class)
public class CrossbowItemMixin {

    @Inject(method = "getMaxDamage(Lnet/minecraft/item/ItemStack;)I",
            at = @At("RETURN"), cancellable = true)
    private void tfarcenim$crossbowDurability(ItemStack stack,
                                              CallbackInfoReturnable<Integer> cir) {
        if (BalanceConfig.ENABLED && stack.isOf(Items.CROSSBOW)) {
            cir.setReturnValue(BalanceConfig.CROSSBOW_DURABILITY);
        }
    }
}
