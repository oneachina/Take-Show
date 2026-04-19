package cn.oneachina.takeshow.datagen;

import cn.oneachina.takeshow.registry.TakeshowItems;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

public class TakeshowRecipeProvider extends FabricRecipeProvider {
    public TakeshowRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput output) {
        return new RecipeProvider(registryLookup, output) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.TOOLS, TakeshowItems.ECHO_CHARM)
                        .pattern(" A ")
                        .pattern("SES")
                        .pattern(" C ")
                        .define('A', Items.AMETHYST_SHARD)
                        .define('S', Items.STRING)
                        .define('E', Items.ECHO_SHARD)
                        .define('C', Items.SCULK_SENSOR)
                        .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                        .save(output);

                shaped(RecipeCategory.TOOLS, TakeshowItems.RESONANT_PICKAXE)
                        .pattern("EE ")
                        .pattern(" S ")
                        .pattern(" S ")
                        .define('E', Items.ECHO_SHARD)
                        .define('S', Items.STICK)
                        .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "TakeShowRecipeProvider";
    }
}
