package lukeperkin.craftingtableii;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

public class RenderCraftingTableII extends TileEntitySpecialRenderer {
	
	private ModelCraftingTableII modelCraftingTable;
	
	public RenderCraftingTableII() {
		modelCraftingTable = new ModelCraftingTableII();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1,
			double d2, float f) {
		
		TileEntityCraftingTableII craftingtable = (TileEntityCraftingTableII)tileentity;
		float doorRotation = craftingtable.doorAngle;
		int facing = craftingtable.getFacing();
		float r = facing * 90F;
		
		GL11.glPushMatrix();
        GL11.glTranslatef((float)d+0.5F, (float)d1+1F, (float)d2+0.5F);
        GL11.glRotatef(270F - r, 0.0F, 1.0F, 0.0F);
        
        bindTextureByName("/blockimage/ctii.png");
        GL11.glScalef(-1F, -1F, 1.0F);
        modelCraftingTable.render(null, doorRotation, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();

	}

}
