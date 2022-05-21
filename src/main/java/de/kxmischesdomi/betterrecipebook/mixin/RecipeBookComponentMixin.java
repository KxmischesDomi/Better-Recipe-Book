package de.kxmischesdomi.betterrecipebook.mixin;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.betterrecipebook.BetterRecipeBookMod;
import de.kxmischesdomi.betterrecipebook.RecipeBookCache;
import de.kxmischesdomi.betterrecipebook.RecipeCacheConfig;
import de.kxmischesdomi.betterrecipebook.gui.HistoryTabButton;
import de.kxmischesdomi.betterrecipebook.gui.SearchClearButton;
import de.kxmischesdomi.betterrecipebook.mixin.accessor.RecipeBookPageAccessor;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.*;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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
	@Shadow protected abstract void updateCollections(boolean bl);
	@Shadow private int width;
	@Shadow private int xOffset;
	@Shadow private int height;
	@Shadow protected Minecraft minecraft;
	@Shadow @Final protected GhostRecipe ghostRecipe;

	@Shadow protected abstract void setVisible(boolean bl);

	private SearchClearButton clearButton;

	@ModifyArgs(method = "initVisuals", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/StateSwitchingButton;<init>(IIIIZ)V"))
	public void initVisuals(Args args) {
		int i = args.get(1);
		args.set(1, i - 2);
	}

	@Inject(method = "initVisuals", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;updateCollections(Z)V", shift = At.Shift.BEFORE))
	public void initVisualsMixin(CallbackInfo ci) {
		int i = (this.width - 147) / 2 - this.xOffset;
		int j = (this.height - 166) / 2;
		clearButton = new SearchClearButton(i + 100, j + 15, button -> {
			searchBox.setValue("");
			lastSearch = "";
			getCache().search = "";
			updateCollections(false);
		});

		HistoryTabButton history = new HistoryTabButton(BetterRecipeBookMod.HISTORY_CATEGORY);
		history.visible = true;
		tabButtons.add(1, history);

		RecipeBookCache cache = getCache();
		lastSearch = cache.search;
	}

	@Inject(method = "initVisuals", at = @At("TAIL"))
	public void initVisualsMixinTails(CallbackInfo ci) {
		// Doing all this after the visual initials since it seems to cause problems to the visibility of the other tabs when doing it earlier
		RecipeBookCache cache = getCache();
		for (RecipeBookTabButton button : tabButtons) {
			if (button.getCategory().equals(cache.category)) {
				selectedTab.setStateTriggered(false);
				selectedTab = button;
				selectedTab.setStateTriggered(true);
				break;
			}
		}
		((RecipeBookPageAccessor) recipeBookPage).setCurrentPage(cache.page);
		searchBox.setValue(lastSearch);
		this.updateCollections(false);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tickMixin(CallbackInfo ci) {
		while (BetterRecipeBookMod.recipeBookKeyBind.consumeClick()) {
			setVisible(false);
		}
	}

	@Inject(method = "checkSearchStringUpdate", at = @At("TAIL"))
	public void checkSearchStringUpdateMixin(CallbackInfo ci) {
		getCache().search = lastSearch;
	}

	@Inject(method = "mouseClicked", at = @At("TAIL"))
	public void mouseClickedMixinTail(double d, double e, int i, CallbackInfoReturnable<Boolean> cir) {
		RecipeBookCache cache = getCache();
		cache.category = selectedTab.getCategory();
		cache.page = ((RecipeBookPageAccessor) recipeBookPage).getCurrentPage();
	}

	@Inject(method = "mouseClicked", cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/components/EditBox;mouseClicked(DDI)Z"))
	public void mouseClickedMixinClearButton(double d, double e, int i, CallbackInfoReturnable<Boolean> cir) {
		if (!searchBox.getValue().isEmpty()) {
			if (clearButton.mouseClicked(d, e, i)) {
				cir.setReturnValue(true);
			}
		}

	}

	@Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookPage;getLastClickedRecipeCollection()Lnet/minecraft/client/gui/screens/recipebook/RecipeCollection;"))
	public RecipeCollection mouseClickedMixinRedirect(RecipeBookPage instance) {
		RecipeCollection collection = instance.getLastClickedRecipeCollection();
		if (collection != null) {
			RecipeBookCache cache = getCache();
			List<RecipeCollection> history = cache.history;

			String name = RecipeCacheConfig.getName(collection);
			history.removeIf(collection1 -> name.equals(RecipeCacheConfig.getName(collection1)));

			history.add(0, collection);

			cache.history = history.subList(0, Math.min(history.size(), 4 * 5));
			cache.lastRecipe = instance.getLastClickedRecipe();
		}
		if (selectedTab.getCategory() == BetterRecipeBookMod.HISTORY_CATEGORY) {
			updateCollections(false);
		}

		return collection;
	}

	@Inject(method = "keyPressed", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/components/EditBox;keyPressed(III)Z"), cancellable = true)
	public void keyPressedMixin(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {

		if (searchBox.isFocused()) return;
		if (i == 32) {
			RecipeBookCache cache = getCache();
			if (cache.history.isEmpty()) return;
			Recipe<?> recipe = cache.lastRecipe;
			RecipeCollection recipeCollection = cache.history.get(0);
			if (recipe != null) {
				if (!recipeCollection.isCraftable(recipe) && this.ghostRecipe.getRecipe() == recipe) {
					return;
				}
				this.ghostRecipe.clear();
				minecraft.gameMode.handlePlaceRecipe(minecraft.player.containerMenu.containerId, recipe, Screen.hasShiftDown());
				cir.setReturnValue(true);
			}
		}

	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE))
	public void renderMixin(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
		if (!searchBox.getValue().isEmpty()) {
			clearButton.render(poseStack, i, j, f);
		}
	}


	@Redirect(method = "updateCollections", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ClientRecipeBook;getCollection(Lnet/minecraft/client/RecipeBookCategories;)Ljava/util/List;"))
	public List<RecipeCollection> renderMixin(ClientRecipeBook instance, RecipeBookCategories recipeBookCategories) {
		if (selectedTab.getCategory() == BetterRecipeBookMod.HISTORY_CATEGORY) {
			List<RecipeCollection> history = getCache().history;
			for (RecipeCollection recipeCollection : history) {
				recipeCollection.updateKnownRecipes(book);
			}
			return Lists.newArrayList(history);
		}
		return instance.getCollection(recipeBookCategories);
	}

	private RecipeBookCache getCache() {
		return BetterRecipeBookMod.BOOK_CACHE.computeIfAbsent(menu.getRecipeBookType(), recipeBookType ->
				new RecipeBookCache(selectedTab.getCategory()));
	}

}
