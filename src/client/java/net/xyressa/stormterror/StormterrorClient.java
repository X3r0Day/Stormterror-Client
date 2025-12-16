package net.xyressa.stormterror;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.xyressa.stormterror.system.ModuleManager;
import net.xyressa.stormterror.ui.ModuleScreen;
import org.lwjgl.glfw.GLFW;

public class StormterrorClient implements ClientModInitializer {

	private static final KeyBinding MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.stormterror.menu",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			"category.stormterror"
	));

	@Override
	public void onInitializeClient() {

		// 1. Tick the Module Manager (which ticks all enabled modules)
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				ModuleManager.INSTANCE.onTick();
			}
		});

		// 2. Handle GUI opening
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (MENU_KEY.wasPressed()) {
				client.setScreen(new net.xyressa.stormterror.ui.clickgui.ClickGUIScreen());
			}
		});
	}
}