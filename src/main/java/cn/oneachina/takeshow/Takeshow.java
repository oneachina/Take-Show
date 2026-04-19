package cn.oneachina.takeshow;

import cn.oneachina.takeshow.event.LookWhatEvents;
import cn.oneachina.takeshow.event.ResonantPickaxeEvents;
import cn.oneachina.takeshow.network.TakeshowPayloads;
import cn.oneachina.takeshow.registry.TakeshowCreativeTabs;
import cn.oneachina.takeshow.registry.TakeshowEnchantments;
import cn.oneachina.takeshow.registry.TakeshowItems;
import cn.oneachina.takeshow.registry.TakeshowStatusEffects;
import net.fabricmc.api.ModInitializer;

public class Takeshow implements ModInitializer {

    public static final String MOD_ID = "takeshow";

    @Override
    public void onInitialize() {
        TakeshowPayloads.register();
        TakeshowStatusEffects.register();
        TakeshowEnchantments.register();
        TakeshowItems.register();
        TakeshowCreativeTabs.register();
        LookWhatEvents.register();
        ResonantPickaxeEvents.register();
    }
}
