package net.xyressa.stormterror.modules.render;

import net.xyressa.stormterror.system.Category;
import net.xyressa.stormterror.system.Module;

public class Fullbright extends Module {

    public static boolean fullbrightEnabled = false;

    public Fullbright() {
        super("Fullbright", "You know what it does.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        fullbrightEnabled = true;
    }

    @Override
    public void onDisable() {
        fullbrightEnabled = false;
    }
}