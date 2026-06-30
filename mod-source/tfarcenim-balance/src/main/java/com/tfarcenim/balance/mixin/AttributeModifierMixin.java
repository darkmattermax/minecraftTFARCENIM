package com.tfarcenim.balance.mixin;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.tfarcenim.balance.config.BalanceConfig;
import net.minecraft.entity.EquipmentSlotGroup;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Applies the "star power" attack-speed bonus to the netherite sword
 * (1.6 → 1.9, +19%) by appending a dedicated attribute modifier.
 *
 * <p>The companion star-power <em>damage</em> bonus (+2, lifting the sword to
 * 10 and the axe to 12) is delivered through the material attack-damage
 * baseline in {@link ToolMaterialMixin}, so this Mixin deliberately adds only
 * the attack-speed modifier to avoid double-counting damage.
 *
 * <p><b>Mapping note (Yarn 1.21.10):</b> intercepts
 * {@code Item#getAttributeModifiers(ItemStack)}, which returns the
 * {@code ATTRIBUTE_MODIFIERS} component multimap. The appended modifier uses
 * {@code Operation.ADD_VALUE} (1.21.2+ rename of {@code ADD}) and the
 * {@code MAINHAND} slot group. If the target method signature or attribute
 * registry entry name differs in your mappings, adjust the {@code method}
 * literal / {@code EntityAttributes} constant — the dispatched modifier is
 * identical.
 */
@Mixin(Item.class)
public class AttributeModifierMixin {

    /**
     * Appends the star-power attack-speed modifier to the netherite sword's
     * attribute multimap.
     *
     * @param stack the item stack being equipped/queried.
     * @param cir   the return-value callback carrying the attribute multimap.
     */
    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$starPowerAttackSpeed(
            ItemStack stack,
            CallbackInfoReturnable<Multimap<RegistryEntry<EntityAttribute>,
                    EntityAttributeModifier>> cir) {
        if (!BalanceConfig.ENABLED) {
            return;
        }
        if (!stack.isOf(Items.NETHERITE_SWORD)) {
            return;
        }
        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> original =
                cir.getReturnValue();
        // Copy into a mutable multimap so the vanilla modifiers are preserved.
        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> augmented =
                LinkedHashMultimap.create(original);
        EntityAttributeModifier starPower = new EntityAttributeModifier(
                new Identifier(BalanceConfig.STAR_POWER_NAMESPACE,
                        BalanceConfig.STAR_POWER_SPEED_PATH),
                BalanceConfig.STAR_POWER_ATTACK_SPEED_BONUS,
                EntityAttributeModifier.Operation.ADD_VALUE,
                EquipmentSlotGroup.MAINHAND);
        augmented.put(EntityAttributes.GENERIC_ATTACK_SPEED, starPower);
        cir.setReturnValue(augmented);
    }
}
