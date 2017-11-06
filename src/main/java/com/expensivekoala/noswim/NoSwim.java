package com.expensivekoala.noswim;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "noswim", version = "1.3")
@Mod.EventBusSubscriber
@Config(modid = "noswim")
public class NoSwim {

    @Config.Comment("Enables swimming if boots have Depth Strider")
    public static boolean enableDepthStrider = true;

    @Config.Comment("If true, disables jump input. No more flying to get out, or using other faster swimming mod to get out.")
    public static boolean disableAllSwimming = false;

    @Config.Comment("Enables swimming if player has Water Breathing")
    public static boolean enableWaterBreathing = true;

    @Config.Comment("No Lava Swimming")
    public static boolean disableLavaSwim = true;

    @SubscribeEvent
    public static void updateEvent(LivingEvent.LivingUpdateEvent event) {
        if(!disableAllSwimming && event.getEntity() instanceof EntityPlayer && event.getEntity().getEntityWorld().isRemote && (event.getEntity().isInWater() || (disableLavaSwim && event.getEntity().isInLava())) && Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && !((EntityPlayer) event.getEntity()).isCreative()) {
            if(hasDepthStrider((EntityPlayer)event.getEntity()) || hasWaterBreathing((EntityPlayer)event.getEntity()))
                return;
            event.getEntity().motionY -= 0.03D;
        }
    }

    @SubscribeEvent
    public static void inputEvent(InputUpdateEvent event) {
        if(disableAllSwimming && (event.getEntityPlayer().isInWater() || (disableLavaSwim && event.getEntityPlayer().isInLava())) && !event.getEntityPlayer().isCreative()) {
            if(hasDepthStrider(event.getEntityPlayer()) || hasWaterBreathing(event.getEntityPlayer()))
                return;
            event.getMovementInput().jump = false;
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("noswim")) {
            ConfigManager.sync("noswim", Config.Type.INSTANCE);
        }
    }

    private static boolean hasDepthStrider(EntityPlayer player) {
        return enableDepthStrider && EnchantmentHelper.getEnchantments(player.inventory.armorInventory.get(0)).containsKey(Enchantments.DEPTH_STRIDER);
    }

    private static boolean hasWaterBreathing(EntityPlayer player) {
        return enableWaterBreathing && player.getActivePotionEffect(Potion.getPotionById(13)) != null;
    }
}