package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventHandler;
import dev.luminous.api.events.impl.UpdateWalkingPlayerEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.entity.EntityUtil;
import dev.luminous.api.utils.entity.InventoryUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoBurrow extends Module {
    public static AutoBurrow INSTANCE;

    public final SliderSetting delay =
            add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final SliderSetting range =
            add(new SliderSetting("Range", 5.0, 1.0, 6.0, 0.1).setSuffix("m"));
    private final SliderSetting targetRange =
            add(new SliderSetting("TargetRange", 8.0, 1.0, 8.0, 0.1).setSuffix("m"));
    private final BooleanSetting inventorySwap =
            add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting usingPause =
            add(new BooleanSetting("UsingPause", true));

    private final Timer timer = new Timer();

    public AutoBurrow() {
        super("AutoBurrow", Category.Combat);
        setChinese("自动困人");
        INSTANCE = this;
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        onUpdate();
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        if (!timer.passedMs(delay.getValueInt())) return;
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return;
        if (usingPause.getValue() && mc.player.isUsingItem()) return;

        for (PlayerEntity target : CombatUtil.getEnemies(targetRange.getValue())) {
            BlockPos targetPos = target.getBlockPos();
            // Try to place blocks around/above the target to trap them
            for (Direction dir : Direction.HORIZONTAL) {
                BlockPos placePos = targetPos.offset(dir);
                if (BlockUtil.canPlace(placePos, range.getValue())) {
                    int slot = inventorySwap.getValue() ?
                        InventoryUtil.findBlockInventorySlot(net.minecraft.block.Blocks.OBSIDIAN) :
                        InventoryUtil.findBlock(net.minecraft.block.Blocks.OBSIDIAN);
                    if (slot != -1) {
                        // Place block
                        timer.reset();
                        return;
                    }
                }
            }
        }
    }
}
