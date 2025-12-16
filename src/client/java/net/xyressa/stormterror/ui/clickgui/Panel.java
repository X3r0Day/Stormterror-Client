package net.xyressa.stormterror.ui.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.xyressa.stormterror.system.Category;
import net.xyressa.stormterror.system.Module;
import net.xyressa.stormterror.system.ModuleManager;
import java.util.ArrayList;
import java.util.List;

public class Panel {
    public int x, y, width, height;
    private final Category category;
    public final List<ModuleButton> buttons = new ArrayList<>();

    public boolean dragging = false;
    public boolean expanded = true;
    private int dragX, dragY;

    public Panel(Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = 120;
        this.height = 20;

        int offset = height;
        for (Module m : ModuleManager.INSTANCE.getModules()) {
            if (m.getCategory() == category) {
                buttons.add(new ModuleButton(m, this, offset));
                offset += 15;
            }
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (dragging) {
            x = (int) mouseX - dragX;
            y = (int) mouseY - dragY;
        }

        // Header Background (Dark: 0xFF141414)
        context.fill(x, y, x + width, y + height, 0xFF141414);
        context.drawText(MinecraftClient.getInstance().textRenderer, category.name(), x + 5, y + 6, 0xFFFFFFFF, true);

        if (expanded) {
            int offset = height;
            for (ModuleButton button : buttons) {
                button.offset = offset;
                button.render(context, mouseX, mouseY, delta);
                offset += button.getHeight();
            }
            // Shadow at bottom
            context.fill(x, y + offset, x + width, y + offset + 2, 0xAA000000);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHoveredHeader(mouseX, mouseY)) {
            if (button == 0) expanded = !expanded;
            else if (button == 1) {
                dragging = true;
                dragX = (int) mouseX - x;
                dragY = (int) mouseY - y;
            }
            return;
        }

        if (expanded) {
            for (ModuleButton mb : buttons) {
                mb.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    private boolean isHoveredHeader(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}