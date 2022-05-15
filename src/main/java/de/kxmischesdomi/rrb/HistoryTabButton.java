package de.kxmischesdomi.rrb;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class HistoryTabButton extends RecipeBookTabButton {

	public HistoryTabButton(RecipeBookCategories recipeBookCategories) {
		super(recipeBookCategories);
	}

	@Override
	public boolean updateVisibility(ClientRecipeBook clientRecipeBook) {
		return visible = true;
	}



}
