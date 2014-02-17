package org.jglrxavpok.mods.decraft;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * The client-visible part of the interface.
 * Can be inverted by a redstone power.
 * @author jglrxavpok
 *
 */
public class GuiUncraftingTable extends GuiContainer
{

	public static ContainerUncraftingTable container;
	private String blockName;
	private boolean	inverted;
	private World	worldObj;
	private int x;
	private int z;
	private int y;
	private EntityPlayer	player;

	public GuiUncraftingTable(InventoryPlayer playerInventory, World world, String blockName, boolean inverted, int x,int y,int z, int min, int max)
	{
		super((container = new ContainerUncraftingTable(playerInventory, world, inverted,x,y,z, min, max)));
		this.blockName = blockName;
		this.inverted = inverted;
		this.worldObj = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.player = playerInventory.player;
	}
	
	public void actionPerformed(GuiButton button)
	{
	}
	
	public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        boolean op = true; // TODO: Check OP
        if(Keyboard.isKeyDown(Keyboard.KEY_O) && op)
        {
        	player.openGui(ModUncrafting.modInstance, 1, worldObj, x, y, z);
        }
    }

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        int xSize = this.xSize;
        int ySize = this.ySize;
        if(!inverted)
        {
	        fontRendererObj.drawString(blockName, xSize/2-fontRendererObj.getStringWidth(blockName)/2+1, 5, 4210752);
			fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 2, 4210752);
			
			Color darkGreen = new Color(75, 245, 75);
			String string1 = "Calculs:";
			fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY+string1+EnumChatFormatting.RESET, 24-fontRendererObj.getStringWidth(string1)/2+1,22,0);
			fontRendererObj.drawString(EnumChatFormatting.GRAY+string1+EnumChatFormatting.RESET, 24-fontRendererObj.getStringWidth(string1)/2,21,0);
	        
			fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY+""+EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRendererObj.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2+1,ySize-126-10,0);
			fontRendererObj.drawString(EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRendererObj.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2,ySize-127-10,darkGreen.getRGB());
	
	        String string = container.result;
	        if(string != null)
	        {
	        	int msgType = container.type;
	        	EnumChatFormatting format = EnumChatFormatting.GREEN;
	        	EnumChatFormatting shadowFormat = EnumChatFormatting.DARK_GRAY;
	        	if(msgType == ContainerUncraftingTable.ERROR)
	        	{
	        		format = EnumChatFormatting.WHITE;
	        		shadowFormat = EnumChatFormatting.DARK_RED;
	        	}
	        	
	        	fontRendererObj.drawString(shadowFormat+string+EnumChatFormatting.RESET, 6+1, ySize - 95 + 2-fontRendererObj.FONT_HEIGHT, 0);
	        	
	        	fontRendererObj.drawString(format+string+EnumChatFormatting.RESET, 6, ySize - 96 + 2-fontRendererObj.FONT_HEIGHT, 0);
	        }
        }
        else
        {
        	int height = 166-8;
        	fontRendererObj.drawString(blockName, xSize/2-fontRendererObj.getStringWidth(blockName)/2+1, height-5, 4210752);
        	
			fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6, height-ySize - 96 + 2, 4210752);
			
			Color darkGreen = new Color(75, 245, 75);
			String string1 = "Calculs:";
			fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY+string1+EnumChatFormatting.RESET, 24-fontRendererObj.getStringWidth(string1)/2+1,height-22,0);
			fontRendererObj.drawString(EnumChatFormatting.GRAY+string1+EnumChatFormatting.RESET, 24-fontRendererObj.getStringWidth(string1)/2,height-21,0);
	        
			fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY+""+EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRendererObj.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2+1,height-(ySize-126-10),0);
			fontRendererObj.drawString(EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRendererObj.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2,height-(ySize-127-10),darkGreen.getRGB());
	
	        String string = container.result;
	        if(string != null)
	        {
	        	int msgType = container.type;
	        	EnumChatFormatting format = EnumChatFormatting.GREEN;
	        	EnumChatFormatting shadowFormat = EnumChatFormatting.DARK_GRAY;
	        	if(msgType == ContainerUncraftingTable.ERROR)
	        	{
	        		format = EnumChatFormatting.WHITE;
	        		shadowFormat = EnumChatFormatting.DARK_RED;
	        	}
	        	
	        	fontRendererObj.drawString(shadowFormat+string+EnumChatFormatting.RESET, 6+1, height-(ySize - 95 + 2-fontRendererObj.FONT_HEIGHT), 0);
	        	
	        	fontRendererObj.drawString(format+string+EnumChatFormatting.RESET, 6, height-(ySize - 96 + 2-fontRendererObj.FONT_HEIGHT), 0);
	        }
        }
        boolean op = true; // TODO: Check OP
        String optionsText = StatCollector.translateToLocal("uncrafting.options.hit");
        if(optionsText == null || "uncrafting.options.hit".equals(optionsText))
        {
        	optionsText = "Hit 'O' to show options";
        }
        if(op)
        fontRendererObj.drawString(EnumChatFormatting.UNDERLINE+optionsText,xSize-fontRendererObj.getStringWidth(optionsText)-4,ySize - 96 + 2,0);

        GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		if(inverted)
		{
			this.mc.renderEngine.bindTexture(new ResourceLocation("uncraftingTable:textures/gui/container/decrafting_gui_redstoned.png"));
		}
		else
			this.mc.renderEngine.bindTexture(new ResourceLocation("uncraftingTable:textures/gui/container/decrafting_gui.png"));

        int k = width/2-xSize/2;
        int l = height/2-ySize/2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		GL11.glPopMatrix();
	}

}