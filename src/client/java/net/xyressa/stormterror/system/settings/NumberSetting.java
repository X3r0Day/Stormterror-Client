package net.xyressa.stormterror.system.settings;

public class NumberSetting extends Setting<Double> {
    private final double min, max;

    public NumberSetting(String name, double min, double max, double defaultValue) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }

    // Helper to get float for Minecraft physics
    public float getFloatValue() { return getValue().floatValue(); }
}