package de.kxmischesdomi.betterrecipebook;

import com.chocohead.mm.api.ClassTinkerers;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class BetterRecipeBookMod implements ClientModInitializer {

	public static final String MOD_ID = "betterrecipebook";
	public static final Map<RecipeBookType, RecipeBookCache> BOOK_CACHE = new HashMap<>();
	public static RecipeBookCategories HISTORY_CATEGORY;
	public static KeyMapping recipeBookKeyBind;

	@Override
	public void onInitializeClient() {
		HISTORY_CATEGORY = ClassTinkerers.getEnum(RecipeBookCategories.class, "HISTORY");

		recipeBookKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				String.format("key.%s.recipebook", MOD_ID),
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"key.categories.inventory"
		));

	}

}
