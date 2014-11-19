package org.jglrxavpok.mods.decraft;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;

import org.lwjgl.opengl.*;

public class GuiSlider2 extends GuiButton
{

    public int              valueType    = -1;
    public float            sliderValue  = 0f;
    public float            oldValue     = 0f;
    private boolean         dragging;

    public static final int MIN_XP_LEVEL = 0;
    public static final int MAX_XP_LEVEL = 1;

    public GuiSlider2(int id, int x, int y, String txt, float value, int valueType)
    {
        super(id, x, y, 150, 20, txt);
        this.valueType = valueType;
        sliderValue = value;
    }

    public int getHoverState(boolean par1)
    {
        return 0;
    }

    public void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if(this.visible)
        {
            int k = 0;
            if(this.dragging)
            {
                oldValue = sliderValue;
                this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);
                if(!ModUncrafting.instance.isValuePossible(sliderValue, valueType))
                {
                    while(!ModUncrafting.instance.isValuePossible(oldValue, valueType))
                    {
                        oldValue += 0.01f;
                    }
                    sliderValue = oldValue;
                }

                if(this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if(this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }
                k = 1;

                displayString = ModUncrafting.instance.getStringAndSetXPLevels(sliderValue, valueType);
                if(!ModUncrafting.instance.isValuePossible(oldValue, valueType))
                {
                    do
                    {
                        oldValue += 0.01f;
                    }
                    while(!ModUncrafting.instance.isValuePossible(oldValue, valueType));
                    sliderValue = oldValue;
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66 + 20 * k, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66 + 20 * k, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if(super.mousePressed(par1Minecraft, par2, par3))
        {
            oldValue = sliderValue;
            this.sliderValue = (float) (oldValue - (this.xPosition + 4)) / (float) (this.width - 8);
            if(!ModUncrafting.instance.isValuePossible(sliderValue, valueType))
            {
                while(!ModUncrafting.instance.isValuePossible(sliderValue, valueType))
                {
                    sliderValue += 0.01f;
                }
            }
            if(this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if(this.sliderValue > 1.0F)
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

    public void mouseReleased(int par1, int par2)
    {
        this.dragging = false;
        ModUncrafting.instance.saveProperties();
    }
}
