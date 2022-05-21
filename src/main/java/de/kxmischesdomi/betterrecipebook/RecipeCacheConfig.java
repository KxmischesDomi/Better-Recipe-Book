package de.kxmischesdomi.betterrecipebook;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RecipeCacheConfig {

	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

	public static File getConfigFile() {
			return new File(FabricLoader.getInstance().getConfigDir().toFile(), BetterRecipeBookMod.MOD_ID + ".json");
	}

	public static void loadCache(List<RecipeCollection> allCollections) {
		getConfigFile();

		Map<String, RecipeCollection> collections = Maps.newHashMap();
		for (RecipeCollection collection : allCollections) {
			collections.put(getName(collection), collection);
		}

		File file = getConfigFile();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			JsonObject json = new JsonParser().parse(br).getAsJsonObject();

			for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
				try {
					String stationName = entry.getKey();
					RecipeBookType type = RecipeBookType.valueOf(stationName);

					JsonObject value = (JsonObject) entry.getValue();
					boolean visible = value.getAsJsonPrimitive("visible").getAsBoolean();
					String search = value.getAsJsonPrimitive("search").getAsString();
					int page = value.getAsJsonPrimitive("page").getAsInt();
					String categoryName = value.getAsJsonPrimitive("category").getAsString();
					RecipeBookCategories category = RecipeBookCategories.valueOf(categoryName);

					JsonArray history = value.getAsJsonArray("history");

					List<RecipeCollection> loadedHistory = new LinkedList<>();

					for (JsonElement element : history) {
						String name = element.getAsString();
						RecipeCollection collection = collections.get(name);

						if (collection != null) {
							loadedHistory.add(collection);
						}

					}

					RecipeBookCache cache = new RecipeBookCache(category);
					cache.visible = visible;
					cache.search = search;
					cache.page = page;
					cache.history = loadedHistory;
					BetterRecipeBookMod.BOOK_CACHE.put(type, cache);

				} catch (Exception exception) {
					exception.printStackTrace();
				}

			}

		} catch (Exception exception) {
			try (FileWriter fileWriter = new FileWriter(file)) {
				fileWriter.write("{}"); // reset cache if anything happens
			} catch (IOException e) {
				System.err.println("Couldn't save configuration file");
				e.printStackTrace();
			}
			exception.printStackTrace();
		}

	}

	public static void saveCache() {
		File file = getConfigFile();
		JsonObject json = new JsonObject();

		for (Map.Entry<RecipeBookType, RecipeBookCache> entry : BetterRecipeBookMod.BOOK_CACHE.entrySet()) {

			JsonObject typeObject = new JsonObject();
			RecipeBookCache cache = entry.getValue();

			typeObject.addProperty("search", cache.search);
			typeObject.addProperty("page", cache.page);
			typeObject.addProperty("category", cache.category.name());
			typeObject.addProperty("visible", cache.visible);

			JsonArray history = new JsonArray();

			for (RecipeCollection collection : cache.history) {
				history.add(getName(collection));
			}

			typeObject.add("history", history);

			json.add(entry.getKey().name(), typeObject);
		}

		String jsonString = GSON.toJson(json);

		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(jsonString);
		} catch (IOException e) {
			System.err.println("Couldn't save configuration file");
			e.printStackTrace();
		}

	}

	public static String getName(RecipeCollection collection) {

		List<Recipe<?>> recipes = collection.getRecipes();

		if (!recipes.isEmpty()) {
			Recipe<?> recipe = recipes.get(0);
			String name = recipe.getGroup();
			if (name.isEmpty()) {
				name = recipe.getId().toString();
			}
			return name;
		}

		return "";
	}

}
