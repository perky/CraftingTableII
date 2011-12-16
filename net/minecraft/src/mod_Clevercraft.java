package net.minecraft.src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.*;

import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.ICraftingHandler;
import net.minecraft.src.forge.MinecraftForge;
import au.com.bytecode.opencsv.CSVReader;

public class mod_Clevercraft extends BaseModMp {
	
	@MLProp public static int blockIDCraftingTableII = 235; 
	@MLProp public static int guiIDCraftingTableII = 235;
	@MLProp public static boolean shouldShowDescriptions = true;
	
	public static List itemDescriptions2;
	public static Block blockClevercraft;
	private static final File descriptionDir = new File(Minecraft.getMinecraftDir(), "/config/itemdescriptions/");
	private static final File vanillaCsv = new File(Minecraft.getMinecraftDir(), "/config/itemdescriptions/vanilla.csv");
	
	public mod_Clevercraft() {
		initBlocks();
		ModLoader.RegisterBlock(blockClevercraft);
		ModLoader.AddName(blockClevercraft, "Crafting Table II");
		ModLoader.AddShapelessRecipe(new ItemStack(blockClevercraft, 1), new Object[]{
			Block.workbench, Item.book
		});

		ModLoaderMp.RegisterGUI(this, guiIDCraftingTableII);
		
		try{
			CSVReader reader = new CSVReader(new FileReader(vanillaCsv));
			itemDescriptions2 = reader.readAll();
			ModLoader.getLogger().fine("Vanilla.csv descriptions loaded");
			
			File file[] = descriptionDir.listFiles();
			for(int i = 0; i < file.length; i++)
			{
				File file1 = file[i];
				
				if(!file1.getName().equalsIgnoreCase("vanilla.csv") && file1.getName().endsWith(".csv"))
				{
					System.out.println(file1.getName());
					reader = new CSVReader(new FileReader(file1));
					itemDescriptions2.addAll(reader.readAll());
					ModLoader.getLogger().fine(file1.getName()+" descriptions loaded");
				}
			}
		} catch(Exception e){
			e.printStackTrace();
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
	
	
	private static Pattern descriptionPattern = Pattern.compile("(.*\\..*)\\.(.*)\\.(.*)");
	private static Pattern rangePattern = Pattern.compile("([0-9]*)-([0-9]*)");
	private static Matcher matcher;
	private static Matcher matcher1;
	private static String lastItemName;
	private static String lastItemDescription;
	private static int lastDataVal;
	
	public static String getItemDescription(String itemName, int dataval)
	{
		if(itemDescriptions2 == null || itemDescriptions2.size() == 0)
			return "";
		
		if(lastItemName != null && itemName == lastItemName && dataval == lastDataVal)
		{
			return lastItemDescription;
		}
		
		for(int i = 0; i < itemDescriptions2.size(); i++)
		{
			String entry[] = (String[])itemDescriptions2.get(i);
			if(entry.length >= 3)
			{
				matcher = descriptionPattern.matcher(entry[0]);
				while (matcher.find()) {
					if(matcher.group(1).equalsIgnoreCase(itemName) && matcher.group(3).equalsIgnoreCase("1"))
					{
						matcher1 = rangePattern.matcher(matcher.group(2));
						while(matcher1.find()) {
							int low = Integer.parseInt(matcher1.group(1));
							int high = Integer.parseInt(matcher1.group(2));
							if(dataval >= low && dataval <= high)
							{
								lastItemName = itemName;
								lastDataVal = dataval;
								lastItemDescription = entry[2];
								return entry[2];
							}
						}
						
						if(matcher.group(2).equalsIgnoreCase(Integer.toString(dataval)) || matcher.group(2).equalsIgnoreCase("*"))
						{
							lastItemName = itemName;
							lastDataVal = dataval;
							lastItemDescription = entry[2];
							return entry[2];
						} else {
							break;
						}
					}
				}
			}
		}
		
		return "";
	}

	@Override
	public String getVersion() {
		return "1.4.2";
	}

	@Override
	public void load() {

	}

}
