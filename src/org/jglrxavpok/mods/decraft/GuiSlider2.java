package org.jglrxavpok.mods.decraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

public class GuiSlider2 extends GuiButton
{

	public int valueType = -1;
	public float sliderValue = 0f;
	public float oldValue = 0f;
	private boolean	dragging;
	
	public static final int MIN_XP_LEVEL = 0;
	public static final int MAX_XP_LEVEL = 1;
	
	public GuiSlider2(int id, int x, int y, String txt, float value, int valueType)
	{
		super(id,x,y,150,20,txt);
		this.valueType = valueType;
		sliderValue = value;
	}
	
	protected int func_146114_a(boolean par1)
	{
		return 0;
	}
	
	protected void func_146119_b(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.field_146125_m)
        {
        	int k = 0;
            if (this.dragging)
            {
            	oldValue = sliderValue;
                this.sliderValue = (float)(par2 - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
                if(!ModUncrafting.modInstance.isValuePossible(sliderValue, valueType))
                {
                	while(!ModUncrafting.modInstance.isValuePossible(oldValue, valueType))
                	{
                		oldValue+=0.01f;
                	}
                	sliderValue = oldValue;
                }
                
                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }
                k = 1;
                
                field_146126_j = ModUncrafting.modInstance.getStringAndSetXPLevels(sliderValue, valueType);
                if(!ModUncrafting.modInstance.isValuePossible(oldValue, valueType))
                {
                    do
                    {
                        oldValue+=0.01f;
                    }while(!ModUncrafting.modInstance.isValuePossible(oldValue, valueType));
                    sliderValue = oldValue;
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.field_146128_h + (int)(this.sliderValue * (float)(this.field_146120_f - 8)), this.field_146129_i, 0, 66+20*k, 4, 20);
            this.drawTexturedModalRect(this.field_146128_h + (int)(this.sliderValue * (float)(this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66+20*k, 4, 20);
        }
    }
	
    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean func_146116_c(Minecraft par1Minecraft, int par2, int par3)
    {
        if (super.func_146116_c(par1Minecraft, par2, par3))
        {
        	oldValue = sliderValue;
            this.sliderValue = (float)(oldValue - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
            if(!ModUncrafting.modInstance.isValuePossible(sliderValue, valueType))
            {
            	while(!ModUncrafting.modInstance.isValuePossible(sliderValue, valueType))
            	{
            		sliderValue+=0.01f;
            	}
            }
            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }
    
   public void func_146118_a(int par1, int par2)
   {
       this.dragging = false;
       ModUncrafting.modInstance.saveProperties();
   }
}
