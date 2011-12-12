package net.minecraft.src;

public class mod_Clevercraft extends BaseMod {
	
	@MLProp public static int blockIDCraftingTableII = 235;
	
	public static Block blockClevercraft;
	static
	{
		blockClevercraft = new BlockClevercraft(blockIDCraftingTableII);
	}
	
	public mod_Clevercraft() {
		ModLoader.RegisterBlock(blockClevercraft);
		ModLoader.AddName(blockClevercraft, "Crafting Table II");
		ModLoader.AddShapelessRecipe(new ItemStack(blockClevercraft, 1), new Object[]{
			Block.workbench, Item.book
		});
		// I have to re add these recipes for some reason.
		ModLoader.AddRecipe(new ItemStack(Block.blockDiamond, 1), new Object[]{
			"###", "###", "###",
			Character.valueOf('#'), Item.diamond
		});
		ModLoader.AddRecipe(new ItemStack(Block.blockGold, 1), new Object[]{
			"###", "###", "###",
			Character.valueOf('#'), Item.ingotGold
		});
		ModLoader.AddRecipe(new ItemStack(Block.blockSteel, 1), new Object[]{
			"###", "###", "###",
			Character.valueOf('#'), Item.ingotIron
		});
		ModLoader.AddRecipe(new ItemStack(Block.blockLapis, 1), new Object[]{
			"###", "###", "###",
			Character.valueOf('#'), new ItemStack(Item.dyePowder, 9, 4)
		});
	}

	@Override
	public String getVersion() {
		return "1.3";
	}

	@Override
	public void load() {

	}

}
