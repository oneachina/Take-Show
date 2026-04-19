package cn.oneachina.takeshow.datagen;

import cn.oneachina.takeshow.Takeshow;
import cn.oneachina.takeshow.registry.TakeshowItems;
import cn.oneachina.takeshow.registry.TakeshowStatusEffects;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class TakeshowAdvancementProvider extends FabricAdvancementProvider {
    public TakeshowAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        HolderLookup.RegistryLookup<Item> itemLookup = registryLookup.lookupOrThrow(Registries.ITEM);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        TakeshowItems.ECHO_CHARM,
                        Component.translatable("advancements.takeshow.root.title"),
                        Component.translatable("advancements.takeshow.root.description"),
                        Identifier.fromNamespaceAndPath("minecraft", "gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion("has_echo_charm", InventoryChangeTrigger.TriggerInstance.hasItems(TakeshowItems.ECHO_CHARM))
                .save(consumer, Takeshow.MOD_ID + ":root");

        Criterion<?> hasEchoDistortion = EffectsChangedTrigger.TriggerInstance.hasEffects(
                MobEffectsPredicate.Builder.effects().and(TakeshowStatusEffects.ECHO_DISTORTION)
        );

        AdvancementHolder ping = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.ECHO_SHARD,
                        Component.translatable("advancements.takeshow.ping.title"),
                        Component.translatable("advancements.takeshow.ping.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("echo_distortion", hasEchoDistortion)
                .save(consumer, Takeshow.MOD_ID + ":ping");

        Criterion<?> killedGlowing = KilledTrigger.TriggerInstance.playerKilledEntity(
                EntityPredicate.Builder.entity().effects(MobEffectsPredicate.Builder.effects().and(MobEffects.GLOWING))
        );

        Advancement.Builder.advancement()
                .parent(ping)
                .display(
                        Items.GLOW_INK_SAC,
                        Component.translatable("advancements.takeshow.no_hiding.title"),
                        Component.translatable("advancements.takeshow.no_hiding.description"),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("killed_glowing", killedGlowing)
                .save(consumer, Takeshow.MOD_ID + ":no_hiding");

        AdvancementHolder resonantPickaxe = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        TakeshowItems.RESONANT_PICKAXE,
                        Component.translatable("advancements.takeshow.resonant_pickaxe.title"),
                        Component.translatable("advancements.takeshow.resonant_pickaxe.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_resonant_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(TakeshowItems.RESONANT_PICKAXE))
                .save(consumer, Takeshow.MOD_ID + ":resonant_pickaxe");

        ItemPredicate resonantPickaxePredicate = ItemPredicate.Builder.item().of(itemLookup, TakeshowItems.RESONANT_PICKAXE).build();
        Criterion<?> firstPulse = ItemDurabilityTrigger.TriggerInstance.changedDurability(
                Optional.of(resonantPickaxePredicate),
                MinMaxBounds.Ints.atLeast(1)
        );

        Advancement.Builder.advancement()
                .parent(resonantPickaxe)
                .display(
                        Items.ECHO_SHARD,
                        Component.translatable("advancements.takeshow.first_pulse.title"),
                        Component.translatable("advancements.takeshow.first_pulse.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("first_pulse", firstPulse)
                .save(consumer, Takeshow.MOD_ID + ":first_pulse");
    }

    @Override
    public String getName() {
        return "TakeShowAdvancementProvider";
    }
}
