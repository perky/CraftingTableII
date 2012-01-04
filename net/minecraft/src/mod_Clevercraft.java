package net.minecraft.src;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.craftingtableii.BlockClevercraft;
import net.minecraft.craftingtableii.ContainerClevercraft;
import net.minecraft.craftingtableii.GuiClevercraft;
import net.minecraft.craftingtableii.RenderCraftingTableII;
import net.minecraft.src.forge.ICraftingHandler;
import net.minecraft.src.forge.MinecraftForge;
import au.com.bytecode.opencsv.CSVReader;

public class mod_Clevercraft extends BaseModMp {
	
	@MLProp public static int blockIDCraftingTableII = 235; 
	@MLProp public static int guiIDCraftingTableII = 235;
	@MLProp public static boolean shouldShowDescriptions = true;
	
	public static Block blockClevercraft;
	public static Class<?> guiDescriptions;
	public static IRecipe[] lastRecipesCrafted;
	public static int numberOfLastRecipesCrafted;
	public static int craftingTableModelID;
	
	public static mod_Clevercraft clevercraftInstance;
	public static ContainerClevercraft containerClevercraft;
	
	public mod_Clevercraft() {
		
		clevercraftInstance = this;
		
		craftingTableModelID = ModLoader.getUniqueBlockModelID(this, true);
		lastRecipesCrafted = new IRecipe[8];
		numberOfLastRecipesCrafted = 0;
		
		try{
			Field[] ff = GuiContainer.class.getFields();
			for(int i = 0; i < ff.length; i++)
			{
				if(ff[i].getName().equalsIgnoreCase("itemDescriptionsEnabled"))
					shouldShowDescriptions = false;
			}
		} catch(Exception e){}
		
		initBlocks();
		
		ModLoader.RegisterBlock(blockClevercraft);
		ModLoader.AddName(blockClevercraft, "Crafting Table II");
		ModLoader.AddShapelessRecipe(new ItemStack(blockClevercraft, 1), new Object[]{
			Block.workbench, Item.book
		});
		
		// Setup block render.
		RenderCraftingTableII render = new RenderCraftingTableII();
		ModLoader.RegisterTileEntity(net.minecraft.craftingtableii.TileEntityCraftingTableII.class, "craftingtableII", render);

		ModLoaderMp.RegisterGUI(this, guiIDCraftingTableII);
		
		try {
			guiDescriptions = Class.forName("net.minecraft.src.GuiItemDescriptions");
		} catch (Exception e) {
			shouldShowDescriptions = false;
			e.printStackTrace();
		}
		
	}
	
	public void sendRecipePacket(IRecipe recipe, int amount)
	{
		int[] dataInt = new int[2];
		dataInt[0] = amount;
		dataInt[1] = recipe.getRecipeOutput().itemID;
		
		Packet230ModLoader packet = new Packet230ModLoader();
		packet.dataInt = dataInt;
		packet.packetType = 0;
		ModLoaderMp.SendPacket(this, packet);
	}
	
	public void HandlePacket(Packet230ModLoader packet)
	{
		switch(packet.packetType){
		case 0:
			if(packet.dataInt != null && packet.dataInt.length > 0 && containerClevercraft != null)
				containerClevercraft.populateContainterWithDataInt(packet.dataInt);
			break;
		}
	}
	
	public static void initBlocks()
	{
		blockClevercraft = new BlockClevercraft(blockIDCraftingTableII);
	}
	
	public GuiScreen HandleGUI(int inventoryType) 
    {
            if(inventoryType == guiIDCraftingTableII)
                    return new GuiClevercraft( ModLoader.getMinecraftInstance().thePlayer );
            else return null;
    }
	
	public void RenderInvBlock(RenderBlocks renderblocks, Block block, int i, int j)
    {
		if(block.getRenderType() == craftingTableModelID) {
			Tessellator tessellator = Tessellator.instance;
			block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, i));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, i));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, i));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, i));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, i));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, i));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
    }
	
	public static void addLastRecipeCrafted(IRecipe recipe) {
		//Check if recipe is already in list.
		for(int i = 0; i < 8; i++) {
			IRecipe recipe1 = lastRecipesCrafted[i];
			if(recipe1 != null && recipe1.equals(recipe)){
				return;
			}
		}
		
		for(int i = 6; i >= 0; i--) {
			IRecipe recipe1 = lastRecipesCrafted[i];
			if(recipe1 != null) {
				lastRecipesCrafted[i+1] = recipe1;
			}
		}
		
		lastRecipesCrafted[0] = recipe;
		numberOfLastRecipesCrafted++;
		if(numberOfLastRecipesCrafted > 8)
			numberOfLastRecipesCrafted = 8;
	}

	@Override
	public String getVersion() {
		return "1.6";
	}

	@Override
	public void load() {

	}

}
