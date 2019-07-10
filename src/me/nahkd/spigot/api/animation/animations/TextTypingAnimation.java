package me.nahkd.spigot.api.animation.animations;

import me.nahkd.spigot.api.animation.TextAnimation;

public class TextTypingAnimation extends TextAnimation {

	int typingTicks;
	int waitingTicks;
	int erasingTicks;
	int blinkingSpeed;
	int cursorBlinkingCountdown;
	boolean isCursorVisible;
	
	public TextTypingAnimation(int typingTicks, int waitingTicks, int erasingTicks, int blinkingSpeed) {
		super("typing", typingTicks + waitingTicks + erasingTicks);
		this.typingTicks = typingTicks;
		this.waitingTicks = waitingTicks;
		this.erasingTicks = erasingTicks;
		this.blinkingSpeed = blinkingSpeed;
		
		this.cursorBlinkingCountdown = this.blinkingSpeed;
		this.isCursorVisible = false;
	}

	@Override
	public String getString(String input) {
		String output = "";
		// Typing the text
		if (currentTicks >= 0 && currentTicks < this.typingTicks) {
			double progress = currentTicks / (double) this.typingTicks;
			for (int i = 0; i < Math.round(input.length() * progress); i++) {
				output += input.charAt(i);
			}
		} else if (currentTicks >= this.typingTicks && currentTicks < this.waitingTicks + this.typingTicks) {
			output = input;
		} else if (currentTicks >= this.waitingTicks && currentTicks < this.erasingTicks + this.typingTicks + this.waitingTicks) {
			double progress = 1.0D - ((currentTicks - this.typingTicks - this.waitingTicks) / (double) this.erasingTicks);
			for (int i = 0; i < Math.round(input.length() * progress); i++) {
				output += input.charAt(i);
			}
		}
		// Cursor visiblity
		if (this.cursorBlinkingCountdown <= 0) {		
			this.cursorBlinkingCountdown = this.blinkingSpeed;
			this.isCursorVisible = !this.isCursorVisible;
		}
		this.cursorBlinkingCountdown--;
		if (this.isCursorVisible) output += "_";
		return output;
	}

}
