package amymialee.halfdoors.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class DoorSmithingRecipe extends SmithingRecipe {
	public DoorSmithingRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result) {
		super(id, base, addition, result);
	}

	public ItemStack craft(Inventory inventory) {
		ItemStack itemStack = this.result.copy();
		NbtCompound nbtCompound = inventory.getStack(0).getNbt();
		if (nbtCompound != null) {
			itemStack.getOrCreateNbt().copyFrom(nbtCompound);
		}
		return itemStack;
	}

	public static ItemStack getItemStack(JsonObject json) {
		String string = JsonHelper.getString(json, "item");
		Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
		if(json.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			int count = JsonHelper.getInt(json, "count", 1);
			String nbt = JsonHelper.getString(json, "nbt");
			ItemStack stack = new ItemStack(item, count);
			stack.getOrCreateNbt().putBoolean(nbt, true);
			return stack;
		}
	}

	public static class OpenSerializer implements RecipeSerializer<DoorSmithingRecipe> {
		public OpenSerializer() {
			super();
		}

		@Override
		public DoorSmithingRecipe read(Identifier identifier, JsonObject jsonObject) {
			Ingredient base = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
			Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "addition"));
			ItemStack result = DoorSmithingRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
			return new DoorSmithingRecipe(identifier, base, addition, result);
		}

		@Override
		public DoorSmithingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
			Ingredient base = Ingredient.fromPacket(packetByteBuf);
			Ingredient addition = Ingredient.fromPacket(packetByteBuf);
			ItemStack result = packetByteBuf.readItemStack();
			return new DoorSmithingRecipe(identifier, base, addition, result);
		}

		@Override
		public void write(PacketByteBuf packetByteBuf, DoorSmithingRecipe smithingRecipe) {
			smithingRecipe.base.write(packetByteBuf);
			smithingRecipe.addition.write(packetByteBuf);
			packetByteBuf.writeItemStack(smithingRecipe.result);
		}
	}
}