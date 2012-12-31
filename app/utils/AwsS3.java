package utils;

import java.io.File;

import play.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * Access the uploaded file at - 
 * http://shadence.uploaded.photos.s3.amazonaws.com/experience/photo.jpeg
 * http://shadence.uploaded.photos.s3-external-3.amazonaws.com/experience/photo.jpeg
 * http(s)://s3-eu-west-1.amazonaws.com/shadence.uploaded.photos/experience/photo.jpeg
 */
public class AwsS3 {
	
	String accessKey;
	String secretKey;
	String s3Bucket;
	
	AmazonS3 amazonS3;
	
	public AwsS3() {
		s3Bucket = Util.getStringProperty("aws.s3.bucket.uploaded.photos");
		accessKey = Util.getStringProperty("aws.access.key");
		secretKey = Util.getStringProperty("aws.secret.key");
		if(s3Bucket.equalsIgnoreCase("") || accessKey.equalsIgnoreCase("") || secretKey.equalsIgnoreCase(""))
			Logger.error("S3Bucket:" + s3Bucket + " Access Key:" + accessKey + " Secret Key:" + secretKey, new RuntimeException("Property required to connect with Amazon S3 not provided"));
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		amazonS3 = new AmazonS3Client(credentials);
		amazonS3.setEndpoint(Util.getStringProperty("aws.s3.endpoint"));
	}
	

	/**
	 * Uploads photos to Amazon S3. Takes the complete path of the local 
	 * file to be uploaded and the key of the file (location + filename) 
	 * as the target destination in Amazon S3 
	 */
	public void uploadFile(File sourceFile, String fileKey) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(s3Bucket, fileKey, sourceFile);
		putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
		amazonS3.putObject(putObjectRequest);
	}
	
	
	/**
	 * Deletes the file from Amazon S3 
	 */
	public void deleteFile(String fileKey) {
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(s3Bucket, fileKey);
		amazonS3.deleteObject(deleteObjectRequest);
	}

}
