package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.forge.ForgeHooks;

public class SlotClevercraft extends Slot {
	
	private EntityPlayer thePlayer;
	private ContainerClevercraft delegate;
	public IInventory craftMatrix;
	public IRecipe irecipe;
	public Map<Integer, Integer[]> collatedRecipe;
	
	public SlotClevercraft(IInventory iinventory, EntityPlayer entityplayer, ContainerClevercraft container, IInventory matrix, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        thePlayer = entityplayer;
        delegate = container;
        craftMatrix = matrix;
    }
	
	public void findMaxOutputSize()
	{
		int minStack = 9999;
		
		for (Map.Entry<Integer, Integer[]> entry : collatedRecipe.entrySet())
		{
			for(int i = 0; i < thePlayer.inventory.getSizeInventory(); i++)
			{
				ItemStack itemstack = thePlayer.inventory.getStackInSlot(i);
				int itemid = entry.getKey();
				int stacksize = entry.getValue()[0];
				int damageval = entry.getValue()[1];
				if(itemstack != null && itemstack.itemID == itemid && itemstack.getItemDamage() == damageval)
				{
					int stackDivision = MathHelper.floor_double(itemstack.stackSize/stacksize);
					minStack = Math.min(minStack, stackDivision);
				}
			}
		}
		
		if(minStack >= 9999)
			minStack = 1;
		
		getStack().stackSize *= minStack;
	}
	
	public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }
	
	public void onPickupFromSlot(ItemStack itemstack)
    {
		delegate.onPickupItem(itemstack, this);
        itemstack.onCrafting(thePlayer.worldObj, thePlayer);
        if(itemstack.itemID == Block.workbench.blockID)
        {
            thePlayer.addStat(AchievementList.buildWorkBench, 1);
        } else
        if(itemstack.itemID == Item.pickaxeWood.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.buildPickaxe, 1);
        } else
        if(itemstack.itemID == Block.stoneOvenIdle.blockID)
        {
            thePlayer.addStat(AchievementList.buildFurnace, 1);
        } else
        if(itemstack.itemID == Item.hoeWood.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.buildHoe, 1);
        } else
        if(itemstack.itemID == Item.bread.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.makeBread, 1);
        } else
        if(itemstack.itemID == Item.cake.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.bakeCake, 1);
        } else
        if(itemstack.itemID == Item.pickaxeStone.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        } else
        if(itemstack.itemID == Item.swordWood.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.buildSword, 1);
        } else
        if(itemstack.itemID == Block.enchantmentTable.blockID)
        {
            thePlayer.addStat(AchievementList.enchantments, 1);
        } else
        if(itemstack.itemID == Block.bookShelf.blockID)
        {
            thePlayer.addStat(AchievementList.bookcase, 1);
        }
        
        ModLoader.TakenFromCrafting(thePlayer, itemstack, craftMatrix);
        ForgeHooks.onTakenFromCrafting(thePlayer, itemstack, craftMatrix);
        
        //Re-add buckets etc..
        for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
        {
            ItemStack itemstack1 = craftMatrix.getStackInSlot(i);
            craftMatrix.decrStackSize(i, 1);
            if(itemstack1 != null)
            {
                if(itemstack1.getItem().hasContainerItem())
                	thePlayer.inventory.addItemStackToInventory(new ItemStack(itemstack1.getItem().getContainerItem()));
            }
        }
        delegate.populateContainer();
        delegate.gui.updateScreen();
    }
}
