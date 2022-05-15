package de.kxmischesdomi.rrb;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;

import java.util.HashMap;
import java.util.Map;

public class RememberRecipeBookMod implements ClientModInitializer {

	public static final Map<RecipeBookType, RecipeBookCache> BOOK_CACHE = new HashMap<>();
	public static RecipeBookCategories HISTORY_CATEGORY;

	@Override
	public void onInitializeClient() {
		HISTORY_CATEGORY = ClassTinkerers.getEnum(RecipeBookCategories.class, "HISTORY");
	}

}
