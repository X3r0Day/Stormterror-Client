package net.xyressa.stormterror.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.xyressa.stormterror.modules.movement.Speed;
import net.xyressa.stormterror.system.Module;
import net.xyressa.stormterror.system.ModuleManager;
import net.xyressa.stormterror.system.settings.NumberSetting;

public class ModuleScreen extends Screen {

    public ModuleScreen() {
        super(Text.of("Stormterror Modules"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int yOffset = -50;

        // Loop through all modules and create buttons for them
        for (Module module : ModuleManager.INSTANCE.getModules()) {

            // 1. Toggle Button
            ButtonWidget btn = ButtonWidget.builder(
                            Text.of(module.getName() + ": " + (module.isEnabled() ? "ON" : "OFF")),
                            button -> {
                                module.toggle();
                                button.setMessage(Text.of(module.getName() + ": " + (module.isEnabled() ? "ON" : "OFF")));
                            })
                    .dimensions(centerX - 100, centerY + yOffset, 200, 20)
                    .build();
            this.addDrawableChild(btn);

            // Special Case: If it's Speed, add a slider for the new NumberSetting
            if (module instanceof Speed) {
                yOffset += 25;
                Speed speedModule = (Speed) module;

                // Access the new Setting object
                NumberSetting speedSetting = speedModule.speed;

                // Calculate the 0.0 - 1.0 progress for the slider
                double min = speedSetting.getMin();
                double max = speedSetting.getMax();
                double current = speedSetting.getValue();
                double progress = (current - min) / (max - min);

                SliderWidget slider = new SliderWidget(centerX - 100, centerY + yOffset, 200, 20, Text.of("Speed"), progress) {
                    @Override
                    protected void updateMessage() {
                        this.setMessage(Text.of(String.format("Speed: %.2fx", speedSetting.getValue())));
                    }

                    @Override
                    protected void applyValue() {
                        // Convert slider progress (0.0-1.0) back to actual speed value
                        double newVal = min + (this.value * (max - min));
                        speedSetting.setValue(newVal);
                    }
                };
                this.addDrawableChild(slider);
            }

            yOffset += 30; // Move down for next module
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 80, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}