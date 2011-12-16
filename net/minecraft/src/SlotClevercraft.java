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
	
	public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }
	
	public void onPickupFromSlot(ItemStack itemstack)
    {
		itemstack.onCrafting(thePlayer.worldObj, thePlayer);
		delegate.onPickupItem(itemstack, this);
		
        
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
    }
}
