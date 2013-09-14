package org.jglrxavpok.mods.decraft;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
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

	public GuiUncraftingTable(InventoryPlayer playerInventory, World world, String blockName, boolean inverted, int x,int y,int z)
	{
		super((container = new ContainerUncraftingTable(playerInventory, world, inverted,x,y,z)));
		this.blockName = blockName;
		this.inverted = inverted;
	}
	
	public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
    }

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_LIGHTING);

        if(!inverted)
        {
	        fontRenderer.drawString(blockName, xSize/2-fontRenderer.getStringWidth(blockName)/2+1, 5, 4210752);
			fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 2, 4210752);
			
			Color darkGreen = new Color(75, 245, 75);
			String string1 = "Calculs:";
			fontRenderer.drawString(EnumChatFormatting.DARK_GRAY+string1+EnumChatFormatting.RESET, 24-fontRenderer.getStringWidth(string1)/2+1,22,0);
			fontRenderer.drawString(EnumChatFormatting.GRAY+string1+EnumChatFormatting.RESET, 24-fontRenderer.getStringWidth(string1)/2,21,0);
	        
			fontRenderer.drawString(EnumChatFormatting.DARK_GRAY+""+EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRenderer.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2+1,ySize-126-10,0);
			fontRenderer.drawString(EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRenderer.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2,ySize-127-10,darkGreen.getRGB());
	
	        String string = container.result;
	        if(string != null)
	        {
	        	int msgType = container.type;
	        	EnumChatFormatting format = EnumChatFormatting.GREEN;
	        	EnumChatFormatting shadowFormat = EnumChatFormatting.DARK_GRAY;
	        	if(msgType == container.ERROR)
	        	{
	        		format = EnumChatFormatting.WHITE;
	        		shadowFormat = EnumChatFormatting.DARK_RED;
	        	}
	        	
	        	fontRenderer.drawString(shadowFormat+string+EnumChatFormatting.RESET, 6+1, ySize - 95 + 2-fontRenderer.FONT_HEIGHT, 0);
	        	
	        	fontRenderer.drawString(format+string+EnumChatFormatting.RESET, 6, ySize - 96 + 2-fontRenderer.FONT_HEIGHT, 0);
	        }
        }
        else
        {
        	int height = 166-8;
        	fontRenderer.drawString(blockName, xSize/2-fontRenderer.getStringWidth(blockName)/2+1, height-5, 4210752);
        	
			fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, height-ySize - 96 + 2, 4210752);
			
			Color darkGreen = new Color(75, 245, 75);
			String string1 = "Calculs:";
			fontRenderer.drawString(EnumChatFormatting.DARK_GRAY+string1+EnumChatFormatting.RESET, 24-fontRenderer.getStringWidth(string1)/2+1,height-22,0);
			fontRenderer.drawString(EnumChatFormatting.GRAY+string1+EnumChatFormatting.RESET, 24-fontRenderer.getStringWidth(string1)/2,height-21,0);
	        
			fontRenderer.drawString(EnumChatFormatting.DARK_GRAY+""+EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRenderer.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2+1,height-(ySize-126-10),0);
			fontRenderer.drawString(EnumChatFormatting.UNDERLINE+""+(ModUncrafting.standardLevel+container.xp)+" levels"+EnumChatFormatting.RESET, xSize/2-fontRenderer.getStringWidth((ModUncrafting.standardLevel+container.xp)+" levels")/2,height-(ySize-127-10),darkGreen.getRGB());
	
	        String string = container.result;
	        if(string != null)
	        {
	        	int msgType = container.type;
	        	EnumChatFormatting format = EnumChatFormatting.GREEN;
	        	EnumChatFormatting shadowFormat = EnumChatFormatting.DARK_GRAY;
	        	if(msgType == container.ERROR)
	        	{
	        		format = EnumChatFormatting.WHITE;
	        		shadowFormat = EnumChatFormatting.DARK_RED;
	        	}
	        	
	        	fontRenderer.drawString(shadowFormat+string+EnumChatFormatting.RESET, 6+1, height-(ySize - 95 + 2-fontRenderer.FONT_HEIGHT), 0);
	        	
	        	fontRenderer.drawString(format+string+EnumChatFormatting.RESET, 6, height-(ySize - 96 + 2-fontRenderer.FONT_HEIGHT), 0);
	        }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		if(inverted)
		{
			this.mc.renderEngine.func_110577_a(new ResourceLocation("xavpoksDecraft:textures/gui/container/decrafting_gui_redstoned.png"));
		}
		else
			this.mc.renderEngine.func_110577_a(new ResourceLocation("xavpoksDecraft:textures/gui/container/decrafting_gui.png"));

		int x = (width - xSize) / 2;

		int y = (height - ySize) / 2;
		
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		GL11.glPopMatrix();
	}

}