package net.xyressa.stormterror.ui.clickgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.xyressa.stormterror.system.Category;
import java.util.ArrayList;
import java.util.List;

public class ClickGUIScreen extends Screen {
    private final List<Panel> panels = new ArrayList<>();
    private SettingsWindow currentSettingsWindow = null;

    public ClickGUIScreen() {
        super(Text.of("ClickGUI"));
        int x = 20;
        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, 20));
            x += 125;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // 1. Render Panels
        for (Panel panel : panels) {
            panel.render(context, mouseX, mouseY, delta);
        }

        // 2. Render Settings Window on top
        if (currentSettingsWindow != null) {
            currentSettingsWindow.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 1. Check Settings Window First
        if (currentSettingsWindow != null) {
            currentSettingsWindow.mouseClicked(mouseX, mouseY, button);

            // Optional: Close window if clicking outside (Currently disabled logic)
            if (button == 0 && !isHoveredSettings(mouseX, mouseY)) {
                // currentSettingsWindow = null;
            }
        }

        // 2. Check Panels
        for (Panel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, button);

            if (panel.expanded) {
                // Ensure Panel.buttons is public!
                for (ModuleButton mb : panel.buttons) {
                    if (mb.isHovered(mouseX, mouseY) && button == 1) {
                        currentSettingsWindow = new SettingsWindow(mb.module, (int)mouseX + 10, (int)mouseY);
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (currentSettingsWindow != null) {
            currentSettingsWindow.mouseReleased(mouseX, mouseY, button);
        }
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isHoveredSettings(double mouseX, double mouseY) {
        if (currentSettingsWindow == null) return false;
        // FIX: Use getTotalHeight() instead of manual calculation
        return mouseX >= currentSettingsWindow.x &&
                mouseX <= currentSettingsWindow.x + currentSettingsWindow.width &&
                mouseY >= currentSettingsWindow.y &&
                mouseY <= currentSettingsWindow.y + currentSettingsWindow.getTotalHeight();
    }

    @Override
    public boolean shouldPause() { return false; }
}