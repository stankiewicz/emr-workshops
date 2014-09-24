package pl.pwlsltsk.workshop.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author pwlsltsk
 */
public class AwsConnectionValidator {

    private static final Logger log = LoggerFactory.getLogger(AwsConnectionValidator.class);

    public static void main(String[] args) throws URISyntaxException {

        final AmazonS3 client = Clients.getS3Client();
        final List<Bucket> buckets = client.listBuckets();

        buckets.forEach(bucket -> log.info(bucket.getName()));
    }
}
