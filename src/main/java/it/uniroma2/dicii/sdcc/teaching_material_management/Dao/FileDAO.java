package it.uniroma2.dicii.sdcc.teaching_material_management.Dao;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import it.uniroma2.dicii.sdcc.teaching_material_management.error.FileAlreadyExistsException;
import it.uniroma2.dicii.sdcc.teaching_material_management.error.NotSuchFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class FileDAO {

    // The name of S3 Bucket is stored in the properties file
    @Value("${bucket_name}")
    private String bucketName;


    /* findByPrefix return the names of the documents in Amazon S3 data-store starting with given prefix*/
    public ArrayList<String> findByPrefix(String prefix){


//        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build();

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.defaultClient();

        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

        while (objectListing.isTruncated()) {
            objectListing = amazonS3.listNextBatchOfObjects(objectListing);
            objectSummaries.addAll (objectListing.getObjectSummaries());
        }

        ArrayList<String> names = new ArrayList<>();
        for (S3ObjectSummary objectSummary : objectSummaries){
            names.add(objectSummary.getKey());
        }
        return names;
    }

    /* generateTemporaryUploadLink generate a link usable to upload on Amazon S3 data-store a file with given filename.
       The link is generated using microservice's AWS Credentials and has a temporary valence. */
    public String generateTemporaryUploadLink(String fileName) throws FileAlreadyExistsException {

        //        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build();

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.defaultClient();


        // Set the presigned URL to expire after ten minuts.
        Date expiration = generateExpiredTime(10);

        // Checking if a file whit given name already exists in repository
        if (amazonS3.doesObjectExist(bucketName, fileName)){
            throw new FileAlreadyExistsException();
        }

        // Generate the pre-signed URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                bucketName, fileName).withMethod(HttpMethod.PUT).withExpiration(expiration);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    /* generateTemporaryDownloadLink generate a link usable to download from Amazon S3 data-store a file with given
       filename. The link is generated using microservice's AWS Credentials and has a temporary valence. */
    public String generateTemporaryDownloadLink(String fileName) throws NotSuchFileException {

//        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build();

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.defaultClient();

        // Set the presigned URL to expire after ten minuts.
        Date expiration = generateExpiredTime(10);

        // Checking if a file whit given name exists in repository
        if (!amazonS3.doesObjectExist(bucketName, fileName)){
            throw new NotSuchFileException();
        }

        // Generate the pre-signed URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                bucketName, fileName).withMethod(HttpMethod.GET).withExpiration(expiration);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();

    }

    private Date generateExpiredTime(int minutes){
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime() + 1000 * 60 * minutes;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
