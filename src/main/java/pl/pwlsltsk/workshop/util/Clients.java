package pl.pwlsltsk.workshop.util;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * @author pwlsltsk
 */
public final class Clients {

    private Clients() {}

    private static final AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();

    public static AmazonElasticMapReduce getEmrClient() {

        return new AmazonElasticMapReduceClient(credentialsProvider);
    }

    public static AmazonS3 getS3Client() {

        return new AmazonS3Client(credentialsProvider);
    }
}
