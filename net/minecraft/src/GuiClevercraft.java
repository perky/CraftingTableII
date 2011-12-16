package net.minecraft.src;

import java.util.*;
import java.util.Map.Entry;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiClevercraft extends GuiContainer {
	
	private static InventoryBasic inventory = new InventoryBasic("tmp", 72);
	private float field_35312_g;
    private boolean field_35313_h;
    private boolean field_35314_i;
    private boolean mouseOverRecipe;
    private boolean shouldShowDescriptions;
    
	public GuiClevercraft(EntityPlayer entityplayer)
    {
        super(new ContainerClevercraft(entityplayer));
        ((ContainerClevercraft)inventorySlots).gui = this;
        field_35312_g = 0.0F;
        field_35313_h = false;
        allowUserInput = true;
        mouseOverRecipe = false;
        shouldShowDescriptions = mod_Clevercraft.shouldShowDescriptions;
        ySize = 208;
    }
	
	public void initGui()
    {
		super.initGui();
    	controlList.clear();
    	if(mod_Clevercraft.shouldShowDescriptions)
    		guiLeft += 40;
    }
	
	protected void func_35309_a(Slot slot, int i, int j, boolean flag)
	{
		super.func_35309_a(slot, i, j, flag);
		if(slot != null)
		{
			ItemStack itemstack1 = mc.thePlayer.inventory.getItemStack();
			ItemStack itemstack2 = slot.getStack();
			if(slot.inventory != inventory)
			{
				ContainerClevercraft container = (ContainerClevercraft)inventorySlots;
				container.populateContainer();
				this.updateScreen();
			}
		}
	}
	
	static InventoryBasic getInventory()
    {
        return inventory;
    }
	
	public void drawDescriptions(int i, int j)
	{
        RenderHelper.func_41089_c();
        GL11.glPushMatrix();
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)240 / 1.0F, (float)240 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        for(int j2 = 0; j2 < inventorySlots.inventorySlots.size(); j2++)
        {
            Slot slot1 = (Slot)inventorySlots.inventorySlots.get(j2);
            
            if(slot1.getStack() != null && slot1.getStack().getItem() != null && getIsMouseOverSlot(slot1, i, j))
        	{
            	mouseOverRecipe = true;
            	ItemStack descItem = slot1.getStack();
            	Item item = descItem.getItem();
        		String itemname = item.getItemDisplayName(descItem);
        		String itemcodename = "null";
        		String description = "";
        		try {
        			itemcodename = (String)item.getItemName();
        		} catch(Exception e) {
        			e.printStackTrace();
        		}
        		
        		if(itemcodename == null)
        			itemcodename = "item."+itemname;
        		else if(itemcodename.equalsIgnoreCase("null"))
        			itemcodename = "item."+itemname;
        		
        		
        		if(item != null && item instanceof ItemBlock)
        		{
        			Block block = Block.blocksList[((ItemBlock) item).getBlockID()];
        			if(block != null && block instanceof ICraftingDescription)
        			{
        				description = ((ICraftingDescription)block).getDescription( descItem.getItemDamage() );
        			}
        		}
        		
        		if(description == "")
        		{
	        		if(item != null && item instanceof ICraftingDescription)
	        		{
	        			description = ((ICraftingDescription)item).getDescription( descItem.getItemDamage() );
	        		} else {
	        			description = mod_Clevercraft.getItemDescription(itemcodename, descItem.getItemDamage());
	        		}
        		}
        		
        		float scalef = 0.5F;
        		int titleLeft = guiLeft - 118;
        		int titleTop  = guiTop + 24;
        		int descLeft = MathHelper.floor_float(titleLeft/scalef);
        		int descTop = MathHelper.floor_float(titleTop/scalef);
        		fontRenderer.drawStringWithShadow(itemname, titleLeft, titleTop, -1);
        		
        		GL11.glPushMatrix();
        		GL11.glScalef(scalef, scalef, 1F);
        		GL11.glTranslatef(descLeft, descTop, 0);
            	fontRenderer.drawSplitString(description, 0, 24, 180, -1);
            	fontRenderer.drawSplitString("Code Name:", 0, 285, 180, -1);
            	fontRenderer.drawSplitString(itemcodename+"."+descItem.getItemDamage()+".1", 0, 295, 180, -1);
            	GL11.glPopMatrix();
        	}
            
            if(slot1 != null && slot1 instanceof SlotClevercraft && getIsMouseOverSlot(slot1, i, j))
            {
            	SlotClevercraft slotclever1 = (SlotClevercraft)slot1;
            	
            	ItemStack itemstack = null;
            	if(slotclever1.collatedRecipe != null)
            	{
            		int y = 0;
            		mouseOverRecipe = true;
            		for (Map.Entry<Integer, Integer[]> entry : slotclever1.collatedRecipe.entrySet())
            		{
            			Integer vals[] = entry.getValue();
            			itemstack = new ItemStack(entry.getKey(), vals[0], vals[1]);
            			GL11.glTranslatef(0.0F, 0.0F, 32F);
            			zLevel = 200F;
                        itemRenderer.zLevel = 200F;
            			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, guiLeft-24, guiTop+28+y);
                		itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, guiLeft-24, guiTop+28+y);
                		zLevel = 0F;
                        itemRenderer.zLevel = 0F;
                		y += 18;
            		}
            	}
            }
        }
        GL11.glPopMatrix();
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	}
	
	public void drawScreen(int i, int j, float f)
    {
        boolean flag = Mouse.isButtonDown(0);
        int k = guiLeft;
        int l = guiTop;
        int i1 = k + 155;
        int j1 = l + 17;
        int k1 = i1 + 14;
        int l1 = j1 + 88 + 2;
        if(!field_35314_i && flag && i >= i1 && j >= j1 && i < k1 && j < l1)
        {
            field_35313_h = true;
        }
        if(!flag)
        {
            field_35313_h = false;
        }
        field_35314_i = flag;
        if(field_35313_h)
        {
            field_35312_g = (float)(j - (j1 + 8)) / ((float)(l1 - j1) - 16F);
            if(field_35312_g < 0.0F)
            {
                field_35312_g = 0.0F;
            }
            if(field_35312_g > 1.0F)
            {
                field_35312_g = 1.0F;
            }
            ((ContainerClevercraft)inventorySlots).func_35374_a(field_35312_g);
        }
        super.drawScreen(i, j, f);
        //----
        mouseOverRecipe = false;
        if(shouldShowDescriptions)
        	drawDescriptions(i, j);
        //----
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
    }
	
	private boolean getIsMouseOverSlot(Slot slot, int i, int j)
    {
        int k = guiLeft;
        int l = guiTop;
        i -= k;
        j -= l;
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + 16 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + 16 + 1;
    }
	
	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Crafting Table II", 8, 6, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = mc.renderEngine.getTexture("/gui/crafttableii.png");
        mc.renderEngine.bindTexture(k);
        int l = guiLeft;
        int i1 = guiTop;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        int j1 = l + 155;
        int k1 = i1 + 17;
        int l1 = k1 + 88 + 2;
        drawTexturedModalRect(l + 154, i1 + 17 + (int)((float)(l1 - k1 - 17) * field_35312_g), 0, 208, 16, 16);
        
        k = mc.renderEngine.getTexture("/gui/crafttableii_description.png");
        mc.renderEngine.bindTexture(k);
        if(mouseOverRecipe)
        {
        	drawTexturedModalRect(l-124, i1+18, 0, 0, 121, 162);
        }
    }
    
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if(i != 0)
        {
            int j = (((ContainerClevercraft)inventorySlots).itemList.size() / 8 - 8) + 1;
            if(i > 0)
            {
                i = 1;
            }
            if(i < 0)
            {
                i = -1;
            }
            field_35312_g += (double)i / (double)j;
            if(field_35312_g < 0.0F)
            {
                field_35312_g = 0.0F;
            }
            if(field_35312_g > 1.0F)
            {
                field_35312_g = 1.0F;
            }
            ((ContainerClevercraft)inventorySlots).func_35374_a(field_35312_g);
        }
    }
}
