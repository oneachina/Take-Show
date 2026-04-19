package cn.oneachina.takeshow.client;

import cn.oneachina.takeshow.client.render.ResonantPickaxeClient;
import net.fabricmc.api.ClientModInitializer;

public class TakeshowClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ResonantPickaxeClient.register();
    }
}
