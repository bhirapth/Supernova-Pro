package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventHandler;
import dev.luminous.api.events.impl.UpdateWalkingPlayerEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;

public class SilentDouble extends Module {
    public static SilentDouble INSTANCE;

    private final BooleanSetting onlyKill =
            add(new BooleanSetting("OnlyKill", false));

    public SilentDouble() {
        super("SilentDouble", Category.Combat);
        setChinese("静默双持");
        INSTANCE = this;
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (nullCheck()) return;
        // Silent double hand logic
    }
}
