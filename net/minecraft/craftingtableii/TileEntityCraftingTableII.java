package net.minecraft.craftingtableii;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;

public class TileEntityCraftingTableII extends TileEntity {
	
	public double playerDistance;
	public float doorAngle;
	public static final float openspeed = 0.2F; 
	private int tablestate;
	
	public TileEntityCraftingTableII() {
		playerDistance = 7F;
		doorAngle = 0F;
		tablestate = 0;
	}
	
	public int getFacing()
	{
		return getBlockMetadata();
	}
	
	public void updateEntity()
	{
		super.updateEntity();
		EntityPlayer entityplayer = worldObj.getClosestPlayer((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, 10D);
		if(entityplayer != null){
			playerDistance = entityplayer.getDistanceSq((double)xCoord, (double)yCoord, (double)zCoord);
			if(playerDistance < 7F){
				doorAngle += openspeed;
				
				if(tablestate != 1) {
					tablestate = 1;
					worldObj.playSoundEffect((double)xCoord, (double)yCoord + 0.5D, (double)zCoord, 
							"random.chestopen", 0.2F, 
							worldObj.rand.nextFloat() * 0.1F + 0.2F);
				}
				
				if(doorAngle > 1.8F){
					doorAngle = 1.8F;
				}
			} else if(playerDistance > 7F) {
				doorAngle -= openspeed;
				
				if(tablestate != 0) {
					tablestate = 0;
					worldObj.playSoundEffect((double)xCoord, (double)yCoord + 0.5D, (double)zCoord, 
							"random.chestclosed", 0.2F, 
							worldObj.rand.nextFloat() * 0.1F + 0.2F);
				}
				
				if(doorAngle < 0F){
					doorAngle = 0F;
				}
			}
		}
	}

}
