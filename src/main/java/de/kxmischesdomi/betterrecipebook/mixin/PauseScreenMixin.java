package de.kxmischesdomi.betterrecipebook.mixin;

import de.kxmischesdomi.betterrecipebook.RecipeCacheConfig;
import net.minecraft.client.gui.screens.PauseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PauseScreen.class)
public class PauseScreenMixin {

	@Inject(method = "init", at = @At("HEAD"))
	public void initMixin(CallbackInfo ci) {
		RecipeCacheConfig.saveCache();
	}

}
