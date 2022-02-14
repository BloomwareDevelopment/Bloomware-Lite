package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class EventRender extends Event {
    MatrixStack matrixStack;
    private final int i;
    private final int j;
    private final float f;

    public EventRender(MatrixStack matrixStack, int i, int j, float f) {
        this.matrixStack = matrixStack;
        this.i = i;
        this.j = j;
        this.f = f;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public float getF() {
        return f;
    }
}
