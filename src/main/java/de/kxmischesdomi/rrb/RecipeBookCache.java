package de.kxmischesdomi.rrb;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RecipeBookCache {

	public String search = "";
	public int page = 0;
	public RecipeBookCategories category;
	public List<RecipeCollection> history = new ArrayList<>();

	public RecipeBookCache(RecipeBookCategories category) {
		this.category = category;
	}

}
