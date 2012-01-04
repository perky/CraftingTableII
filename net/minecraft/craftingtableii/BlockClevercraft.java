package net.minecraft.craftingtableii;

import java.util.*;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICraftingDescription;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_Clevercraft;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;

public class BlockClevercraft extends BlockContainer implements ICraftingDescription, ITextureProvider {
	
	private int toptexture;
	private String texturePath = "/blockimage/crafttableii_terrain.png";
	
	public BlockClevercraft(int i)
	{
		super(i, Material.wood);
		this.setBlockName("craftingtableii");
		this.blockIndexInTexture = 0;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1F, 1.0F);
		setLightOpacity(0);
		MinecraftForgeClient.preloadTexture(texturePath);
	}
	
	public String getTextureFile()
    {
        return texturePath;
    }
	
	public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        return getBlockTextureFromSide(i);
    }

    public int getBlockTextureFromSide(int i)
    {
        if(i == 0)
        {
            return blockIndexInTexture + 1;
        }
        if(i == 1)
        {
            return blockIndexInTexture + 2;
        } 
        if(i == 2)
        	return blockIndexInTexture;
        if(i == 3)
        	return blockIndexInTexture;
        else
        {
            return blockIndexInTexture + 1;
        }
    }
	
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		if(!world.multiplayerWorld)
		{
			ModLoader.OpenGUI(entityplayer, new GuiClevercraft(entityplayer));
		}
		return true;
	}
	
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
		int i1 = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		world.setBlockMetadata(i, j, k, i1);
		System.out.println(i1);
    }
	
	public TileEntity getBlockEntity() {
		return new TileEntityCraftingTableII();
	}
	
	public String getDescription(int damageValue)
	{
		return "The crafting table of awesomeness that you are using right now! No need to remeber fiddly recipe patterns, the " +
				"crafting table II will figure it out all for you.\n\n" +
				"Shift click to craft as much of that item a possible.";
	}
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	public int getRenderType()
	{
		return mod_Clevercraft.craftingTableModelID;
	}
	
}
