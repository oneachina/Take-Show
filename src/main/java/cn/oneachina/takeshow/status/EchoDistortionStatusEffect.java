package cn.oneachina.takeshow.status;

import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EchoDistortionStatusEffect extends MobEffect {

    private static final UUID MOVEMENT_SPEED_MODIFIER_ID = UUID.fromString("c3cd5b8f-e5a9-4e8b-a9d6-1e3a4d88f1c0");

    public EchoDistortionStatusEffect() {
        super(MobEffectCategory.HARMFUL, 0x3B2F5B);
        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                Identifier.withDefaultNamespace(MOVEMENT_SPEED_MODIFIER_ID.toString()),
                -0.25,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
    }
}
