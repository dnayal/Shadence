package utils;

import java.util.UUID;

import play.Play;

public class Util {

	
	/**
	 * Returns a unique id
	 */
	public static String getUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	
	/**
	 * Formats string
	 */
	public static String getString(String string){
		return (string!=null)?string.trim():"";
	}


	/**
	 * Returns the extension of a file (including .)
	 */
	public static String getFileExtension(String filename){
		return filename.trim().substring(filename.lastIndexOf("."));
	}
	
	
	/**
	 * Concats the string with character if the string 
	 * is not empty, else returns a replacement string - 
	 * primarily used for address components
	 */
	public static String concatIfNotEmpty(String string, String character, String replace) {
		if (getString(string).equalsIgnoreCase(""))
			return replace;
		else
			return getString(string).concat(character);
	}
	
	
	/**
	 * Take input in hours (number) and converts it into (string) hours or days
	 */
	public static String getDurationDescription(Integer duration) {
		String result = null;
		
		if (duration == null) {
			result = "";
		} else {
			Float days = duration/ 24f;
			if (days >= 1) {
				if (days > 1)
					result = String.valueOf(days-days.intValue()==0?""+days.intValue():""+days).concat(" days");
				else
					result = String.valueOf(days-days.intValue()==0?""+days.intValue():""+days).concat(" day");
			} else {
				if (duration > 1)
					result = String.valueOf(duration).concat(" hours");
				else
					result = String.valueOf(duration).concat(" hour");
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * Returns a property from application.conf file
	 */
	public static String getProperty(String property) {
		return getString(Play.application().configuration().getString(property));
	}

}
