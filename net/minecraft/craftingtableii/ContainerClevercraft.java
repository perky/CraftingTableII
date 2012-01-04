package net.minecraft.craftingtableii;

import java.util.*;

import net.minecraft.src.Container;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModLoaderMp;
import net.minecraft.src.Packet230ModLoader;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraft.src.Slot;
import net.minecraft.src.mod_Clevercraft;
import net.minecraft.src.forge.ForgeHooks;

public class ContainerClevercraft extends Container {
	
	public List itemList;
	public List recipes;
	public EntityPlayer thePlayer; 
	public List recipeList;
	public List collatedRecipes;
	public InventoryCrafting craftMatrix;
	public GuiClevercraft gui;
	public ItemStack lastItemCrafted = null;
	public IRecipe lastRecipeCrafted;
	private boolean isMultiplayer;
	private boolean isMuliplayer;
	
	public ContainerClevercraft(InventoryPlayer inventoryplayer)
    {
		itemList = new ArrayList();
		thePlayer = inventoryplayer.player;
		recipeList = Collections.unmodifiableList(CraftingManager.getInstance().getRecipeList());
		collatedRecipes = new ArrayList();
		craftMatrix = new InventoryCrafting(this, 3, 3);
		mod_Clevercraft.containerClevercraft = this;
		
		isMultiplayer = ModLoader.getMinecraftInstance().theWorld.multiplayerWorld;
		
        for(int l2 = 0; l2 < 5; l2++)
        {
            for(int j3 = 0; j3 < 8; j3++)
            {
            	addSlot(new SlotClevercraft(GuiClevercraft.getInventory(), inventoryplayer.player, this, craftMatrix, j3 + l2 * 8, 8 + j3 * 18, 18 + l2 * 18));
            }
        }

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(inventoryplayer, i1 + j * 9 + 9, 8 + i1 * 18, 125 + j * 18));
            }
        }
        
        for(int i3 = 0; i3 < 9; i3++)
        {
            addSlot(new Slot(inventoryplayer, i3, 8 + i3 * 18, 184));
        }

        populateContainer();
        func_35374_a(0.0F);
    }
	
	public void populateContainterWithDataInt(int[] dataInt)
	{
		System.out.println("populateContainterWithDataInt");
		int j = 0;
		for(int i = 0; i < dataInt.length; i += 3){
			ItemStack itemstack = new ItemStack(dataInt[i], dataInt[i+1], dataInt[i+2]);
			Slot slot = (Slot)inventorySlots.get(j);
			j++;
			
			slot.putStack(itemstack);
		}
	}
	
	public void populateContainer()
	{
		if(isMultiplayer)
			return;
		
		itemList.clear();
		try {
			recipes = getRecipeItems(recipeList, thePlayer.inventory);
			Slot slot;
			// Clear crafting slots.
			for(int i = 0; i < this.inventorySlots.size(); i++)
			{
				slot = (Slot)this.inventorySlots.get(i);
				if(slot instanceof SlotClevercraft)
					slot.putStack(null);
			}
			// Add recipes.
			for(int i = 0; i < recipes.size(); i++)
			{
				IRecipe irecipe = (IRecipe)recipes.get(i);
				itemList.add( irecipe.getCraftingResult(null) );
			}
			//Update screen.
			this.func_35374_a(0.0F);
		} catch (NoSuchFieldException e) {
			ModLoader.getLogger().warning("CleverCraft - NoSuchFieldException - Perhaps your minecraft version is incompatible.");
			e.printStackTrace();
		}
	}
	
	public void onPickupItem(ItemStack itemstack, SlotClevercraft slot)
	{
		if(isMuliplayer)
			return;
		
		if(slot.irecipe != null)
		{
			try {
				ItemStack itemstacks[] = getRecipeItemStackArray(slot.irecipe);
				takeRecipeItems(itemstacks, 1);
				mod_Clevercraft.addLastRecipeCrafted(slot.irecipe);
				mod_Clevercraft.clevercraftInstance.sendRecipePacket(slot.irecipe, 1);
				
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		
		onCraftingHook(itemstack, 1);
        gui.updateScreen();
        gui.resetScroll();
	}
	
	//TODO: remove me!
	private ItemStack[] getRecipeItemStackArray(IRecipe irecipe)
	{
		ItemStack itemstacks[];
		try
		{
			if(irecipe instanceof ShapedRecipes)
	        {
				itemstacks = (ItemStack[])ModLoader.getPrivateValue(ShapedRecipes.class, (ShapedRecipes)irecipe, 2);
	        }else
	    	{
	        	ArrayList recipeItems = new ArrayList((List)ModLoader.getPrivateValue(ShapelessRecipes.class, (ShapelessRecipes)irecipe, 1));
	        	itemstacks = (ItemStack[])recipeItems.toArray(new ItemStack[recipeItems.size()]);
	    	}
			return itemstacks;
		} catch(NoSuchFieldException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void takeRecipeItems(ItemStack itemstacks[], int multiplier) throws NoSuchFieldException
	{
		for(int i = 0; i<9; i++)
			craftMatrix.setInventorySlotContents(i, null);
		
		for(int n = 0; n < itemstacks.length; n++)
    	{
			ItemStack itemstack1 = itemstacks[n];
			if(itemstack1 != null)
			{
				craftMatrix.setInventorySlotContents(n, itemstack1);	
				int count = multiplier;
				
				ItemStack itemstacks1[] = thePlayer.inventory.mainInventory;
				for(int n1 = 0; n1 < itemstacks1.length; n1++)
				{
					ItemStack itemstack2 = itemstacks1[n1];
					if(itemstack2 != null && itemstack2.itemID == itemstack1.itemID 
							&& (itemstack2.getItemDamage() == itemstack1.getItemDamage() || itemstack1.getItemDamage() == -1))
					{
						if(itemstack2.stackSize >= multiplier)
						{
							thePlayer.inventory.decrStackSize(n1, count);
							count = 0;
							break;
						} else {
							thePlayer.inventory.decrStackSize(n1, itemstack2.stackSize);
							count -= itemstack2.stackSize;
						}
					}
				}
			}
    	}
	}
	
	private void takeMaxRecipeItems(IRecipe irecipe, Map<Integer, Integer[]> collatedRecipe) throws NoSuchFieldException
	{
		if(collatedRecipe == null)
			return;
		
		int minStack = 9999;
		boolean flag = true;
		ItemStack recipeItems[] = getRecipeItemStackArray(irecipe);
		for(int i = 0; i < recipeItems.length; i++)
		{
			ItemStack itemstack1 = recipeItems[i];
			if(itemstack1 != null && itemstack1.getMaxStackSize() == 1)
			{
				minStack = 1;
				flag = false;
				break;
			}
		}
		
		if(flag)
		{
			for (Map.Entry<Integer, Integer[]> entry : collatedRecipe.entrySet())
			{
				int itemCount = 0;
				int itemid = entry.getKey();
				int stacksize = entry.getValue()[0];
				int damageval = entry.getValue()[1];
				for(int i = 0; i < thePlayer.inventory.getSizeInventory(); i++)
				{
					ItemStack itemstack = thePlayer.inventory.getStackInSlot(i);
					if(itemstack != null && itemstack.itemID == itemid && (itemstack.getItemDamage() == damageval || damageval == -1))
					{
						itemCount += itemstack.stackSize;
					}
				}
				int stackDivision = MathHelper.floor_double(itemCount/stacksize);
				minStack = Math.min(minStack, stackDivision);
			}
			
			if(minStack >= 9999 || minStack == 0)
				minStack = 1;
		}
		
		//Get output item.
		ItemStack outputstack = irecipe.getRecipeOutput();
		
		//Multiply output.
		int maxStackSize = outputstack.stackSize*minStack;
		
		//Limit to max stack size.
		if(maxStackSize > outputstack.getMaxStackSize())
		{
			minStack = MathHelper.floor_double(outputstack.getMaxStackSize() / outputstack.stackSize);
			maxStackSize = outputstack.getMaxStackSize();
		}
		
		//Add item to inventory.
		ItemStack itemstack = new ItemStack(outputstack.itemID, outputstack.stackSize*minStack, outputstack.getItemDamage());
		boolean canAddItemStack = canAddItemStackToPlayerInventory(itemstack, thePlayer.inventory);
		
		//if item was added.
		if(canAddItemStack) {
			// Add the item.
			thePlayer.inventory.addItemStackToInventory(itemstack);
			// Take recipe items.
			takeRecipeItems(recipeItems, minStack);
			//Send mod hooks.
			onCraftingHook(itemstack, minStack);
		}
		
		//Update container and gui.
		populateContainer();
		gui.updateScreen();
	}
	
	private boolean canAddItemStackToPlayerInventory(ItemStack itemstack, InventoryPlayer inventory) {
		int emptySlot = getFirstEmptyStack(inventory);
		
		if(!itemstack.isItemDamaged())
        {
			if(itemstack.getMaxStackSize() == 1)
	        {
	            if(emptySlot < 0)
	            {
	                return false;
	            } else {
	            	return true;
	            }
	        }
	        
	        for(int i = 0; i < inventory.mainInventory.length; i++)
	        {
	        	ItemStack itemstack1 = inventory.getStackInSlot(i);
	        	if(itemstack1 != null && itemstack1.itemID == itemstack.itemID 
	        			&& itemstack1.getItemDamage() == itemstack.getItemDamage()
	        			&& (itemstack1.stackSize + itemstack.stackSize) < itemstack1.getMaxStackSize() ) {
	        		return true;
	        	}
	        }
        }
		
		if(emptySlot >= 0)
        	return true;
		
		return false;
	}

	private int getFirstEmptyStack(InventoryPlayer inventory)
    {
        for(int i = 0; i < inventory.mainInventory.length; i++)
        {
            if(inventory.mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }
	
	public void onCraftingHook(ItemStack itemstack, int multiplier)
	{
		int stacksizes[] = new int[craftMatrix.getSizeInventory()];
        for(int i1 = 0; i1 < craftMatrix.getSizeInventory(); i1++)
        {
        	ItemStack itemstack1 = craftMatrix.getStackInSlot(i1);
        	if(itemstack1 != null)
        		stacksizes[i1] = itemstack1.stackSize;
        }
        
        ModLoader.TakenFromCrafting(thePlayer, itemstack, craftMatrix);
		ForgeHooks.onTakenFromCrafting(thePlayer, itemstack, craftMatrix);
		
		for(int i = 0; i < 9; i++)
		{
			ItemStack itemstack1 = craftMatrix.getStackInSlot(i);
			if(itemstack1 != null)
			{
				craftMatrix.decrStackSize(i, 1);
				ItemStack itemstack2 = craftMatrix.getStackInSlot(i);
				if(itemstack1.getItem().hasContainerItem())
				{
					ItemStack itemstack3 = new ItemStack(itemstack1.getItem().getContainerItem());
					itemstack3.stackSize *= multiplier;
					thePlayer.inventory.addItemStackToInventory(itemstack3);
				} else if(itemstack2 != null && itemstack2.stackSize >= stacksizes[i])
				{
					thePlayer.inventory.addItemStackToInventory(itemstack2.copy());
				}
			}
		}
	}
	
	private List getRecipeItems(List recipes, InventoryPlayer inventory) throws NoSuchFieldException
	{
		ArrayList recipeItems;
		ItemStack itemstacks[];
		ArrayList craftableRecipes = new ArrayList<IRecipe>();
		collatedRecipes.clear();
		boolean canCraft;
		
		// Add last item crafted.
		int j = 0;
		for(int n = 0; n < mod_Clevercraft.numberOfLastRecipesCrafted; n++) {
			Map<Integer, Integer[]> collatedRecipe = new HashMap<Integer, Integer[]>();
			IRecipe recipe = mod_Clevercraft.lastRecipesCrafted[n];
			if(recipe instanceof ShapelessRecipes && canCraftShapelessRecipe(recipe, inventory, collatedRecipe))
            {
            	craftableRecipes.add(recipe);
            	collatedRecipes.add(collatedRecipe);
            } else if(recipe instanceof ShapedRecipes && canCraftShapedRecipe(recipe, inventory, collatedRecipe))
            {
            	craftableRecipes.add(recipe);
            	collatedRecipes.add(collatedRecipe);
            }
			j++;
		}
		
		// Add all other craftable recipes.
		for(int n = j; n < recipes.size(); n++)
        {
            IRecipe irecipe = (IRecipe)recipes.get(n);
            //ArrayList collatedRecipe = new ArrayList();
            Map<Integer, Integer[]> collatedRecipe = new HashMap<Integer, Integer[]>();
            if(irecipe instanceof ShapelessRecipes && canCraftShapelessRecipe(irecipe, inventory, collatedRecipe))
            {
            	craftableRecipes.add(irecipe);
            	collatedRecipes.add(collatedRecipe);
            } else if(irecipe instanceof ShapedRecipes && canCraftShapedRecipe(irecipe, inventory, collatedRecipe))
            {
            	craftableRecipes.add(irecipe);
            	collatedRecipes.add(collatedRecipe);
            }
        }
		
		return craftableRecipes;
	}
	
	private boolean canCraftShapedRecipe(IRecipe irecipe, InventoryPlayer inventory, Map<Integer, Integer[]> collatedRecipe) throws NoSuchFieldException
	{
		ItemStack itemstacks[] = this.getRecipeItemStackArray(irecipe);
    	boolean canCraft = true;

    	// Collate each item together to get total stack size per item.
    	for(int i = 0; i < itemstacks.length; i++)
    	{
    		if(itemstacks[i] != null)
    		{
        		if(collatedRecipe.containsKey(itemstacks[i].itemID))
        		{
        			Integer vals[] = (Integer[])collatedRecipe.get(itemstacks[i].itemID);
        			vals[0] += 1; 
        			collatedRecipe.put(itemstacks[i].itemID, vals);
        		} else {
        			Integer vals[] = {1, itemstacks[i].getItemDamage()};
        			collatedRecipe.put((Integer)itemstacks[i].itemID, vals);
        		}
    		}
    	}
    	
    	for(int i = 0; i < itemstacks.length; i++)
    	{
    		if(itemstacks[i] != null && !getInventoryHasItemStack(inventory, itemstacks[i], collatedRecipe))
    		{
    			canCraft = false;
    			break;
    		}
    	}
    	
    	return canCraft;
	}
	
	private boolean canCraftShapelessRecipe(IRecipe irecipe, InventoryPlayer inventory, Map<Integer, Integer[]> collatedRecipe) throws NoSuchFieldException
	{
		ArrayList recipeItems = new ArrayList((List)ModLoader.getPrivateValue(ShapelessRecipes.class, (ShapelessRecipes)irecipe, 1));
    	boolean canCraft = true;
    	
    	// Collate each item together to get total stack size per item.
    	for(int i = 0; i < recipeItems.size(); i++)
    	{
    		ItemStack itemstack = (ItemStack)recipeItems.get(i);
    		if(itemstack != null)
    		{
	    		if(collatedRecipe.containsKey(itemstack.itemID))
	    		{
	    			Integer vals[] = (Integer[])collatedRecipe.get(itemstack.itemID);
	    			vals[0] += 1; 
	    			collatedRecipe.put((Integer)itemstack.itemID, vals);
	    		} else {
	    			Integer vals[] = {1, itemstack.getItemDamage()};
	    			collatedRecipe.put((Integer)itemstack.itemID, vals);
	    		}
    		
	    		if(!getInventoryHasItemStack(inventory, itemstack, collatedRecipe))
	    		{
	    			canCraft = false;
	    			break;
	    		}
    		}
    	}
        
        return canCraft;
	}
	
	public static boolean getInventoryHasItemStack(InventoryPlayer inventory, ItemStack itemstack, Map<Integer, Integer[]> collatedRecipe)
    {
		ItemStack mInv[] = inventory.mainInventory;
		ArrayList items = new ArrayList<ItemStack>();
		int stacksize = 0;
        for(int j = 0; j < mInv.length; j++)
        {
            if(mInv[j] != null && mInv[j].itemID == itemstack.itemID
            		&& (mInv[j].getItemDamage() == itemstack.getItemDamage() || itemstack.getItemDamage() == -1))
            {
            	items.add(mInv[j]);
            	stacksize += mInv[j].stackSize;
            }
        }
        
        if(collatedRecipe.containsKey(itemstack.itemID))
        {
        	Integer vals[] = (Integer[])collatedRecipe.get(itemstack.itemID);
	        int stacksize1 = vals[0];
	        if(stacksize >= stacksize1)
	        {
	        	return true;
	        }
        }

        return false;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

    public void func_35374_a(float f)
    {
        int i = (itemList.size() / 8 - 4) + 1;
        int j = (int)((double)(f * (float)i) + 0.5D);
        if(j < 0)
        {
            j = 0;
        }
        Slot slot;
        for(int k = 0; k < 5; k++)
        {
            for(int l = 0; l < 8; l++)
            {
                int i1 = l + (k + j) * 8;
                if(i1 >= 0 && i1 < itemList.size())
                {
                	GuiClevercraft.getInventory().setInventorySlotContents(l + k * 8, (ItemStack)itemList.get(i1));
                	slot = (Slot)inventorySlots.get(l + k * 8);
                	if(slot instanceof SlotClevercraft)
                	{
                		((SlotClevercraft)slot).collatedRecipe = (HashMap)collatedRecipes.get(i1);
                		((SlotClevercraft)slot).irecipe = (IRecipe)recipes.get(i1);
                	}
                } else
                {
                	GuiClevercraft.getInventory().setInventorySlotContents(l + k * 8, null);
                	slot = (Slot)inventorySlots.get(l + k * 8);
                	if(slot instanceof SlotClevercraft)
                	{
                		((SlotClevercraft)slot).collatedRecipe = null;
                		((SlotClevercraft)slot).irecipe = null;
                	}
                }
            }
        }
    }

    protected void func_35373_b(int i, int j, boolean flag, EntityPlayer entityplayer)
    {
    }
    
    public ItemStack slotClick(int i, int j, boolean flag, EntityPlayer entityplayer)
    {
    	// i: Slot number.
    	// j: Mouse buttons, 0 = left, 1 = right.
    	// flag: Shift button down.
    	if(j == 1 && inventorySlots.get(i) instanceof SlotClevercraft)
    		return null;
    	if(i < 40 && flag && !isMultiplayer)
    	{
    		SlotClevercraft slot = (SlotClevercraft)inventorySlots.get(i);
    		if(slot.getHasStack())
    		{
    			try{
    				takeMaxRecipeItems(slot.irecipe, slot.collatedRecipe);
    			} catch (NoSuchFieldException e) {
    				e.printStackTrace();
    			}
    		}
    		return null;
    	}
    	return super.slotClick(i, j, flag, entityplayer);
    }

}
