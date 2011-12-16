package net.minecraft.src;

import java.util.*;

public class BlockClevercraft extends Block implements ICraftingDescription {
	
	private int toptexture;
	
	public BlockClevercraft(int i)
	{
		super(i, Material.wood);
		this.blockIndexInTexture = Block.workbench.blockIndexInTexture;
		this.setBlockName("craftingtableii");
		toptexture = ModLoader.addOverride("/terrain.png", "/blockimage/clevercrafttop.png");
	}
	
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		if(!world.multiplayerWorld)
		{
			ModLoader.OpenGUI(entityplayer, new GuiClevercraft(entityplayer));
		}
		return true;
	}
	
	public int getBlockTextureFromSide(int i)
    {
        if(i == 1)
        {
            return toptexture;
        }
        if(i == 0)
        {
            return Block.planks.getBlockTextureFromSide(0);
        }
        if(i == 2 || i == 3)
        {
            return blockIndexInTexture + 1;
        } else
        {
            return Block.bookShelf.blockIndexInTexture;
        }
    }
	
	public String getDescription(int damageValue)
	{
		return "The crafting table of awesomeness that you are using right now! No need to remeber fiddly recipe patterns, the " +
				"crafting table II will figure it out all for you.\n\n" +
				"Shift click to craft as much of that item a possible.";
	}
	
}
