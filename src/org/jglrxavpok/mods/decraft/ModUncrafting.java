package org.jglrxavpok.mods.decraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "xavpoksDecraft", name = "jglrxavpok's UncraftingTable", version = "1.4")

/**
 * Principal class of the mod. Used to handle crafting of the table & some of the new achievements.
 * @author jglrxavpok
 */
public class ModUncrafting
{
	
	@Instance("xavpoksDecraft")
	public static ModUncrafting modInstance;
	
	/**
	 * The block. Obviously :)
	 */
	public static Block uncraftingTable;
	public static final UnGuiHandler guiHandler = new UnGuiHandler();

	
	public Achievement placeTable;
	public Achievement uncraftAny;
	private Achievement	uncraftDiamondHoe;
	private Achievement	uncraftJunk;
	private Achievement	uncraftDiamondShovel;
	/**
	 * Requested by a very special person to me! 
	 * @see org.jglrxavpok.mods.decraft.BlockUncraftingTable
	 */
	public Achievement	porteManteauAchievement;
	/**
	 * Number of uncrafted items
	 */
	public StatBasic	uncraftedItemsStat;

	private static File cfgFile;
	public static int uncraftMethod;
	public static int maxUsedLevel;
	public static int standardLevel;
	private Properties	props;
	public int	minLvlServer;
	public int	maxLvlServer;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
		DefaultsRecipeHandlers.load();
		
		System.out.println("[Uncrafting Table] The mod has been initialized!");
	}
	
	@SubscribeEvent
	public void onUncrafting(UncraftingEvent event)
	{
		
	}
	
	@SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent e)
    {
        ItemStack item = e.crafting;
        EntityPlayer player = e.player;
        System.out.println(item.getItem().getUnlocalizedName());
        if(item.getItem().getUnlocalizedName().equals(uncraftingTable.getUnlocalizedName()))
        {
            player.triggerAchievement(modInstance.placeTable);
        }
    }
    
	
	@SubscribeEvent
	public void onSuccessedUncrafting(SuccessedUncraftingEvent event)
	{
	    event.getPlayer().addStat(uncraftedItemsStat, 1);
		Item itemID = event.getUncrafted().getItem();
		if(itemID == Items.diamond_hoe)
		{
			event.getPlayer().triggerAchievement(uncraftDiamondHoe);
		}
		if(itemID == Items.diamond_shovel)
		{
			event.getPlayer().triggerAchievement(uncraftDiamondShovel);
		}
		if(itemID == Items.leather_leggings)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Items.leather_helmet)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Items.leather_boots)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Items.leather_chestplate)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Items.glass_bottle)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
	}
	
	public void saveProperties()
	{
		try
		{
			props.store(new FileOutputStream(cfgFile), "jglrxavpok's uncrafting table properties");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void parseProps() 
	{
		props = new Properties();
		try 
		{
			boolean flag = false;
			props.load(new FileInputStream(cfgFile));
			String s;
			s = props.getProperty("standardLevel");
			if(s == null)
			{
				s = "5";
				props.setProperty("standardLevel", s);
				flag = true;
			}
			try
			{
				standardLevel = Integer.parseInt(s);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				standardLevel =  0;
			}
			s = props.getProperty("maxUsedLevel");
			if(s == null)
			{
				s = "30";
				props.setProperty("maxUsedLevel", s);
				flag = true;
			}
			try
			{
				maxUsedLevel = Integer.parseInt(s);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				maxUsedLevel =  0;
			}
			s = props.getProperty("uncraftMethod");
			if(s == null)
			{
				System.out.println("[Uncrafting Table] Uncrafting Method ID not found, generating new one");
				s = "1";
				props.setProperty("uncraftMethod", s);
				flag = true;
			}
			try
			{
				uncraftMethod = Integer.parseInt(s);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				uncraftMethod =  0;
			}
			
			if(flag)
			{
				props.store(new FileOutputStream(cfgFile), "jglrxavpok's uncrafting table properties");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			uncraftMethod = 0;
		}
		
		
		minLvlServer = standardLevel;
		maxLvlServer = maxUsedLevel;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		cfgFile = event.getSuggestedConfigurationFile();
		if(!cfgFile.getParentFile().exists())
			cfgFile.getParentFile().mkdirs();
		if(!cfgFile.exists())
			try 
			{
				cfgFile.createNewFile();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		parseProps();
		MinecraftForge.EVENT_BUS.register(this);
		uncraftingTable = new BlockUncraftingTable();
        GameRegistry.registerBlock(uncraftingTable, "uncrafting_table");
        GameRegistry.addShapedRecipe(new ItemStack(uncraftingTable), new Object[]{"SSS", "SXS", "SSS", 'X', Blocks.crafting_table, 'S', Blocks.cobblestone});
        placeTable = new Achievement("createDecraftTable","createDecraftTable",1-2-2,-1-3,uncraftingTable, null).registerStat();
        uncraftAny = (Achievement) new Achievement("uncraftAnything","uncraftAnything",2-2,-2-2,Items.diamond_hoe,placeTable).registerStat();
        uncraftDiamondHoe = (Achievement) new Achievement("uncraftDiamondHoe","uncraftDiamondHoe",2-2,0-2,Items.diamond_hoe,uncraftAny).registerStat();
        uncraftJunk = (Achievement) new Achievement("uncraftJunk","uncraftJunk",1-2,-1-2,Items.leather_boots,uncraftAny).registerStat();
        uncraftDiamondShovel = (Achievement) new Achievement("uncraftDiamondShovel","uncraftDiamondShovel",3-2,-1-2,Items.diamond_shovel,uncraftAny).registerStat();
        porteManteauAchievement = (Achievement) new Achievement("porteManteauAchievement","porteManteauAchievement",3-2,-4-2,Blocks.fence,placeTable).registerStat();
//        AchievementPage.registerAchievementPage(new AchievementPage("Uncrafting Table", 
//                new Achievement[]{createTable, uncraftAny, uncraftDiamondHoe, uncraftJunk, uncraftDiamondShovel, porteManteauAchievement}));

		uncraftedItemsStat = (StatBasic)(new StatBasic("stat.uncrafteditems", new ChatComponentTranslation("stat.uncrafteditems", new Object[0])).registerStat());
	}

	public String getStringAndSetXPLevels(float sliderValue, int valueType)
	{
		String maxLevel = StatCollector.translateToLocal("uncrafting.options.lvl.max");
		if(maxLevel == null || maxLevel.equals("uncrafting.options.lvl.max"))
		{
			maxLevel = "Max XP level required";
		}
		if(valueType == GuiSlider2.MAX_XP_LEVEL)
		{
			float lvl = ((float)(sliderValue*50))+10;
			if(lvl < 5+standardLevel)
				lvl = 5+standardLevel;
			
			this.maxUsedLevel = (int)lvl;
			return maxLevel+": "+(int)lvl;
		}
		String minLevel = StatCollector.translateToLocal("uncrafting.options.lvl.min");
		if(minLevel == null || minLevel.equals("uncrafting.options.lvl.min"))
		{
			minLevel = "Min XP level required";
		}
		if(valueType == GuiSlider2.MIN_XP_LEVEL)
		{
			int lvl = (int)((sliderValue*35f))+5;
			this.standardLevel = lvl;
			
			return minLevel+": "+lvl;
		}
		return "UNKNOWN";
	}

	public boolean isValuePossible(float sliderValue, int valueType)
	{
		if(valueType == GuiSlider2.MAX_XP_LEVEL)
		{
			float lvl = ((float)(sliderValue*50))+10;
			if(lvl < 5+standardLevel)
				return false;
		}
		return true;
	}

}
