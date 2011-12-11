package net.minecraft.src;

import java.util.*;

public class ContainerClevercraft extends Container {
	
	public List itemList;
	public List recipes;
	public EntityPlayer thePlayer; 
	public List recipeList;
	public List collatedRecipes;
	
	public ContainerClevercraft(EntityPlayer entityplayer)
    {
		itemList = new ArrayList();
		thePlayer = entityplayer;
		recipeList = Collections.unmodifiableList(CraftingManager.getInstance().getRecipeList());

		
		InventoryPlayer inventoryplayer = entityplayer.inventory;
        for(int l2 = 0; l2 < 5; l2++)
        {
            for(int j3 = 0; j3 < 8; j3++)
            {
            	addSlot(new SlotClevercraft(GuiClevercraft.getInventory(), entityplayer, this, j3 + l2 * 8, 8 + j3 * 18, 18 + l2 * 18));
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
	
	public void populateContainer()
	{
		itemList.clear();
		try {
			recipes = getRecipeItems(recipeList, thePlayer.inventory);
			Slot slot;
			for(int i = 0; i < this.inventorySlots.size(); i++)
			{
				slot = (Slot)this.inventorySlots.get(i);
				if(slot instanceof SlotClevercraft)
					slot.putStack(null);
			}
			for(int i = 0; i < recipes.size(); i++)
			{
				IRecipe irecipe = (IRecipe)recipes.get(i);
				itemList.add( irecipe.getCraftingResult(null) );
				//slot = (Slot)this.inventorySlots.get(i);
				//if(slot instanceof SlotClevercraft)
					//slot.putStack(irecipe.getCraftingResult(null));
			}
			//Update screen.
			this.func_35374_a(0.0F);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			ModLoader.getLogger().warning("CleverCraft - NoSuchFieldException - Perhaps your minecraft version is incompatible.");
			e.printStackTrace();
		}
	}
	
	public void onPickupItem(ItemStack itemstack, int slotnumber)
	{
		//find recipe.
		IRecipe irecipe = (IRecipe)recipes.get(slotnumber);
		if(irecipe != null)
		{
			try {
				takeRecipeItems(irecipe);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
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
	
	private void takeRecipeItems(IRecipe irecipe) throws NoSuchFieldException
	{
		ItemStack itemstacks[] = getRecipeItemStackArray(irecipe);
		
		for(int n = 0; n < itemstacks.length; n++)
    	{
			ItemStack itemstack = itemstacks[n];
			if(itemstack != null)
			{
				int count = itemstack.stackSize;
				ItemStack itemstacks1[] = thePlayer.inventory.mainInventory;
				for(int n1 = 0; n1 < itemstacks1.length; n1++)
				{
					ItemStack itemstack1 = itemstacks1[n1];
					if(itemstack1 != null && itemstack1.itemID == itemstack.itemID 
							&& (itemstack1.getItemDamage() == itemstack.getItemDamage() || itemstack.getItemDamage() == -1))
					{
						if(itemstack1.stackSize >= count)
						{
							thePlayer.inventory.decrStackSize(n1, count);
							count = 0;
							break;
						} else {
							thePlayer.inventory.decrStackSize(n1, itemstack1.stackSize);
							count -= itemstack1.stackSize;
						}
					}
				}
			}
    	}
		
		populateContainer();
	}
	
	private List getRecipeItems(List recipes, InventoryPlayer inventory) throws NoSuchFieldException
	{
		ArrayList recipeItems;
		ItemStack itemstacks[];
		ArrayList craftableRecipes = new ArrayList<IRecipe>();
		collatedRecipes.clear();
		boolean canCraft;
		for(int n = 0; n < recipes.size(); n++)
        {
            IRecipe irecipe = (IRecipe)recipes.get(n);
            //ArrayList collatedRecipe = new ArrayList();
            //HashMap collatedRecipe = new HashMap();
            if(irecipe instanceof ShapelessRecipes && canCraftShapelessRecipe(irecipe, inventory, collatedRecipe))
            {
            	craftableRecipes.add(irecipe);
            	collatedRecipes.add(collatedRecipe);
            	//System.out.println("You can shapeless craft: "+irecipe.getRecipeOutput().toString());
            } else if(irecipe instanceof ShapedRecipes && canCraftShapedRecipe(irecipe, inventory, collatedRecipe))
            {
            	craftableRecipes.add(irecipe);
            	collatedRecipes.add(collatedRecipe);
            	//System.out.println("You can shaped craft: "+irecipe.getRecipeOutput().toString());
            }
        }
		
		return craftableRecipes;
	}
	
	private boolean canCraftShapedRecipe(IRecipe irecipe, InventoryPlayer inventory, HashMap collatedRecipe) throws NoSuchFieldException
	{
		ItemStack itemstacks[] = (ItemStack[])ModLoader.getPrivateValue(ShapedRecipes.class, (ShapedRecipes)irecipe, 2);
    	boolean canCraft = true;

    	// Collate each item together to get total stack size per item.
    	//HashMap collatedRecipe = new HashMap();
    	for(int n1 = 0; n1 < itemstacks.length; n1++)
    	{
    		if(itemstacks[n1] != null)
    		{
        		if(collatedRecipe.containsKey(itemstacks[n1].itemID))
        		{
        			int stacksize = (Integer)collatedRecipe.get(itemstacks[n1].itemID);
        			collatedRecipe.put(itemstacks[n1].itemID, itemstacks[n1].stackSize+stacksize);
        		} else {
        			collatedRecipe.put(itemstacks[n1].itemID, itemstacks[n1].stackSize);
        		}
    		}
    	}
    	
    	for(int n1 = 0; n1 < itemstacks.length; n1++)
    	{
    		if(itemstacks[n1] != null && !getInventoryHasItemStack(inventory, itemstacks[n1], collatedRecipe))
    		{
    			canCraft = false;
    			break;
    		}
    	}
    	
    	return canCraft;
	}
	
	private boolean canCraftShapelessRecipe(IRecipe irecipe, InventoryPlayer inventory, HashMap collatedRecipe) throws NoSuchFieldException
	{
		ArrayList recipeItems = new ArrayList((List)ModLoader.getPrivateValue(ShapelessRecipes.class, (ShapelessRecipes)irecipe, 1));
    	boolean canCraft = true;
    	
    	// Collate each item together to get total stack size per item.
    	//HashMap collatedRecipe = new HashMap();
    	
    	for(int i = 0; i < recipeItems.size(); i++)
    	{
    		ItemStack itemstack = (ItemStack)recipeItems.get(i);
    		collatedRecipe.put(itemstack.itemID, itemstack.stackSize);
    		if(itemstack != null && !getInventoryHasItemStack(inventory, itemstack, collatedRecipe))
    		{
    			canCraft = false;
    			break;
    		}
    	}
        
        return canCraft;
	}
	
	public static boolean getInventoryHasItemStack(InventoryPlayer inventory, ItemStack itemstack, HashMap collatedRecipe)
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
	        int stacksize1 = (Integer)collatedRecipe.get(itemstack.itemID);
	        if(stacksize >= stacksize1)
	        {
	        	return true;
	        }
        }

        return false;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
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
                		ItemStack recipeItemstacks[] = this.getRecipeItemStackArray((IRecipe)recipes.get(i1));
                		((SlotClevercraft)slot).recipeItemstacks = recipeItemstacks;
                		((SlotClevercraft)slot).collatedRecipe = (HashMap)collatedRecipes.get(i1);
                	}
                } else
                {
                	GuiClevercraft.getInventory().setInventorySlotContents(l + k * 8, null);
                	slot = (Slot)inventorySlots.get(l + k * 8);
                	if(slot instanceof SlotClevercraft)
                	{
                		((SlotClevercraft)slot).recipeItemstacks = null;
                	}
                }
            }
        }
    }

    protected void func_35373_b(int i, int j, boolean flag, EntityPlayer entityplayer)
    {
    }

}
