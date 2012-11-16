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

}
