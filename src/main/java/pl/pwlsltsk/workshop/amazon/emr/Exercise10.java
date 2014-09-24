package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.*;
import pl.pwlsltsk.workshop.util.Clients;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pwlsltsk
 *
 * Scale out cluster
 */
public class Exercise10 {

    public static void main(String[] args) {

        if (args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("Cluster ID is required");
        }

        final String clusterId = args[0];

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        //TODO
    }
}
