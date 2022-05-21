package de.kxmischesdomi.betterrecipebook.mixin;

import de.kxmischesdomi.betterrecipebook.BetterRecipeBookMod;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
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

	@Inject(method = "keyPressed", at = @At("TAIL"), cancellable = true)
	public void keyPressedMixin(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
		if (BetterRecipeBookMod.recipeBookKeyBind.matches(i, j)) {

			for (Widget renderable : renderables) {
				if (renderable instanceof ImageButton imageButton) {
					if (imageButton.resourceLocation.toString().equals("minecraft:textures/gui/recipe_button.png")) {
						imageButton.onPress();
						break;
					}
				}

			}
			cir.setReturnValue(true);
		}
	}

}
