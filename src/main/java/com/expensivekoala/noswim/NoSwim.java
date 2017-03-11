package com.expensivekoala.noswim;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = NoSwim.MODID, version = NoSwim.VERSION)
public class NoSwim {
    public static final String MODID = "noswim";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void inputEvent(LivingEvent.LivingUpdateEvent event)
    {
        if(event.getEntity() instanceof EntityPlayer && event.getEntity().worldObj.isRemote && event.getEntity().isInWater() && Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && !((EntityPlayer) event.getEntity()).isCreative())
        {
            event.getEntity().motionY -= 0.03D;
        }
    }
}
