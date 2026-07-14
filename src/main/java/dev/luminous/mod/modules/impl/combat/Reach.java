package dev.luminous.mod.modules.impl.combat;

import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;

public class Reach extends Module {
    public static Reach INSTANCE;

    public final SliderSetting range =
            add(new SliderSetting("Range", 4.5, 3.0, 6.0, 0.1).setSuffix("m"));

    public Reach() {
        super("Reach", Category.Combat);
        setChinese("攻击距离");
        INSTANCE = this;
    }
}
