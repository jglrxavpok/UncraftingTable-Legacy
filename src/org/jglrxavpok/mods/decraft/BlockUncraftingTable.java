package org.jglrxavpok.mods.decraft;

import java.util.Random;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

/**
 * 
 * @author jglrxavpok
 *
 */
public class BlockUncraftingTable extends Block
{

	public BlockUncraftingTable(int blockId)
	{
		super(blockId, Material.rock);
		setHardness(0.5F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@SideOnly(Side.CLIENT)
    private Icon topBlock;
    @SideOnly(Side.CLIENT)
    private Icon front;
	private Icon bottom;
	private Icon	redstonedBlockIcon;
	private Icon	redstonedFront;
    
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f, float g, float t)
	{
		player.openGui(ModUncrafting.modInstance, 0, world, x, y, z);
		/**
		 * @see org.jglrxavpok.mods.decraft.ModUncrafting
		 */
		checkForPorteManteau(player, world, x, y, z);
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int i, int j)
	{
		super.breakBlock(world, x, y, z, i, j);
	}
	
	/**
     * Called whenever the block is added into the world. Args: world, x, y, z
     * This one is used to know if there is a redstone power near it.
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            if (par1World.getBlockMetadata(par2, par3, par4) == 1 && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
            }
            else if (par1World.getBlockMetadata(par2, par3, par4) == 0 && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
                par1World.setBlock(par2, par3, par4, blockID, 1, 2);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     * This one is used to know if there is a redstone power near it.
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            if (par1World.getBlockMetadata(par2, par3, par4) == 1 && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
            }
            else if (par1World.getBlockMetadata(par2, par3, par4) == 0 && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
            	par1World.setBlock(par2, par3, par4, blockID, 1, 2);
            }
        }
        
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote && par1World.getBlockMetadata(par2, par3, par4) == 1 && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
        {
        	par1World.setBlock(par2, par3, par4, blockID, 0, 2);
        }
        
        
    }

	private void checkForPorteManteau(EntityPlayer player, World w, int x, int y, int z)
	{
		boolean furnace = false, chest = false, workbench = false;
		if(w.getBlockId(x, y-1, z) == Block.fence.blockID)
		{
			if((w.getBlockId(x+1, y, z) == Block.furnaceIdle.blockID || w.getBlockId(x+1, y, z) == Block.furnaceBurning.blockID)
			|| (w.getBlockId(x-1, y, z) == Block.furnaceIdle.blockID || w.getBlockId(x-1, y, z) == Block.furnaceBurning.blockID)
			|| (w.getBlockId(x, y, z+1) == Block.furnaceIdle.blockID || w.getBlockId(x, y, z+1) == Block.furnaceBurning.blockID)
			|| (w.getBlockId(x, y, z-1) == Block.furnaceIdle.blockID || w.getBlockId(x, y, z-1) == Block.furnaceBurning.blockID))
				furnace = true;
			if(w.getBlockId(x+1, y, z) == Block.chest.blockID
					|| w.getBlockId(x-1, y, z) == Block.chest.blockID
					|| w.getBlockId(x, y, z+1) == Block.chest.blockID
					|| w.getBlockId(x, y, z-1) == Block.chest.blockID)
						chest = true;
			if(w.getBlockId(x+1, y, z) == Block.workbench.blockID
					|| w.getBlockId(x-1, y, z) == Block.workbench.blockID
					|| w.getBlockId(x, y, z+1) == Block.workbench.blockID
					|| w.getBlockId(x, y, z-1) == Block.workbench.blockID)
						workbench = true;
			
			if(furnace && chest && workbench)
			{
				player.triggerAchievement(ModUncrafting.modInstance.porteManteauAchievement);
			}
		}
	}

	@SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
		if(par2 == 0)
		{
			return par1 == 1 ? this.topBlock : (par1 == 0 ? bottom : (par1 != 3 && par1 != 1 ? this.blockIcon : this.front));
		}
		else
		{
			return par1 == 1 ? this.bottom : (par1 == 0 ? topBlock : (par1 != 3 && par1 != 1 ? this.redstonedBlockIcon : this.redstonedFront));
		}
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("xavpoksDecraft:decrafting_side");
        this.redstonedBlockIcon = par1IconRegister.registerIcon("xavpoksDecraft:decrafting_side_redstoned");
        this.topBlock = par1IconRegister.registerIcon("xavpoksDecraft:decrafting_top");
        this.front = par1IconRegister.registerIcon("xavpoksDecraft:decrafting_front");
        this.redstonedFront = par1IconRegister.registerIcon("xavpoksDecraft:decrafting_front_redstoned");
        this.bottom = par1IconRegister.registerIcon("xavpoksDecraft:decrafting_bottom");
    }

}