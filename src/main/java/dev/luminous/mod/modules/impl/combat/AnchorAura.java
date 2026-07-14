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
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class AnchorAura extends Module {
    public static AnchorAura INSTANCE;

    public final SliderSetting delay =
            add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final SliderSetting range =
            add(new SliderSetting("Range", 5.0, 1.0, 6.0, 0.1).setSuffix("m"));
    private final SliderSetting targetRange =
            add(new SliderSetting("TargetRange", 8.0, 1.0, 8.0, 0.1).setSuffix("m"));
    private final SliderSetting minDamage =
            add(new SliderSetting("MinDamage", 5.0, 0.0, 20.0, 0.5).setSuffix("dmg"));
    private final SliderSetting maxSelf =
            add(new SliderSetting("SelfDamage", 12.0, 0.0, 20.0, 0.5).setSuffix("dmg"));
    private final BooleanSetting inventorySwap =
            add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting usingPause =
            add(new BooleanSetting("UsingPause", true));

    private final Timer timer = new Timer();

    public AnchorAura() {
        super("AnchorAura", Category.Combat);
        setChinese("锚点光环");
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
            // Find anchor placement position
            for (BlockPos pos : BlockUtil.getSphere((float) range.getValue())) {
                if (BlockUtil.canPlace(pos, range.getValue())) {
                    int slot = inventorySwap.getValue() ?
                        InventoryUtil.findBlockInventorySlot(Blocks.RESPAWN_ANCHOR) :
                        InventoryUtil.findBlock(Blocks.RESPAWN_ANCHOR);
                    if (slot != -1) {
                        // Place anchor
                        timer.reset();
                        return;
                    }
                }
            }
        }
    }
}
