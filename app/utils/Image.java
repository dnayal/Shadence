package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import org.imgscalr.*;

import javax.imageio.ImageIO;

public class Image {

	public static boolean resizeAndSaveImage(File sourceImage, File targetImage, int targetImageSize, String targetImageType) {

		boolean successful = false;

		try {
			BufferedImage originalImage = ImageIO.read(sourceImage);
			int height = originalImage.getHeight();
			int width = originalImage.getWidth();

			// Resize the image only if it is more than given image size
			if (height > targetImageSize || width > targetImageSize) {
				BufferedImage newImage = AsyncScalr.resize(originalImage,
						Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC,
						targetImageSize, Scalr.OP_ANTIALIAS).get();

				ImageIO.write(newImage, targetImageType, targetImage);
				newImage.flush();
				newImage = null;
			}
			// If the image size is less, then maintain the scale of the
			// destination image
			else {
				ImageIO.write(originalImage, targetImageType, targetImage);
			}

			successful = true;

			originalImage.flush();
			originalImage = null;

//			System.gc();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return successful;

	}
}
