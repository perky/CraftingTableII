package net.minecraft.craftingtableii;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Slot;
import net.minecraft.src.forge.ForgeHooks;

public class SlotClevercraft extends Slot {
	
	private EntityPlayer thePlayer;
	public IInventory craftMatrix;
	private IRecipe irecipe;
	
	public SlotClevercraft(EntityPlayer entityplayer, IInventory craftableRecipes, IInventory matrix, int i, int j, int k)
    {
        super(craftableRecipes, i, j, k);
        thePlayer = entityplayer;
        craftMatrix = matrix;
    }
	
	public void setIRecipe(IRecipe theIRecipe)
	{
		irecipe = theIRecipe;
	}
	
	public IRecipe getIRecipe()
	{
		return irecipe;
	}
	
	public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }
	
	public void onPickupFromSlot(ItemStack itemstack)
    {
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
    }
}
