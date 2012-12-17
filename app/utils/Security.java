package utils;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import play.Logger;

public class Security {
	
	private static final String CIPHER_ALGO = "AES";
	private static final String KEY = "5f8848e7ceb84a8b91c2f3e937d4135f";
	
	/**
	 * These parameters are put into a url and mailed to user
	 * which is why we are using obscure names for them
	 */
	public static final String PARAM_USERID = "key1";
	public static final String PARAM_TIMESTAMP = "key2";

	/**
	 * Encrypts and encodes string
	 */
	public static String generateHash(String salt, String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String data = salt.concat(string);
			for (int i=0 ; i<100 ; i++)
				md.update(data.getBytes(Util.getStringProperty("application.encoding")));
			return Base64.encodeBase64URLSafeString(md.digest());
		} catch (Exception exception) {
			Logger.error("Error while generating hash string", exception);
			return "";
		}
	}
	

	/**
	 * Encrypts a string
	 */
	public static String encrypt(String string) {
		try {
			Key key = new SecretKeySpec(KEY.getBytes(Util.getStringProperty("application.encoding")), CIPHER_ALGO);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64.encodeBase64URLSafeString(cipher.doFinal(string.getBytes(Util.getStringProperty("application.encoding"))));
		} catch (Exception exception) {
			Logger.error("Error while encrypting string", exception);
			return "";
		}
	}


	/**
	 * Decrypts a string
	 */
	public static String decrypt(String encryptedString) {
		try {
			Key key = new SecretKeySpec(KEY.getBytes(Util.getStringProperty("application.encoding")), CIPHER_ALGO);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] bytes = cipher.doFinal(Base64.decodeBase64(encryptedString));
			return new String(bytes);
		} catch (Exception exception) {
			Logger.error("Error while decrypting string", exception);
			return "";
		}
	}
}
