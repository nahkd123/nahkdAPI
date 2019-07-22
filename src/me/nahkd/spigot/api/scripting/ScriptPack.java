package me.nahkd.spigot.api.scripting;

import java.util.ArrayList;
import java.util.HashMap;

public class ScriptPack {
	
	public String name;
	public String displayName;
	public String author;
	public String version;
	
	public HashMap<String, ArrayList<Script>> eventsListeners;
	public HashMap<String, ScriptPackCommand> commands;
	
	public ScriptPack(String name, String displayName, String author, String version) {
		this.name = name;
		this.displayName = displayName;
		this.author = author;
		this.version = version;
		
		this.eventsListeners = new HashMap<String, ArrayList<Script>>();
		this.commands = new HashMap<String, ScriptPackCommand>();
	}
	
}
