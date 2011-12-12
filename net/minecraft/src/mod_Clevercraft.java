package net.minecraft.src;

public class mod_Clevercraft extends BaseMod {
	
	@MLProp public static int blockClevercraftId = 235;
	public static Block blockClevercraft = new BlockClevercraft(blockClevercraftId);
	
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
	}

	@Override
	public String getVersion() {
		return "1.2";
	}

	@Override
	public void load() {

	}

}
