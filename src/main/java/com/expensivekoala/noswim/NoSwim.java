package com.expensivekoala.noswim;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod(modid = NoSwim.MODID, version = NoSwim.VERSION)
public class NoSwim {
    public static final String MODID = "noswim";
    public static final String VERSION = "1.0";

    public static Configuration cfg;
    public static boolean enableAquaAffinity;
    public static boolean disableAllSwimming;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        cfg = new Configuration(new File(event.getModConfigurationDirectory().getPath(), "noswim.cfg"));
        readConfig();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        if(cfg.hasChanged()) cfg.save();
    }

    @SubscribeEvent
    public void updateEvent(LivingEvent.LivingUpdateEvent event)
    {
        if(!disableAllSwimming && event.getEntity() instanceof EntityPlayer && event.getEntity().getEntityWorld().isRemote && event.getEntity().isInWater() && Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && !((EntityPlayer) event.getEntity()).isCreative())
        {
            if(enableAquaAffinity && EnchantmentHelper.getEnchantments(((EntityPlayer)event.getEntity()).inventory.armorInventory.get(0)).containsKey(Enchantments.AQUA_AFFINITY))
                return;
            event.getEntity().motionY -= 0.03D;
        }
    }

    @SubscribeEvent
    public void inputEvent(InputUpdateEvent event) {
        if(disableAllSwimming && event.getEntityPlayer().isInWater() && !event.getEntityPlayer().isCreative()) {
            if(enableAquaAffinity && EnchantmentHelper.getEnchantments(event.getEntityPlayer().inventory.armorInventory.get(0)).containsKey(Enchantments.AQUA_AFFINITY))
                return;
            event.getMovementInput().jump = false;
        }
    }

    public void readConfig() {
        try {
            cfg.load();

            enableAquaAffinity = cfg.getBoolean("enableAquaAffinity", "general", true, "Enables swimming if boots have Aqua Affinity");
            disableAllSwimming = cfg.getBoolean("disableAllSwimming", "general", false, "If true, disables jump input. No more flying to get out, or using other faster swimming mod to get out.");
        } catch (Exception e) {
            System.err.println("Error loading configs");
        } finally {
            if(cfg.hasChanged()) {
                cfg.save();
            }
        }
    }
}
