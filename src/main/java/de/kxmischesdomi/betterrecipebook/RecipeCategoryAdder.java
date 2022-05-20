package de.kxmischesdomi.betterrecipebook;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RecipeCategoryAdder implements Runnable {

	@Override
	public void run() {
		MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

		String recipeBookCategories = remapper.mapClassName("intermediary", "net.minecraft.class_314");
		String itemstack = "[L" + remapper.mapClassName("intermediary", "net.minecraft.class_1799") + ';';
		ClassTinkerers.enumBuilder(recipeBookCategories, itemstack).addEnum("HISTORY", () -> new Object[] { new ItemStack[] {new ItemStack(Items.CLOCK)} }).build();
	}

}
