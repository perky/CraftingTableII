package net.minecraft.src;

public class ItemTestdesc extends Item implements ICraftingDescription {
	
	public ItemTestdesc(int i)
	{
		super(i);
	}
	
	public String getDescription(int i)
	{
		return "testdesc";
	}

}
