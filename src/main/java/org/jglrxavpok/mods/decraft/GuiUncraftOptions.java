package org.jglrxavpok.mods.decraft;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiUncraftOptions extends GuiScreen
{

	private GuiSlider2 maxLevel, minLevel;
	
	public GuiUncraftOptions()
	{
		super();
	}
	
	public void initGui()
	{
		String methodName=StatCollector.translateToLocal("uncrafting.options.method.jglr.switchto");
		if(methodName == null || "uncrafting.options.method.jglr.switchto".equals(methodName))
		{
			methodName="Switch to jglrxavpok's uncrafting method";
		}
		GuiButton uncraftMethod0 = new GuiButton(0,width/2-250/2,40,250,20,methodName);
		buttonList.add(uncraftMethod0);
		methodName=StatCollector.translateToLocal("uncrafting.options.method.xell75zenen.switchto");
		if(methodName == null || "uncrafting.options.method.xell75zenen.switchto".equals(methodName))
		{
			methodName="Switch to Xell75 and zenen's uncrafting method";
		}
		GuiButton uncraftMethod1 = new GuiButton(1,width/2-250/2,70,250,20,methodName);
		buttonList.add(uncraftMethod1);
		
		maxLevel = new GuiSlider2(2,width/2+250/2-150+150/2,175,"Max Level",((float)(ModUncrafting.modInstance.maxLvlServer-10f))/50f,1);
		minLevel = new GuiSlider2(3,width/2-250/2-150/2,175,"Min Level",((float)((ModUncrafting.modInstance.minLvlServer-5f))/35f),0);
		
		buttonList.add(maxLevel);
		buttonList.add(minLevel);
	}
	
	public void drawScreen(int par1, int par2, float f)
	{
	    this.drawBackground(0);
		if(ModUncrafting.uncraftMethod == 0)
		{
			maxLevel.visible = false;
		}
		else
		{
			maxLevel.visible = true;
		}
		String methodName = null;
		String methodImg = null;
		if(ModUncrafting.uncraftMethod == 0)
		{
			methodName=StatCollector.translateToLocal("uncrafting.options.method.jglr");
			if(methodName == null || "uncrafting.options.method.jglr".equals(methodName))
			{
				methodName="jglrxavpok's uncrafting method";
			}
			methodImg="jglrxavpoksmethod";
		}
		else
		{
			methodName=StatCollector.translateToLocal("uncrafting.options.method.xell75zenen");
			if(methodName == null || "uncrafting.options.method.xell75zenen".equals(methodName))
			{
				methodName="Xell75's & zenen's uncrafting method";
			}
			methodImg="Xell75s&zenens";
		}
		
		if(methodImg != null)
		{
			this.mc.renderEngine.bindTexture(new ResourceLocation("uncraftingTable:textures/gui/"+methodImg+".png"));
			GL11.glPushMatrix();
			GL11.glScalef(0.5f, 0.25f, 1);
			this.drawTexturedModalRect(width-125, 190*2+50, 0, 0, 255, 250);
			GL11.glPopMatrix();
		}
		
		super.drawScreen(par1, par2, f);
		String optionsLabel = StatCollector.translateToLocal("uncrafting.options");
		if(optionsLabel == null || "uncrafting.options".equals(optionsLabel))
		{
			optionsLabel="Uncrafting options";
		}
		fontRendererObj.drawString(EnumChatFormatting.WHITE+optionsLabel, width/2-fontRendererObj.getStringWidth(optionsLabel)/2, 15, 0);
		
		String getBackText = StatCollector.translateToLocal("uncrafting.options.getback");
		if(getBackText == null || "uncrafting.options.getback".equals(getBackText))
		{
			getBackText="Press 'Esc' to get back to game.";
		}
		fontRendererObj.drawString(EnumChatFormatting.WHITE+getBackText, 1, 1, 0);
		
		String using = StatCollector.translateToLocal("uncrafting.options.method.using");
		if(using == null || using.equals("uncrafting.options.method.using"))
		{
			using="Currently using";
		}
		fontRendererObj.drawString(EnumChatFormatting.WHITE+using+": "+EnumChatFormatting.GOLD+methodName, width/2-250/2, 95, 0);
		
		String slidersText = StatCollector.translateToLocal("uncrafting.options.lvl.sliders");
		if(slidersText == null || slidersText.equals("uncrafting.options.lvl.sliders"))
		{
			slidersText = "XP levels";
		}
		slidersText = "<-- "+slidersText;
		if(ModUncrafting.modInstance.uncraftMethod == 1)
		{
			slidersText+=" -->";
		}
		else
		{
			slidersText+="    ";
		}
		fontRendererObj.drawString(EnumChatFormatting.WHITE+""+EnumChatFormatting.BOLD+slidersText, width/2-fontRendererObj.getStringWidth(EnumChatFormatting.WHITE+""+EnumChatFormatting.BOLD+slidersText)/2, 181, 0);
	}
	
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			ModUncrafting.modInstance.uncraftMethod = 0;
			ModUncrafting.modInstance.saveProperties();
		}
		else if(button.id == 1)
		{
			ModUncrafting.modInstance.uncraftMethod = 1;
			ModUncrafting.modInstance.saveProperties();
		}
	}
}
