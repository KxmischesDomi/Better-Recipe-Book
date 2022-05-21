package de.kxmischesdomi.betterrecipebook.mixin.keybind;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AbstractButton.class)
public class AbstractButtonMixin {

	@Inject(method = "keyPressed", cancellable = true, at = @At("HEAD"))
	public void keyPressedMixin(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
		if (i == 32) {

			Screen screen = Minecraft.getInstance().screen;
			if (screen != null) {
				for (GuiEventListener child : screen.children()) {
					if (child instanceof RecipeBookComponent recipeBookComponent) {
						recipeBookComponent.keyPressed(i, j, k);
					}
				}
			}

			if (((Object) this) instanceof ImageButton imageButton) {
				if (imageButton.resourceLocation.toString().equals("minecraft:textures/gui/recipe_button.png")) {

					// Block toggling with space bar
					cir.setReturnValue(true);
				}
			}
		}
	}

}
