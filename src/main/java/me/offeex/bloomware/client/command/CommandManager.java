package me.offeex.bloomware.client.command;

import me.offeex.bloomware.api.util.ClassUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
	
	public static List<Command> commands = new ArrayList<>();
	public static String prefix = "$";

	public static String ARROW = "" + Formatting.GRAY + Formatting.BOLD + " ➜ ";
	public static String USAGE = "" + Formatting.GRAY + Formatting.BOLD + "Usage: ";

	ArrayList<Command> all = new ArrayList<>();
	
	public CommandManager() {
		ArrayList<Class<?>> classes = new ArrayList<>();
		try {
			classes = ClassUtil.getClassesOther("me.offeex.bloomware.client.command.commands", Command.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		classes.forEach(aClass -> {
			try {
				all.add((Command) aClass.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});

		all.forEach(cmd -> {
			if (getCommand(cmd.getName()) == null) commands.add(cmd);
		});
	}

	public static void addChatMessage(String message) {
		Text textComponentString = new LiteralText(message);
		String prefix = "" + Formatting.BLACK + Formatting.BOLD + "<" + Formatting.LIGHT_PURPLE + Formatting.BOLD + "Bloomware" + Formatting.BLACK + Formatting.BOLD + "> ";
		MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText( prefix + Formatting.GRAY + Formatting.BOLD + "▸ ").append(textComponentString));
	}

	public static void addChatMessagef(String format, Object... o) {
		addChatMessage(String.format(format, o));
	}
	
	public static void callCommandReturn(String input) {
        if (!input.startsWith(prefix)) return;
        
        input = input.substring(prefix.length());
        if (input.split(" ").length > 0) {
        	boolean commandFound = false;
        	String commandName = input.split(" ")[0];
        	for (Command c : commands) {
        		if (c.getAliases().contains(commandName) || c.getName().equalsIgnoreCase(commandName)) {
	        		c.onCommand(Arrays.copyOfRange(input.split(" "), 1, input.split(" ").length), input);
	        		commandFound = true;
	        		break;
        		}
        	}
        	
        	if (!commandFound) {
        		addChatMessage(Formatting.DARK_RED + "command doesn't exist. Type " + Formatting.RED + Formatting.ITALIC + prefix + "help " + Formatting.RESET + "" + Formatting.DARK_RED + "to see all commands.");
        	}
        }
    }

	public static Command getCommand(String name) {
		return commands.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findAny().orElse(null);
	}

	public static void setCommandPrefix(String pre) {
        prefix = pre;
    }
	public static String getPrefix() {
		return prefix;
	}
}