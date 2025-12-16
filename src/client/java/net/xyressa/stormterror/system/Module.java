package net.xyressa.stormterror.system;

import net.minecraft.client.MinecraftClient;
import net.xyressa.stormterror.system.settings.Setting;
import java.util.ArrayList;
import java.util.List;

public class Module {
    // We allow this to be null initially
    protected MinecraftClient mc = null;

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;

    private final List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // FIX: This ensures 'mc' is never null when the module ticks
    public void onTickWrapper() {
        if (mc == null) {
            mc = MinecraftClient.getInstance();
        }
        onTick();
    }

    public void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) {
            // Ensure mc is ready when enabling
            if (mc == null) mc = MinecraftClient.getInstance();
            onEnable();
        } else {
            onDisable();
        }
    }

    public boolean isEnabled() { return enabled; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }

    // Modules override these
    public void onTick() {}
    public void onEnable() {}
    public void onDisable() {}
}