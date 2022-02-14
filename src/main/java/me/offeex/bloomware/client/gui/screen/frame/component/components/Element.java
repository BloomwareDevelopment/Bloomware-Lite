package me.offeex.bloomware.client.gui.screen.frame.component.components;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventDrawOverlay;
import me.offeex.bloomware.api.util.ColorUtils;
import me.offeex.bloomware.client.gui.IDraggable;
import me.offeex.bloomware.client.gui.screen.ClickGUI;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class Element implements IDraggable {
	private final Module module;
	private boolean isDragging;
	private int x, y;
	public int dragX, dragY, width, height;
	MinecraftClient mc = MinecraftClient.getInstance();

	public Element(Module module) {
		this.module = module;
		this.x = module.x;
		this.y = module.y;
		this.isDragging = false;
		Bloomware.EVENTBUS.register(this);
	}

	@Subscribe
	public void onDrawOverlay(EventDrawOverlay event) {
		if (mc.currentScreen == Bloomware.gui) return;
		this.draw(event.getMatrix(), (int) MinecraftClient.getInstance().mouse.getX(), (int) MinecraftClient.getInstance().mouse.getY(), MinecraftClient.getInstance().getTickDelta());
	}

	public void draw(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
		module.x = x;
		module.y = y;
		width = module.width;
		height = module.height;

		if (module.isEnabled()) {
			try {
				module.draw(stack, mouseX, mouseY, tickDelta);
			} catch (Exception ignored) {}
		}

		if (mc.currentScreen == Bloomware.hud && module.isEnabled()) {
			DrawableHelper.fill(stack, x, y, x + width, y + height, ColorUtils.withAlpha(ColorUtils.getColor("ElementFrame"), 50));
		}
	}

	public void updatePosition(final int mouseX, final int mouseY) {
		if (this.isDragging) {
			this.setX(Math.max(0, Math.min(mc.getWindow().getScaledWidth() - this.width, mouseX - dragX)));
			this.setY(Math.max(0, Math.min(mc.getWindow().getScaledHeight() - this.height, mouseY - dragY)));
		}
	}

	public void setDrag(final boolean drag) {
		if (drag && ClickGUI.dragging == null) {
			ClickGUI.dragging = this;
			this.isDragging = true;
		}
		else {
			if (ClickGUI.dragging == this)
				ClickGUI.dragging = null;
			this.isDragging = false;
		}
	}

	public boolean isHover(final double x, final double y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
	}

	public Module getModule() {
		return module;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(final int newX) {
		this.x = newX;
	}

	public void setY(final int newY) {
		this.y = newY;
	}
}
