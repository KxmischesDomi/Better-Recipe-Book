package de.kxmischesdomi.betterrecipebook.mixin.cache;

import de.kxmischesdomi.betterrecipebook.RecipeCacheConfig;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(ClientRecipeBook.class)
public class RecipeManagerMixin {

	@Shadow private List<RecipeCollection> allCollections;

	@Inject(method = "setupCollections", at = @At("TAIL"))
	public void applyMixin(Iterable<Recipe<?>> iterable, CallbackInfo ci) {
		RecipeCacheConfig.loadCache(allCollections);
	}

}
