package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import pl.pwlsltsk.workshop.util.Clients;

/**
 * @author pwlsltsk
 *
 * Scale in cluster
 */
public class Exercise11 {

    public static void main(String[] args) {

        if (args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("Cluster ID is required");
        }

        final String clusterId = args[0];

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        //TODO
    }
}
