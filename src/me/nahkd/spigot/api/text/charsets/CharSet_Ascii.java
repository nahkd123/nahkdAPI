package me.nahkd.spigot.api.text.charsets;

import me.nahkd.spigot.api.text.CharSet;

public enum CharSet_Ascii implements CharSet {
	SPACE(4),
	EXP(2),
	DOUBLE_QUOTE(5),
	HASH(6),
	DOLLAR_SIGN(6);

	public int width;
	private CharSet_Ascii(int width) {
		this.width = width;
	}
	
	@Override
	public int calculateWidth(String input) {
		return 0;
	}

}
