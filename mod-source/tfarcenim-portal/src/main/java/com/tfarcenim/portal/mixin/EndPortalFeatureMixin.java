package com.tfarcenim.portal.mixin;

import com.tfarcenim.core.util.DifficultyManager;
import com.tfarcenim.portal.TfarcenimPortal;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * EndPortalFeatureMixin — 普通难度末地门少一个框.
 *
 * <p>In NORMAL difficulty, removes one End Portal frame block from the
 * generated End Portal structure. This makes it impossible to activate
 * the portal without finding an Eye of Ender to fill the missing slot,
 * creating the "can play but can't beat the game" torture of NORMAL mode.
 *
 * <p>The frame block is removed after the portal is generated, ensuring
 * the portal blocks themselves are not affected — only the frame is
 * missing one piece.
 */
@Mixin(EndPortalFeature.class)
public class EndPortalFeatureMixin {

    /**
     * Injects after the End Portal feature generation completes.
     * In NORMAL difficulty, removes one frame block.
     */
    @Inject(method = "generate", at = @At("RETURN"))
    private void tfarcenim$removeFrameInNormal(FeatureContext<?> context,
                                               CallbackInfoReturnable<Boolean> cir) {
        // Only proceed if generation was successful
        if (!cir.getReturnValue()) {
            return;
        }

        DifficultyManager dm = DifficultyManager.getInstance();

        // Only remove a frame block in NORMAL difficulty
        if (!dm.isNormal()) {
            return;
        }

        // Get the world and origin position from the feature context
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        // The End Portal frame is a 3x3 ring of bedrock with portal blocks in the center.
        // The frame blocks (End Portal Frame) are placed in a specific pattern.
        // We need to find and remove one End Portal Frame block.

        // Search for End Portal Frame blocks in a 5x5 area around the origin
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos pos = origin.add(x, 0, z);
                if (world.getBlockState(pos).getBlock() == Blocks.END_PORTAL_FRAME) {
                    // Found a frame block — remove it (set to air)
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                    TfarcenimPortal.LOGGER.info(
                            "Removed End Portal frame block at {} (NORMAL difficulty)", pos);
                    return; // Only remove one frame block
                }
            }
        }

        // If no frame block was found at y=0, search y=1 and y=-1
        for (int y : new int[]{1, -1}) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos pos = origin.add(x, y, z);
                    if (world.getBlockState(pos).getBlock() == Blocks.END_PORTAL_FRAME) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                        TfarcenimPortal.LOGGER.info(
                                "Removed End Portal frame block at {} (NORMAL difficulty)", pos);
                        return;
                    }
                }
            }
        }
    }
}
