package me.offeex.bloomware.client.command;

import me.offeex.bloomware.Bloomware;
import net.minecraft.client.MinecraftClient;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
	protected final MinecraftClient mc = Bloomware.mc;
	private final String name, description, modifier;
	private final List<String> aliases;

	public Command() {
		Register info = getClass().getAnnotation(Register.class);
		this.name = info.name();
		this.description = info.description();
		this.modifier = info.modifier();
		this.aliases = Arrays.asList(info.aliases());
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Register {
		String name();
		String description();
		String modifier();
		String[] aliases();
	}
	
	public abstract void onCommand(String[] args, String command);
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getAliases() {
		return aliases;
	}
}
