package net.minecraft.src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.*;

import net.minecraft.client.Minecraft;
import au.com.bytecode.opencsv.CSVReader;

public class mod_Clevercraft extends BaseMod {
	
	@MLProp public static int blockIDCraftingTableII = 235;
	public static Properties itemDescriptions; 
	public static List itemDescriptions2;
	public static Block blockClevercraft;
	private static final File descriptionDir = new File(Minecraft.getMinecraftDir(), "/config/itemdescriptions/");
	
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
		
		
		
		
		try{
			String vanillaFile = ModLoader.getMinecraftInstance().getMinecraftDir()+"/config/itemdescriptions/vanilla.csv";
			CSVReader reader = new CSVReader(new FileReader(vanillaFile));
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
			
		}
	}
	
	public static void initBlocks()
	{
		blockClevercraft = new BlockClevercraft(blockIDCraftingTableII);
		System.out.println(blockClevercraft.blockID);
	}
	
	
	private static Pattern descriptionPattern = Pattern.compile("(.*\\..*)\\.(.*)\\.(.*)");
	private static Matcher matcher;
	private static String lastItemName;
	private static String lastItemDescription;
	private static int lastDataVal;
	public static String getItemDescription(String itemName, int dataval)
	{
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
					if(matcher.group(1).equalsIgnoreCase(itemName))
					{
						System.out.println(matcher.group());
					}
					if(matcher.group(1).equalsIgnoreCase(itemName) 
							&& (matcher.group(2).equalsIgnoreCase(Integer.toString(dataval)) || matcher.group(2).equalsIgnoreCase("*"))
							&& matcher.group(3).equalsIgnoreCase("1"))
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
