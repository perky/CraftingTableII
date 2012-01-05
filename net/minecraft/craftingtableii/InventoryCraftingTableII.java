package net.minecraft.craftingtableii;

import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryBasic;
import net.minecraft.src.ItemStack;

public class InventoryCraftingTableII {
	
	private IRecipe[] recipes;
	private int recipesLength;
	
	public InventoryCraftingTableII(int i)
    {
		recipesLength = i;
		recipes = new IRecipe[recipesLength];
    }
	
	public int getSize()
	{
		for(int i = 0; i < recipes.length; i++) {
			if(recipes[i] == null)
				return i;
		}
		
		return 0;
	}
	
	public boolean addRecipe(IRecipe irecipe)
	{
		System.out.println("Adding: " + irecipe.getRecipeOutput().toString());
		int size = getSize();
		if(size >= recipesLength || irecipe == null)
			return false;
		
		recipes[size] = irecipe;
		return true;
	}
	
	public IRecipe getIRecipe(int i)
	{
		return recipes[i];
	}
	
	public ItemStack getRecipeOutput(int i)
	{
		if(recipes[i] != null)
			return recipes[i].getRecipeOutput().copy();
		else
			return null;
	}
	
	public void clearRecipes() {
		recipes = null;
		recipes = new IRecipe[recipesLength];
	}
}
