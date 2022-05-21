package de.kxmischesdomi.betterrecipebook.mixin;

import de.kxmischesdomi.betterrecipebook.BetterRecipeBookMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Screen.class)
public class ScreenMixin {

	@Shadow @Final private List<Widget> renderables;

	@Shadow @Final private List<GuiEventListener> children;

	@Inject(method = "keyPressed", at = @At("TAIL"), cancellable = true)
	public void keyPressedMixin(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
		if (BetterRecipeBookMod.recipeBookKeyBind.matches(i, j)) {
			for (GuiEventListener child : children) {
				if (child instanceof RecipeBookComponent recipeBookComponent) {
					if (recipeBookComponent.searchBox.isFocused()) {
						return;
					}
				}
			}
			for (Widget renderable : renderables) {
				if (renderable instanceof ImageButton imageButton) {
					if (imageButton.resourceLocation.toString().equals("minecraft:textures/gui/recipe_button.png")) {
						imageButton.playDownSound(Minecraft.getInstance().getSoundManager());
						imageButton.onPress();
						break;
					}
				}

			}
			cir.setReturnValue(true);
		}
	}

}
