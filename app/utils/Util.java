package utils;

import java.util.UUID;

public class Util {

	
	public static String getUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	
	public static String getString(String string){
		return (string!=null)?string.trim():"";
	}


	public static String getFileExtension(String filename){
		return filename.trim().substring(filename.lastIndexOf("."));
	}
	
	
	public static String concatIfNotEmpty(String string, String character, String replace) {
		if (getString(string).equalsIgnoreCase(""))
			return replace;
		else
			return getString(string).concat(character);
	}
	
	
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

}
