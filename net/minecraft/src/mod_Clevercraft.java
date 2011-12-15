package net.minecraft.src;

import java.io.*;
import java.util.List;
import java.util.Properties;
import au.com.bytecode.opencsv.CSVReader;

public class mod_Clevercraft extends BaseMod {
	
	@MLProp public static int blockIDCraftingTableII = 235;
	public static Properties itemDescriptions; 
	public static List itemDescriptions2;
	public static Block blockClevercraft;
	
	public mod_Clevercraft() {
		initBlocks();
		ModLoader.RegisterBlock(blockClevercraft);
		ModLoader.AddName(blockClevercraft, "Crafting Table II");
		ModLoader.AddShapelessRecipe(new ItemStack(blockClevercraft, 1), new Object[]{
			Block.workbench, Item.book
		});
		
		/*
		itemDescriptions = new Properties();
		try
		{
			itemDescriptions.load(new FileInputStream("config/ItemDescriptions.cfg"));
		} catch (Exception e){
			e.printStackTrace();
		}
		*/
		
		String fileName = ModLoader.getMinecraftInstance().getMinecraftDir()+"/config/ItemDescriptions.csv";
		try{
			CSVReader reader = new CSVReader(new FileReader(fileName));
			itemDescriptions2 = reader.readAll();
			
			ModLoader.getLogger().fine("ItemDescriptions.csv loaded");
		} catch(Exception e){
			
		}
	}
	
	public static void initBlocks()
	{
		blockClevercraft = new BlockClevercraft(blockIDCraftingTableII);
		System.out.println(blockClevercraft.blockID);
	}
	
	public static String getItemDescription(String itemName)
	{
		
		//return itemDescriptions.getProperty(itemName, "");
		for(int i = 0; i < itemDescriptions2.size(); i++)
		{
			String entry[] = (String[])itemDescriptions2.get(i);
			
			if(entry[0].equalsIgnoreCase(itemName) && entry.length >= 3)
			{
				return entry[2];
			}
		}
		
		return "";
	}

	@Override
	public String getVersion() {
		return "1.3.1";
	}

	@Override
	public void load() {

	}

}
