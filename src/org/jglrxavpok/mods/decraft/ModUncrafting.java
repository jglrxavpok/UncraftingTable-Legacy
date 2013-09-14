package org.jglrxavpok.mods.decraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBasic;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "xavpoksDecraft", name = "jglrxavpok's UncraftingTable", version = "1.3")


@NetworkMod(clientSideRequired=true, serverSideRequired=true,

clientPacketHandlerSpec = @SidedPacketHandler(channels = {"Uncrafting" }, packetHandler = ClientPacketHandler.class),

serverPacketHandlerSpec = @SidedPacketHandler(channels = {"Uncrafting" }, packetHandler = ServerPacketHandler.class))


/**
 * Principal class of the mod. Used to handle crafting of the table & some of the new achievements.
 * @author jglrxavpok
 */
public class ModUncrafting
{
	
	public static class UnCraftingHandler implements ICraftingHandler
	{

		@Override
		public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
		{
			if(item.getItem().itemID == uncraftingTable.blockID)
			{
				player.triggerAchievement(modInstance.createTable);
			}
		}
		@Override
		public void onSmelting(EntityPlayer player, ItemStack item)
		{}
		
	}
	
	@Instance("xavpoksDecraft")
	public static ModUncrafting modInstance;
	@SidedProxy(clientSide="org.jglrxavpok.mods.decraft.ClientProxy", serverSide="org.jglrxavpok.mods.decraft.CommonProxy")
	public static CommonProxy proxy;
	
	/**
	 * The block. Obviously :)
	 */
	public static Block uncraftingTable;
	public static final UnGuiHandler guiHandler = new UnGuiHandler();

	
	private Achievement createTable;
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
	private static int blockID;
	public static int uncraftMethod;
	public static int maxUsedLevel;
	public static int standardLevel;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
		GameRegistry.registerBlock(uncraftingTable, "Uncrafting Table");
		GameRegistry.addShapedRecipe(new ItemStack(uncraftingTable), new Object[]{"SSS", "SXS", "SSS", 'X', Block.workbench, 'S', Block.cobblestone});
		GameRegistry.registerCraftingHandler(new UnCraftingHandler());
		MinecraftForge.EVENT_BUS.register(this);
		proxy.init();
		
		DefaultsRecipeHandlers.load();
		
		System.out.println("[Uncrafting Table] The mod has been initialized!");
	}
	
	@ForgeSubscribe
	public void onUncrafting(UncraftingEvent event)
	{
		
	}
	
	@ForgeSubscribe
	public void onSuccessedUncrafting(SuccessedUncraftingEvent event)
	{
		int itemID = event.getUncrafted().getItem().itemID;
		if(itemID == Item.hoeDiamond.itemID)
		{
			event.getPlayer().triggerAchievement(uncraftDiamondHoe);
		}
		if(itemID == Item.shovelDiamond.itemID)
		{
			event.getPlayer().triggerAchievement(uncraftDiamondShovel);
		}
		if(itemID == Item.bootsLeather.itemID)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Item.helmetLeather.itemID)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Item.legsLeather.itemID)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Item.plateLeather.itemID)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
		else if(itemID == Item.glassBottle.itemID)
		{
			event.getPlayer().triggerAchievement(uncraftJunk);
		}
	}

	private static void parseProps() 
	{
		Properties props = new Properties();
		try 
		{
			boolean flag = false;
			props.load(new FileInputStream(cfgFile));
			String s = props.getProperty("decraftTableID");
			if(s == null)
			{
				System.out.println("[Uncrafting Table] UncraftingTable BlockID not found, generating new one");
				s = "243";
				props.setProperty("decraftTableID", s);
				flag = true;
			}
			try
			{
				blockID = Integer.parseInt(s);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				blockID =  243;
			}
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
			blockID = 243;
			uncraftMethod = 0;
		}
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
		uncraftingTable = new BlockUncraftingTable(blockID).func_111022_d("xavpoksdecraft:uncraftingtable").setUnlocalizedName("uncraftingtable");
		createTable = (Achievement) new Achievement(0x507AB,"createDecraftTable",0,-2,uncraftingTable,null).registerStat();
		uncraftAny = (Achievement) new Achievement(0x507AB+1,"uncraftAnything",2,-2,Item.hoeDiamond,createTable).registerStat();
		uncraftDiamondHoe = (Achievement) new Achievement(0x507AB+2,"uncraftDiamondHoe",2,0,Item.hoeDiamond,uncraftAny).registerStat();
		uncraftJunk = (Achievement) new Achievement(0x507AB+3,"uncraftJunk",1,-1,Item.bootsLeather,uncraftAny).registerStat();
		uncraftDiamondShovel = (Achievement) new Achievement(0x507AB+4,"uncraftDiamondShovel",3,-1,Item.shovelDiamond,uncraftAny).registerStat();
		porteManteauAchievement = (Achievement) new Achievement(0x507AB+5,"porteManteauAchievement",3,-4,Block.fence,createTable).registerStat();
		AchievementPage.registerAchievementPage(new AchievementPage("Uncrafting Table", 
				new Achievement[]{createTable, uncraftAny, uncraftDiamondHoe, uncraftJunk, uncraftDiamondShovel, porteManteauAchievement}));
		uncraftedItemsStat = (StatBasic)(new StatBasic(0x507AB, "stat.uncrafteditems").registerStat());
	}

}
