package de.kxmischesdomi.betterrecipebook.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.betterrecipebook.BetterRecipeBookMod;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class SearchClearButton extends Button {

	private static final ResourceLocation xIcon = new ResourceLocation(BetterRecipeBookMod.MOD_ID, "textures/x.png");

	public SearchClearButton(int i, int j, OnPress onPress) {
		super(i, j, 7, 7, TextComponent.EMPTY, onPress);
	}

	@Override
	public void renderToolTip(PoseStack poseStack, int i, int j) {

	}

	@Override
	public void renderButton(PoseStack poseStack, int i, int j, float f) {
		poseStack.pushPose();
		RenderSystem.disableDepthTest();
		RenderSystem.setShaderTexture(0, xIcon);
		int g = 0;
		if (isHovered) {
			g = 7;
		}
		blit(poseStack, this.x, this.y, 0, g, this.width, this.height, this.width, this.height * 2);
		RenderSystem.enableDepthTest();
		poseStack.popPose();
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationElementOutput) {
		this.defaultButtonNarrationText(narrationElementOutput);
	}

}
