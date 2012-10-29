package utils;

import java.util.UUID;

public class Util {

	
	public static String getUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	
	public static String getString(String string){
		return (string!=null)?string.trim():"";
	}

}
