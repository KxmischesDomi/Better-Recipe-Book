package de.kxmischesdomi.betterrecipebook.mixin;

import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(RecipeBookPage.class)
public interface RecipeBookPageAccessor {

	@Accessor("currentPage")
	int getCurrentPage();

	@Accessor("currentPage")
	void setCurrentPage(int page);

}
