package org.jglrxavpok.mods.decraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * 
 * @author jglrxavpok
 *
 */
public class ContainerUncraftingTable extends Container
{

    public static final int ERROR = 1;
	public InventoryCrafting uncraftIn = new InventoryCrafting(this, 1,1);
    public InventoryUncraftResult uncraftOut = new InventoryUncraftResult();
    public InventoryCrafting calculInput = new InventoryCrafting(this,1,1);
	private World worldObj;
	public InventoryPlayer playerInv;
	public String result = StatCollector.translateToLocal("uncrafting.result.ready") == null ? "Ready" :StatCollector.translateToLocal("uncrafting.result.ready"); 
	public int type = 0;
	public int xp = -ModUncrafting.standardLevel;
	public int x = 0;
	public int y = 0;
	public int z = 0;
	private boolean	confirmation;
	private int	minLvl;
	private int	maxLvl;
	

	public ContainerUncraftingTable(InventoryPlayer par1PlayerInventory, World world, boolean inverted,int x,int y,int z, int minLvl, int maxLvl)
	{
		this.minLvl = minLvl;
		this.maxLvl = maxLvl;
		this.x = x;
		this.y = y;
		this.z = z;
        this.worldObj = world;
		int l;
        int i1;
        int i2;
        if(!inverted)
        {
			for (l = 0; l < 3; ++l)
	        {
	            for (i1 = 0; i1 < 3; ++i1)
	            {
	            		this.addSlotToContainer(new Slot(this.uncraftOut, i1 + l * 3, 112 + i1 * 18, 17 + l * 18));                
	            }
	        }
			this.addSlotToContainer(new Slot(this.uncraftIn, 0, 30 + 15, 35));
			this.addSlotToContainer(new Slot(this.calculInput, 0, 15, 35));
	
			for (l = 0; l < 3; ++l)
	        {
	            for (i1 = 0; i1 < 9; ++i1)
	            {
	                this.addSlotToContainer(new Slot(par1PlayerInventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
	            }
	        }
	        for (l = 0; l < 9; ++l)
	        {
	            this.addSlotToContainer(new Slot(par1PlayerInventory, l, 8 + l * 18, 142));
	        }
        }
        else
        {
        	int height = 166-16;
        	for (l = 0; l < 3; ++l)
	        {
	            for (i1 = 0; i1 < 3; ++i1)
	            {
	            		this.addSlotToContainer(new Slot(this.uncraftOut, i1 + l * 3, 112 + i1 * 18, height-(17+l*18)));                
	            }
	        }
	
			this.addSlotToContainer(new Slot(this.uncraftIn, 0, 30 + 15, height-35));
			this.addSlotToContainer(new Slot(this.calculInput, 0, 15, height-35));
	
			for (l = 0; l < 3; ++l)
	        {
	            for (i1 = 0; i1 < 9; ++i1)
	            {
	                this.addSlotToContainer(new Slot(par1PlayerInventory, i1 + l * 9 + 9, 8 + i1 * 18, height-(84 + l * 18)));
	            }
	        }
	
	        for (l = 0; l < 9; ++l)
	        {
	            this.addSlotToContainer(new Slot(par1PlayerInventory, l, 8 + l * 18, height-142));
	        }
        }
        playerInv = par1PlayerInventory;
	}
	

	/**
	 * Short story: fires a UncraftingEvent instance and look if the uncrafting is possible.
	 * If possible, tries to do the uncrafting and fires a SuccessedUncraftingEvent if managed to do it.
	 */
	public void onCraftMatrixChanged(IInventory inventory)
	{
		if(inventory == calculInput)
		{
			if(calculInput.getStackInSlot(0) == null)
			{
				xp = 0;
				if(uncraftIn.getStackInSlot(0) == null)
				{
					String r = StatCollector.translateToLocal("uncrafting.result.ready");
					if(r == null)
					{
						r = "Ready!";
					}
					result = r;
					type = 0;
					xp = -ModUncrafting.standardLevel;
				}
				return;
			}
			else if(uncraftIn.getStackInSlot(0) == null)
			{
				List<ItemStack[]> list1 = UncraftingManager.getUncraftResults(calculInput.getStackInSlot(0));
				ItemStack[] output = null;
				if(list1.size() > 0)
					output = list1.get(0);
				List<Integer> needs = UncraftingManager.getStackSizeNeeded(calculInput.getStackInSlot(0));
				int required = 1;
				if(needs.size() > 0)
				{
					required = needs.get(0);
				}
				UncraftingEvent event = new UncraftingEvent(calculInput.getStackInSlot(0), output, required,playerInv.player);
				if(!MinecraftForge.EVENT_BUS.post(event))
				{
					int nbrStacks = event.getRequiredNumber();
					if(nbrStacks > calculInput.getStackInSlot(0).stackSize)
					{
						String r = StatCollector.translateToLocal("uncrafting.result.needMoreStacks");
						if(r == null)
						{
							r = "At least "+nbrStacks+" of that!";
						}
						else
						{
							r = r.replace("$nbrStacks", ""+(nbrStacks-calculInput.getStackInSlot(0).stackSize));
						}
						result =r;
						type = ERROR;
						xp = -minLvl;
						return;
					}
					else if(event.getOutput() == null)
					{
						String r = StatCollector.translateToLocal("uncrafting.result.impossible");
						if(r == null)
						{
							r = "Impossible with this!";
						}
						result = r;
						type = ERROR;
						xp = -minLvl;
						return;
					}
					else
					{
						String r = StatCollector.translateToLocal("uncrafting.result.ready");
						if(r == null)
						{
							r = "Ready!";
						}
						result = r;
						type = 0;
					}
				}
				if(ModUncrafting.uncraftMethod == 0)
				{
					xp = 0;
				}
				else if(ModUncrafting.uncraftMethod == 1)
				{
					ItemStack s1 = calculInput.getStackInSlot(0);
					int percent = (int)(((double)s1.getItemDamageForDisplay()/(double)s1.getMaxDamage())*100);
					xp = (maxLvl*percent)/100;
				}
			}
			else
			{
				String r = StatCollector.translateToLocal("uncrafting.result.impossible");
				if(r == null)
				{
					r = "Impossible with this!";
				}
				result = r;
				type = ERROR;
				xp = -minLvl;
				return;
			}
		}
		else if(inventory == uncraftIn)
		{
			xp = 0;
			if(uncraftIn.getStackInSlot(0) == null)
			{
				String r = StatCollector.translateToLocal("uncrafting.result.ready");
				if(r == null)
				{
					r = "Ready";
				}
				result = r;
				if(calculInput.getStackInSlot(0) == null)
				{
					xp = 0;
				}
				type = 0;
				return;
			}
			List<ItemStack[]> list1 = UncraftingManager.getUncraftResults(uncraftIn.getStackInSlot(0));
			ItemStack[] output = null;
			if(list1.size() > 0)
				output = list1.get(0);
			List<Integer> needs = UncraftingManager.getStackSizeNeeded(uncraftIn.getStackInSlot(0));
			int required = 1;
			if(needs.size() > 0)
			{
				required = needs.get(0);
			}
			UncraftingEvent event = new UncraftingEvent(uncraftIn.getStackInSlot(0), output,required,playerInv.player);
			if(!MinecraftForge.EVENT_BUS.post(event))
			{
				int nbrStacks = event.getRequiredNumber();
				if(nbrStacks > uncraftIn.getStackInSlot(0).stackSize)
				{
					String r = StatCollector.translateToLocal("uncrafting.result.needMoreStacks");
					if(r == null)
					{
						r = "Need "+nbrStacks+" more!";
					}
					else
					{
						r = r.replace("$nbrStacks", ""+(nbrStacks-uncraftIn.getStackInSlot(0).stackSize));
					}
					result =r;
					type = ERROR;
					return;
				}
				EntityPlayer player = playerInv.player;
				int lvl = player.experienceLevel;
				xp = 0;
				if(!uncraftOut.isEmpty())
				{
					for(int i = 0;i<uncraftOut.getSizeInventory();i++)
					{
						ItemStack item = uncraftOut.getStackInSlot(i);
						if(item != null)
						{
							if(!playerInv.addItemStackToInventory(item))
							{
								EntityItem e = playerInv.player.entityDropItem(item, 0.5f);
								e.posX = playerInv.player.posX;
								e.posY = playerInv.player.posY;
								e.posZ = playerInv.player.posZ;
							}
							uncraftOut.setInventorySlotContents(i, null);
						}
					}
				}
				if(!EnchantmentHelper.getEnchantments(uncraftIn.getStackInSlot(0)).isEmpty() && calculInput.getStackInSlot(0) != null && calculInput.getStackInSlot(0).getItem() == Items.book)
				{
					Map enchantsMap = EnchantmentHelper.getEnchantments(uncraftIn.getStackInSlot(0));
					Iterator<?> i = enchantsMap.keySet().iterator();
					Map tmpMap = new LinkedHashMap();
					ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
					while(i.hasNext())
					{
						int id = (Integer)i.next();
						tmpMap.put(id,(Integer)enchantsMap.get(id));
						ItemStack stack = new ItemStack(Items.enchanted_book, 1);
						EnchantmentHelper.setEnchantments(tmpMap, stack);
						stacks.add(stack);
						tmpMap.clear();
					}
					int nbr = calculInput.getStackInSlot(0).stackSize;
					for(ItemStack s : stacks)
					{
						nbr--;
						if(!playerInv.addItemStackToInventory(s))
						{
							EntityItem e = playerInv.player.entityDropItem(s,0.5f);
							e.posX = playerInv.player.posX;
							e.posY = playerInv.player.posY;
							e.posZ = playerInv.player.posZ;
						}
						if(nbr <= 0)
						{
							break;
						}
					}
					calculInput.setInventorySlotContents(0, null);
				}
				ItemStack[] items = event.getOutput();
				if(items == null)
				{
					String r = StatCollector.translateToLocal("uncrafting.result.impossible");
					if(r == null)
					{
						r = "Impossible with this!";
					}
					result = r;
					type = ERROR;
					return;
				}
				if(!playerInv.player.capabilities.isCreativeMode && uncraftIn.getStackInSlot(0).getItem().getItemEnchantability() > 0)
				{
					if(ModUncrafting.uncraftMethod == 0)
					{
						int count = 0;
						ItemStack s1 = uncraftIn.getStackInSlot(0);
						
						int percent = (int)(((double)s1.getItemDamageForDisplay()/(double)s1.getMaxDamage())*100);
						for(int i = 0;i<items.length;i++)
						{
							if(items[i] != null)
								count++;
						}
						int toRemove = Math.round((float)(percent*count)/100f);
						if(toRemove > 0)
							for(int i = 0;i<items.length;i++)
							{
								if(items[i] != null)
								{
									toRemove--;
									items[i] = null;
									if(toRemove <= 0)
									{
										break;
									}
								}
							}
					}
					else if(ModUncrafting.uncraftMethod == 1)
					{
						ItemStack s1 = uncraftIn.getStackInSlot(0);
						int percent = (int)(((double)s1.getItemDamageForDisplay()/(double)s1.getMaxDamage())*100);
						xp = (maxLvl*percent)/100;
					}
				}
				if(lvl < ModUncrafting.standardLevel+xp && !player.capabilities.isCreativeMode)
				{
					String r = StatCollector.translateToLocal("uncrafting.result.needMoreXP");
					if(r == null)
					{
						r = "You need more xp!";
					}
					result = r;
					type = ERROR;
					return;
				}
				else if(lvl >= ModUncrafting.standardLevel+xp && !player.capabilities.isCreativeMode)
				{
					player.experienceLevel-=ModUncrafting.standardLevel+xp;
				}
				for(int i = 0;i<items.length;i++)
				{
					ItemStack s = items[i];
					if(s != null)
					{
						int metadata = s.getItemDamageForDisplay();
						if(metadata == 32767)
						{
							metadata = 0;
						}
						ItemStack newStack = new ItemStack(s.getItem(), 1, metadata);
						uncraftOut.setInventorySlotContents(i, newStack);
					}
					else
					{
						uncraftOut.setInventorySlotContents(i, null);
					}
				}
				ItemStack stack = uncraftIn.getStackInSlot(0);
				int n = (stack.stackSize-nbrStacks);
				if(n > 0)
				{
					ItemStack newStack = new ItemStack(stack.getItem(), n, stack.getItemDamageForDisplay());
					if(!playerInv.addItemStackToInventory(newStack))
					{
						EntityItem e = playerInv.player.entityDropItem(newStack,0.5f);
						e.posX = playerInv.player.posX;
						e.posY = playerInv.player.posY;
						e.posZ = playerInv.player.posZ;
					}
				}
				SuccessedUncraftingEvent sevent = new SuccessedUncraftingEvent(uncraftIn.getStackInSlot(0), items, event.getRequiredNumber(),playerInv.player);
				if(!MinecraftForge.EVENT_BUS.post(sevent))
				{
//					event.getPlayer().addStat(ModUncrafting.modInstance.uncraftedItemsStat, event.getRequiredNumber());
					event.getPlayer().triggerAchievement(ModUncrafting.modInstance.uncraftAny);
				}
				uncraftIn.setInventorySlotContents(0, null);
				this.onCraftMatrixChanged(calculInput);
				confirmation = false;
			}
			else
			{
				String r = StatCollector.translateToLocal("uncrafting.result.impossible");
				if(r == null)
				{
					r = "Impossible with this!";
				}
				result = r;
				type = ERROR;
			}
		}
		else
		{
			String r = StatCollector.translateToLocal("uncrafting.result.impossible");
			if(r == null)
			{
				r = "Impossible with this!";
			}
			result = r;
			type = ERROR;
		}
	}
	
	public ItemStack slotClick(int par1,int par2,int par3, EntityPlayer player)
	{
		ItemStack r = super.slotClick(par1, par2, par3, player);
		if(inventorySlots.size() > par1 && par1 >= 0)
		{
			if(inventorySlots.get(par1) != null)
			{
				if((((Slot)inventorySlots.get(par1)).inventory == calculInput || ((Slot)inventorySlots.get(par1)).inventory == playerInv))
					this.onCraftMatrixChanged(calculInput);
				else if(((Slot)inventorySlots.get(par1)).inventory == uncraftIn)
					this.onCraftMatrixChanged(uncraftIn);
			}
		}
		return r;
	}


	/**
     * Callback for when the crafting gui is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        if(playerInv.getItemStack() != null)
        {
        	par1EntityPlayer.entityDropItem(playerInv.getItemStack(),0.5f);
        }
        if (!this.worldObj.isRemote)
        {
        	ItemStack itemstack = this.uncraftIn.getStackInSlotOnClosing(0);
        	if (itemstack != null)
        	{
        		par1EntityPlayer.entityDropItem(itemstack,0.5f);
        	}
        	
        	itemstack = this.calculInput.getStackInSlotOnClosing(0);
        	if (itemstack != null)
        	{
        		par1EntityPlayer.entityDropItem(itemstack,0.5f);
        	}
        	for(int i = 0;i<uncraftOut.getSizeInventory();i++)
        	{
        		itemstack = this.uncraftOut.getStackInSlotOnClosing(i);
        		
        		if (itemstack != null)
        		{
        			par1EntityPlayer.entityDropItem(itemstack,0.5f);
        		}
        	}
        }
    }

    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);
        if(slot != null && slot.getHasStack())
        	if(slot.inventory.equals(calculInput))
        	{
        		ItemStack itemstack1 = slot.getStack();
        		slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        		if(!playerInv.addItemStackToInventory(itemstack1))
        		{
        			return null;
        		}
        		slot.putStack(null);
        	}
        	else if(slot.inventory.equals(uncraftIn))
        	{
        		if(slot.getHasStack())
        		{
        			if(!playerInv.addItemStackToInventory(slot.getStack()))
            		{
            			return null;
            		}
            		slot.putStack(null);
            		slot.onSlotChanged();
        		}
        	}
        	else if(slot.inventory.equals(playerInv))
        	{
        		Slot calcInput = null;
        		Slot uncraftSlot = null;
        		for(Object s : inventorySlots)
        		{
        			Slot s1 = (Slot)s;
        			if(s1.inventory.equals(calculInput))
        			{
        				calcInput = s1;
        			}
        			else if(s1.inventory.equals(uncraftIn))
        			{
        				uncraftSlot = s1;
        			}
        		}
        		if(calcInput != null)
        		{
        			if(calcInput.getStack() == null)
        			{
        				calcInput.putStack(slot.getStack());
        				calcInput.onSlotChanged();
        				slot.putStack(null);
        			}
        			else
        			{
        				if(slot.getStack() != null)
                		{
        					ItemStack i = slot.getStack();
        					slot.onPickupFromSlot(par1EntityPlayer, slot.getStack());
        					slot.putStack(calcInput.getStack().copy());
        					calcInput.putStack(i.copy());
        					this.onCraftMatrixChanged(calculInput);
        					calcInput.onSlotChanged();
                		}
        				else
        				{
        					return null;
        				}
        			}
        		}
        	}
        	else if(slot.inventory.equals(uncraftOut))
        	{
        		if(slot.getHasStack())
        		{
        			if(!playerInv.addItemStackToInventory(slot.getStack()))
            		{
            			return null;
            		}
            		slot.putStack(null);
            		slot.onSlotChanged();
        		}
        	}
        return null;
    }

    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
    {
        return !par2Slot.inventory.equals(uncraftOut);
    }
    
    public Slot getSlot(int par1)
    {
    	if(par1 >= this.inventorySlots.size())
    		par1 = this.inventorySlots.size()-1;
    	return super.getSlot(par1);
    }

}