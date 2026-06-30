package com.tfarcenim.core.event;

import com.tfarcenim.core.TfarcenimCore;
import com.tfarcenim.core.item.GuideBookItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * RewardChestHandler — 奖励箱填充事件处理器.
 *
 * <p>Listens for world load events and fills any starter/bonus chest
 * with TFARCENIM-specific items:
 * <ul>
 *   <li>Wooden axe (1)</li>
 *   <li>Guide book (1) — "Take everything from the chest!"</li>
 *   <li>Leather (3-5)</li>
 *   <li>String (3-5)</li>
 *   <li>Bread (5)</li>
 *   <li>Torches (10)</li>
 * </ul>
 */
public class RewardChestHandler {

    private static final Random RANDOM = new Random();

    /**
     * Registers the world-load event callback.
     * Called from {@code TfarcenimCore.onInitialize()}.
     */
    public static void register() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            onWorldLoad(world);
        });
        TfarcenimCore.LOGGER.info("Registered RewardChestHandler");
    }

    /**
     * Called when a world is loaded. Searches for bonus chests and fills them.
     *
     * @param world the loaded world
     */
    private static void onWorldLoad(World world) {
        if (world.isClient) {
            return;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        // Scan a 32x32 area around spawn for chests
        BlockPos spawnPos = serverWorld.getSpawnPos();
        int searchRadius = 32;

        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = -2; y <= 5; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos pos = spawnPos.add(x, y, z);
                    if (serverWorld.getBlockState(pos).getBlock() == Blocks.CHEST) {
                        ChestBlockEntity chest = (ChestBlockEntity) serverWorld.getBlockEntity(pos);
                        if (chest != null) {
                            fillRewardChest(chest);
                            TfarcenimCore.LOGGER.info("Filled reward chest at {}", pos);
                        }
                    }
                }
            }
        }
    }

    /**
     * Fills a chest with TFARCENIM starter items.
     *
     * @param chest the chest block entity to fill
     */
    private static void fillRewardChest(ChestBlockEntity chest) {
        // Clear existing contents
        chest.clear();

        // Slot 0: Wooden axe
        chest.setStack(0, new ItemStack(Items.WOODEN_AXE, 1));

        // Slot 1: Guide book
        chest.setStack(1, GuideBookItem.createGuideBook());

        // Slot 2: Leather (3-5)
        int leatherCount = 3 + RANDOM.nextInt(3);
        chest.setStack(2, new ItemStack(Items.LEATHER, leatherCount));

        // Slot 3: String (3-5)
        int stringCount = 3 + RANDOM.nextInt(3);
        chest.setStack(3, new ItemStack(Items.STRING, stringCount));

        // Slot 4: Bread (5)
        chest.setStack(4, new ItemStack(Items.BREAD, 5));

        // Slot 5: Torches (10)
        chest.setStack(5, new ItemStack(Items.TORCH, 10));

        // Slot 6: Oak planks (4) — starter building material
        chest.setStack(6, new ItemStack(Items.OAK_PLANKS, 4));

        // Slot 7: Stick (4) — starter crafting material
        chest.setStack(7, new ItemStack(Items.STICK, 4));

        // Mark chest as updated
        chest.markDirty();
    }
}
