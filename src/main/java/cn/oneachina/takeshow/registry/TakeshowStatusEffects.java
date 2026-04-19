package cn.oneachina.takeshow.registry;

import cn.oneachina.takeshow.Takeshow;
import cn.oneachina.takeshow.status.EchoDistortionStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public final class TakeshowStatusEffects {

    public static final Holder<MobEffect> ECHO_DISTORTION = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            Identifier.fromNamespaceAndPath(Takeshow.MOD_ID, "echo_distortion"),
            new EchoDistortionStatusEffect()
    );

    private TakeshowStatusEffects() {
    }

    public static void register() {
    }
}
