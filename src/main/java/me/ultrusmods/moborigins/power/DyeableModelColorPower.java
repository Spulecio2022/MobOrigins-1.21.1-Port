package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.power.type.PowerType;
import net.minecraft.world.entity.LivingEntity;
import io.github.apace100.apoli.power.PowerConfiguration;
import org.jetbrains.annotations.NotNull;


public class DyeableModelColorPower extends PowerType {

    private float red, green, blue, alpha;

    public DyeableModelColorPower(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        throw new UnsupportedOperationException("DyeableModelColorPower is runtime-only and has no config.");
    }

    public float getRed()   { return red; }
    public float getGreen() { return green; }
    public float getBlue()  { return blue; }
    public float getAlpha() { return alpha; }

    public void blendColor(float[] col) {
        this.red   = (this.red   + col[0]) / 2f;
        this.green = (this.green + col[1]) / 2f;
        this.blue  = (this.blue  + col[2]) / 2f;
    }
    public void applyColor(LivingEntity entity) {
        // optional: put logic here later
    }
}