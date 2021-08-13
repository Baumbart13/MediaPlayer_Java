package com.baumbart.mediaPlayer;

import com.baumbart.annotations.Author;
import com.baumbart.mediaPlayer.windows.MediaWindow;

import java.util.Locale;

@Author
public class MediaPlayer {
	private static boolean isDebugModeActive = false;
	private static MediaWindow mediaWindow;

	public static enum ProgramArguments{
		debug;

		public static String PREFIX = "--";
		@Override
		public String toString(){
			return String.format("%s%s", PREFIX, name());
		}

		public boolean equalsIgnoreCase(String s){
			return toString().toLowerCase().equals(s.toLowerCase());
		}
	}

	private static void argumentHandling(String[] args){
		for(var i = 0; i < args.length; ++i){
			var s = args[i];

			if(!s.startsWith(ProgramArguments.PREFIX)){
				continue;
			}

			if(ProgramArguments.debug.equalsIgnoreCase(s)){
				activateDebugMode();
			}
		}
	}

	public static MediaWindow getPlayerWindow(){
		return mediaWindow;
	}

	public static void main(String[] args) {

		argumentHandling(args);

		mediaWindow = new MediaWindow(isDebugModeActive);
	}

	public static boolean isDebugMode(){ return isDebugModeActive;}

	private static void activateDebugMode(){
		isDebugModeActive = true;
	}
}
