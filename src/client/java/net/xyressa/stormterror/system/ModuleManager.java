package net.xyressa.stormterror.system;

import net.xyressa.stormterror.modules.movement.AirJump;
import net.xyressa.stormterror.modules.movement.NoFall;
import net.xyressa.stormterror.modules.movement.Speed;
import net.xyressa.stormterror.modules.render.Freecam;
import net.xyressa.stormterror.modules.render.Fullbright;
import net.xyressa.stormterror.modules.render.Xray;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {
        add(new Speed());
        add(new NoFall());
        add(new AirJump());
        add(new Xray());
        add(new Fullbright());
        add(new Freecam());
    }

    private void add(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                // FIX: Call the wrapper, NOT onTick() directly
                module.onTickWrapper();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz) {
        for (Module module : modules) {
            if (module.getClass() == clazz) {
                return (T) module;
            }
        }
        return null;
    }
}