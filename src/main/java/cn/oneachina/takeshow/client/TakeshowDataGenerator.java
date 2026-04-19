package cn.oneachina.takeshow.client;

import cn.oneachina.takeshow.datagen.TakeshowAdvancementProvider;
import cn.oneachina.takeshow.datagen.TakeshowDynamicRegistryProvider;
import cn.oneachina.takeshow.datagen.TakeshowRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TakeshowDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(TakeshowAdvancementProvider::new);
        pack.addProvider(TakeshowDynamicRegistryProvider::new);
        pack.addProvider(TakeshowRecipeProvider::new);
    }
}
