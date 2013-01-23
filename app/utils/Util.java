package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import play.Logger;
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
	public static String getStringProperty(String property) {
		return getString(Play.application().configuration().getString(property));
	}


	/**
	 * Returns a property from application.conf file
	 */
	public static Integer getIntegerProperty(String property) {
		return Play.application().configuration().getInt(property);
	}
	
	
	/**
	 * Copies file to the given destination
	 */
	public static boolean copyFile(File sourceFile, File destinationFile) {
		
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		
		try {
			sourceChannel = new FileInputStream(sourceFile).getChannel();
			destinationFile.getParentFile().mkdirs();
			destinationChannel = new FileOutputStream(destinationFile).getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
			destinationChannel.close();
			sourceChannel.close();
			return true;
		} catch (Exception exception) {
			try {
				destinationChannel.close();
				sourceChannel.close();
				Logger.error("Error while copying file " + sourceFile.getName() + " to " + destinationFile.getName(), exception);
			} catch (Exception e) {}
			return false;
		}
	}
	
	
	public static void sendMail(final String email, final String subject, final String body) {
		Runnable mailThread = new Runnable() {
			@Override
			public void run() {
				try {
					final String EMAIL_USER = "info@shadence.com";
					final String EMAIL_PASSWORD = "myn$3007";
					final String EMAIL_HOST = "smtpout.secureserver.net";
			
					Properties props = new Properties();
					props.setProperty("mail.smtp.starttls.enable","true");
					props.setProperty("mail.smtp.auth","true");
					props.setProperty("mail.transport.protocol", "smtp");
					props.setProperty("mail.host", EMAIL_HOST);
			
					Session mailSession = Session.getInstance(props, new Authenticator() {
					    @Override
					    protected PasswordAuthentication getPasswordAuthentication() {
					        return new PasswordAuthentication(EMAIL_USER, EMAIL_PASSWORD);
					    }
					});
					Transport transport = mailSession.getTransport();
					MimeMessage message = new MimeMessage(mailSession);
					message.setSubject(subject);
					message.setFrom(new InternetAddress(EMAIL_USER, Util.getStringProperty("application.name")));
					message.setContent(body, "text/html");
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					transport.connect();
					transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
					transport.close();
				} catch (Exception exception) {
					Logger.error("Error while sending the email", exception);
				}
			}
		};
		
		new Thread(mailThread).start();
		
	}
	
	
	public static <E> List<E> convertSetToList(Set<E> set) {
		List<E> list = new ArrayList<E>(set);
		return list;
	}

}
