package me.nahkd.spigot.api.placeholder.placeholders;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;

public class MathPlaceholders extends PlaceholderListener {

	public MathPlaceholders() {
		super("math");
	}

	@Override
	public String onCalled(Player player, String dataString) {
		return calculateFromString(PlaceholderManager.parse(dataString, player)) + "";
	}
	
	public static double calculateFromString(String fomula) {
		double output = 0.0;
		// 1 + 2 = 3
		// 1 + (2 - 1) = 2 => 1+(2-1) = 2
		// Check inside '()' first
		ArrayList<String> childrenFomulas = new ArrayList<String>();
		
		int mode = 0;
		String outputFomula = "";
		String childrenFomula = "";
		int childIndex = 0;
		int pipe = 0;
		
		for (char c : fomula.toCharArray()) {
			if (c == ' ') {}
			else if (mode == 0) {
				if (c == '(') mode = 1;
				else outputFomula += c;
			} else if (mode == 1) {
				if (c == ')') {
					if (pipe == 0) {
						childrenFomulas.add(childrenFomula);
						outputFomula += "!" + childIndex;
						childIndex++;
						mode = 0;
					} else {
						childrenFomula += c;
						pipe--;
					}
				} else if (c == '(') {
					childrenFomula += c;
					pipe++;
				} else childrenFomula += c;
			}
		}
		
		if (childrenFomulas.size() > 0) for (int i = 0; i < childrenFomulas.size(); i++) {
			String fom = childrenFomulas.get(i);
			outputFomula = outputFomula.replace("!" + i, "" + MathPlaceholders.calculateFromString(fom));
		}
		// Now we only need to solve 1 + 2 problem
		// 1 + 2 * 1
		
		String finalShit = "";
		String temp = "";
		mode = 0;
		
		for (char c : outputFomula.toCharArray()) {
			if (mode == 0) {
				if (c == '+' || c == '-') {
					finalShit += temp + c;
					temp = "";
				} else if (c == '*' || c == '/') {
					mode = 1;
					temp += c;
				} else temp += c;
			} else if (mode == 1) {
				if (c == '+' || c == '-') {
					finalShit += calculateDump(temp) + "" + c;
					temp = "";
					mode = 0;
				} else temp += c;
			}
		}
		
		if (mode == 0) finalShit += temp;
		else if (mode == 1) finalShit += calculateDump(temp);

		//System.out.println(finalShit);
		// finalShit: The fomula with only plus and minus

		temp = "";
		mode = 0;
		
		for (char c : finalShit.toCharArray()) {
			if (mode == 0) {
				// Plus
				if (c == '+') {
					output += Double.parseDouble(temp);
					temp = "";
				} else if (c == '-') {
					output += Double.parseDouble(temp);
					temp = "";
					mode = 1;
				} else temp += c;
			} else if (mode == 1) {
				// Minus
				if (c == '+') {
					output -= Double.parseDouble(temp);
					temp = "";
					mode = 0;
				} else if (c == '-') {
					output -= Double.parseDouble(temp);
					temp = "";
				} else temp += c;
			}
		}
		//System.out.println(output);
		if (mode == 0) output += Double.parseDouble(temp);
		else if (mode == 1) output -= Double.parseDouble(temp);
		return output;
	}
	
	private static double calculateDump(String mdiv) {
		double a = 0.0;
		
		String temp = "";
		int mode = 0;
		boolean declaredLeftSide = false;
		
		for (char c : mdiv.toCharArray()) {
			if (mode == 0) {
				if (c == '*') {
					if (!declaredLeftSide) {
						a = Double.parseDouble(temp);
					}
					temp = "";
					mode = 1;
				} else if (c == '/') {
					if (!declaredLeftSide) {
						a = Double.parseDouble(temp);
					}
					temp = "";
					mode = 2;
				} else temp += c;
			} else if (mode == 1) {
				if (c == '*') {
					// Keep mode
					a = a * Double.parseDouble(temp);
					temp = "";
				} else if (c == '/') {
					a = a / Double.parseDouble(temp);
					temp = "";
					mode = 2;
				} else temp += c;
			} else if (mode == 2) {
				if (c == '*') {
					a = a * Double.parseDouble(temp);
					temp = "";
					mode = 1;
				} else if (c == '/') {
					// Keep mode
					a = a / Double.parseDouble(temp);
					temp = "";
				} else temp += c;
			}
		}
		
		// Nothing...
		if (mode == 0) a = Double.parseDouble(temp);
		else if (mode == 1) a = a * Double.parseDouble(temp);
		else if (mode == 2) a = a / Double.parseDouble(temp);
		
		return a;
	}

}
