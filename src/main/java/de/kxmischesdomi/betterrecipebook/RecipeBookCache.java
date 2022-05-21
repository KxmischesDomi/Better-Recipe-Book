package de.kxmischesdomi.betterrecipebook;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.Recipe;

import java.util.LinkedList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RecipeBookCache {

	public String search = "";
	public int page = 0;
	public RecipeBookCategories category;
	public List<RecipeCollection> history = new LinkedList<>();
	public Recipe<?> lastRecipe;

	public RecipeBookCache(RecipeBookCategories category) {
		this.category = category;
	}

}
