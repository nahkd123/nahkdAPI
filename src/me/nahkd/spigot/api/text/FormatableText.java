package me.nahkd.spigot.api.text;

public class FormatableText {
	
	public String text;
	public int textWidth;
	public FormatableText(String text) {
		this.text = text;
		this.textWidth = getNewWidth();
	}
	
	public int getNewWidth() {
		int totalWidthPX = 0;
		boolean scan_bold = false;
		boolean scan_colorCode = false;
		for (char ch : this.text.toCharArray()) {
			if (ch == '§') {
				scan_colorCode = true;
			} else if (scan_colorCode) {
				scan_colorCode = false;
				if (ch == 'l' || ch == 'L') {
					scan_bold = true;
				} else scan_bold = false;
			} else {
				DefaultFontInfo fontInfo = DefaultFontInfo.getDefaultFontInfo(ch);
				totalWidthPX += (scan_bold? fontInfo.getBoldLength() : fontInfo.getLength()) + 1;
			}
		}
		return totalWidthPX;
	}
	
}
