package de.kxmischesdomi.betterrecipebook.mixin;

import com.google.common.collect.Lists;
import de.kxmischesdomi.betterrecipebook.BetterRecipeBookMod;
import de.kxmischesdomi.betterrecipebook.HistoryTabButton;
import de.kxmischesdomi.betterrecipebook.RecipeBookCache;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.inventory.RecipeBookMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(RecipeBookComponent.class)
public abstract class RecipeBookComponentMixin {

	@Shadow private String lastSearch;
	@Shadow private @Nullable EditBox searchBox;

	@Shadow protected RecipeBookMenu<?> menu;

	@Shadow @Final private RecipeBookPage recipeBookPage;

	@Shadow private @Nullable RecipeBookTabButton selectedTab;

	@Shadow @Final private List<RecipeBookTabButton> tabButtons;

	@Shadow private ClientRecipeBook book;

	@Inject(method = "initVisuals", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;updateCollections(Z)V", shift = At.Shift.BEFORE))
	public void initVisualsMixin(CallbackInfo ci) {
		HistoryTabButton history = new HistoryTabButton(BetterRecipeBookMod.HISTORY_CATEGORY);
		history.visible = true;
		tabButtons.add(1, history);

		RecipeBookCache cache = getCache();
		lastSearch = cache.search;
		searchBox.setValue(lastSearch);
		for (RecipeBookTabButton button : tabButtons) {
			if (button.getCategory().equals(cache.category)) {
				selectedTab.setStateTriggered(false);
				selectedTab = button;
				selectedTab.setStateTriggered(true);
				break;
			}
		}
		((RecipeBookPageAccessor) recipeBookPage).setCurrentPage(cache.page);
	}

	@Inject(method = "checkSearchStringUpdate", at = @At("TAIL"))
	public void checkSearchStringUpdateMixin(CallbackInfo ci) {
		getCache().search = lastSearch;
	}

	@Inject(method = "mouseClicked", at = @At("TAIL"))
	public void mouseClickedMixin(double d, double e, int i, CallbackInfoReturnable<Boolean> cir) {
		RecipeBookCache cache = getCache();
		cache.category = selectedTab.getCategory();
		cache.page = ((RecipeBookPageAccessor) recipeBookPage).getCurrentPage();
	}

	@Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookPage;getLastClickedRecipeCollection()Lnet/minecraft/client/gui/screens/recipebook/RecipeCollection;"))
	public RecipeCollection mouseClickedMixinRedirect(RecipeBookPage instance) {
		RecipeCollection collection = instance.getLastClickedRecipeCollection();
		if (collection != null) {
			List<RecipeCollection> history = getCache().history;
			history.remove(collection);
			history.add(0, collection);
		}
		return collection;
	}

	@Redirect(method = "updateCollections", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ClientRecipeBook;getCollection(Lnet/minecraft/client/RecipeBookCategories;)Ljava/util/List;"))
	public List<RecipeCollection> renderMixin(ClientRecipeBook instance, RecipeBookCategories recipeBookCategories) {
		if (selectedTab.getCategory() == BetterRecipeBookMod.HISTORY_CATEGORY) {
			return Lists.newArrayList(getCache().history);
		}
		return instance.getCollection(recipeBookCategories);
	}

	private RecipeBookCache getCache() {
		return BetterRecipeBookMod.BOOK_CACHE.computeIfAbsent(menu.getRecipeBookType(), recipeBookType ->
				new RecipeBookCache(RecipeBookCategories.getCategories(menu.getRecipeBookType()).get(0)));
	}

}
