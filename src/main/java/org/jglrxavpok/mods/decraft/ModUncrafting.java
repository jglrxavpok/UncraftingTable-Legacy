package org.jglrxavpok.mods.decraft;

import java.io.*;
import java.util.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.block.*;
import net.minecraft.client.resources.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;

import org.apache.logging.log4j.*;

@Mod(modid = "uncraftingTable", name = "jglrxavpok's UncraftingTable", version = "1.4")
/**
 * Principal class of the mod. Used to handle crafting of the table & some of the new achievements.
 * @author jglrxavpok
 */
public class ModUncrafting
{

    @Instance("uncraftingTable")
    public static ModUncrafting      instance;

    /**
     * The block. Obviously :)
     */
    public Block                     uncraftingTable;
    public static final UnGuiHandler guiHandler = new UnGuiHandler();

    public Achievement               craftTable;
    public Achievement               uncraftAny;
    private Achievement              uncraftDiamondHoe;
    private Achievement              uncraftJunk;
    private Achievement              uncraftDiamondShovel;
    /**
     * Requested by a very special person to me! 
     * @see org.jglrxavpok.mods.decraft.BlockUncraftingTable
     */
    public Achievement               porteManteauAchievement;
    /**
     * Number of uncrafted items
     */
    public StatBasic                 uncraftedItemsStat;

    private File                     cfgFile;
    public static int                uncraftMethod;
    public static int                maxUsedLevel;
    public static int                standardLevel;
    private Properties               props;
    public int                       minLvlServer;
    public int                       maxLvlServer;

    private Logger                   logger;

    public Logger getLogger()
    {
        return logger;
    }

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
    public void onSuccessedUncrafting(SuccessedUncraftingEvent event)
    {
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
            catch(Exception e)
            {
                e.printStackTrace();
                standardLevel = 0;
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
            catch(Exception e)
            {
                e.printStackTrace();
                maxUsedLevel = 0;
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
            catch(Exception e)
            {
                e.printStackTrace();
                uncraftMethod = 0;
            }

            if(flag)
            {
                props.store(new FileOutputStream(cfgFile), "jglrxavpok's uncrafting table properties");
            }
        }
        catch(Exception e)
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
        logger = event.getModLog();
        cfgFile = event.getSuggestedConfigurationFile();
        if(!cfgFile.getParentFile().exists())
            cfgFile.getParentFile().mkdirs();
        if(!cfgFile.exists())
            try
            {
                cfgFile.createNewFile();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        parseProps();
        MinecraftForge.EVENT_BUS.register(this);
        uncraftingTable = new BlockUncraftingTable();
        GameRegistry.registerBlock(uncraftingTable, ItemUncraftingTableBlock.class, "uncrafting_table");
        GameRegistry.addShapedRecipe(new ItemStack(uncraftingTable), new Object[]
        {
                "SSS", "SXS", "SSS", 'X', Blocks.crafting_table, 'S', Blocks.cobblestone
        });
        craftTable = new Achievement("createDecraftTable", "createDecraftTable", 1 - 2 - 2, -1 - 3, uncraftingTable, null).registerStat();
        uncraftAny = (Achievement) new Achievement("uncraftAnything", "uncraftAnything", 2 - 2, -2 - 2, Items.diamond_hoe, craftTable).registerStat();
        uncraftDiamondHoe = (Achievement) new Achievement("uncraftDiamondHoe", "uncraftDiamondHoe", 2 - 2, 0 - 2, Items.diamond_hoe, uncraftAny).registerStat();
        uncraftJunk = (Achievement) new Achievement("uncraftJunk", "uncraftJunk", 1 - 2, -1 - 2, Items.leather_boots, uncraftAny).registerStat();
        uncraftDiamondShovel = (Achievement) new Achievement("uncraftDiamondShovel", "uncraftDiamondShovel", 3 - 2, -1 - 2, Items.diamond_shovel, uncraftAny).registerStat();
        porteManteauAchievement = (Achievement) new Achievement("porteManteauAchievement", "porteManteauAchievement", 3 - 2, -4 - 2, Blocks.fence, craftTable).registerStat();
        //        AchievementPage.registerAchievementPage(new AchievementPage("Uncrafting Table", 
        //                new Achievement[]{createTable, uncraftAny, uncraftDiamondHoe, uncraftJunk, uncraftDiamondShovel, porteManteauAchievement}));

        uncraftedItemsStat = (StatBasic) (new StatBasic("stat.uncrafteditems", new ChatComponentTranslation("stat.uncrafteditems", new Object[0])).registerStat());
    }

    public String getStringAndSetXPLevels(float sliderValue, int valueType)
    {
        String maxLevel = I18n.format("uncrafting.options.lvl.max");
        if(valueType == GuiSlider2.MAX_XP_LEVEL)
        {
            float lvl = ((float) (sliderValue * 50)) + 10;
            if(lvl < 5 + standardLevel)
                lvl = 5 + standardLevel;

            ModUncrafting.maxUsedLevel = (int) lvl;
            return maxLevel + ": " + (int) lvl;
        }
        String minLevel = I18n.format("uncrafting.options.lvl.min");
        if(valueType == GuiSlider2.MIN_XP_LEVEL)
        {
            int lvl = (int) ((sliderValue * 35f)) + 5;
            ModUncrafting.standardLevel = lvl;

            return minLevel + ": " + lvl;
        }
        return "UNKNOWN";
    }

    public boolean isValuePossible(float sliderValue, int valueType)
    {
        if(valueType == GuiSlider2.MAX_XP_LEVEL)
        {
            float lvl = ((float) (sliderValue * 50)) + 10;
            if(lvl < 5 + standardLevel)
                return false;
        }
        return true;
    }

}
