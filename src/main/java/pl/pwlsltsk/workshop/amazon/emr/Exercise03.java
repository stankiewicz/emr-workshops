package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.configuration.ConfigurationProvider;

/**
 * @author pwlsltsk
 *
 * Add MapReduce step
 */
public class Exercise03 {

    private static final Logger log = LoggerFactory.getLogger(Exercise03.class);

    public static void main(String[] args) {

        if (args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("Cluster ID is required");
        }

        final ConfigurationProvider configurationProvider = new ConfigurationProvider();

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterId = args[0];

        final String jarLocation =  configurationProvider.getJarsLocation() + "/mapreduce-count.jar";

        final String inputLocation = configurationProvider.getDataLocation();

        final String outputLocation = configurationProvider.getResultsLocation() + "/mrhc";

        //TODO
    }
}
