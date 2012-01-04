package net.minecraft.craftingtableii;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IGuiItemDescription;
import net.minecraft.src.InventoryBasic;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.mod_Clevercraft;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiClevercraft extends GuiContainer {
	
	private static InventoryBasic inventory = new InventoryBasic("tmp", 72);
	private float field_35312_g;
    private boolean field_35313_h;
    private boolean field_35314_i;
    private boolean shouldShowDescriptions;
    private IGuiItemDescription guiItemDescriptions;
    
	public GuiClevercraft(EntityPlayer entityplayer)
    {
        super(new ContainerClevercraft(entityplayer.inventory));
        ((ContainerClevercraft)inventorySlots).gui = this;
        field_35312_g = 0.0F;
        field_35313_h = false;
        allowUserInput = true;
        shouldShowDescriptions = mod_Clevercraft.shouldShowDescriptions;
        ySize = 208;
        
        if(shouldShowDescriptions)
        {
        	try{
        		guiItemDescriptions = (IGuiItemDescription)mod_Clevercraft.guiDescriptions.newInstance();
        	} catch(Exception e) {
        		shouldShowDescriptions = false;
        	}
        }	
    }
	
	public void initGui()
    {
		super.initGui();
    	controlList.clear();
    	if(mod_Clevercraft.shouldShowDescriptions)
    		guiLeft += 40;
    	
    	if(shouldShowDescriptions)
    		guiItemDescriptions.setSize(xSize, ySize);
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
        if(shouldShowDescriptions)
        {
			GL11.glPushMatrix();
	        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
	        guiItemDescriptions.drawDescriptionBackground(i, j);
	        
        	for(int j2 = 0; j2 < inventorySlots.inventorySlots.size(); j2++)
            {
                Slot slot1 = (Slot)inventorySlots.inventorySlots.get(j2);
                
                if(slot1.getStack() != null && slot1.getStack().getItem() != null && getIsMouseOverSlot(slot1, i, j))
            	{
                	if(slot1 instanceof SlotClevercraft) {
                		SlotClevercraft slotclever = (SlotClevercraft)slot1;
                		guiItemDescriptions.drawDescriptions(i, j, slot1.getStack(), false);
                    	guiItemDescriptions.displayCollatedRecipe(i, j, slotclever.getCollatedRecipe());
                    	break;
                	} else {
                		guiItemDescriptions.drawDescriptions(i, j, slot1.getStack(), true);
                    	break;
                	}	
            	}
            }
        	
        	GL11.glPopMatrix();
        }
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
    }
    
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if(i != 0)
        {
            int j = (((ContainerClevercraft)inventorySlots).itemList.size() / 8 - 5) + 1;
            if(i > 0)
            {
                i = 1;
            }
            if(i < 0)
            {
                i = -1;
            }
            field_35312_g -= (double)i / (double)j;
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
    
    public void resetScroll()
	{
		field_35312_g = 0.0F;
		((ContainerClevercraft)inventorySlots).func_35374_a(field_35312_g);
	}
}
