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
	}

	@Override
	public String getVersion() {
		return "1.1";
	}

	@Override
	public void load() {

	}

}
