package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.Names;
import pl.pwlsltsk.workshop.util.configuration.ConfigurationProvider;

/**
 * @author pwlsltsk
 *
 * Setup cluster with Hive
 */
public class Exercise07 {

    private static final Logger log = LoggerFactory.getLogger(Exercise07.class);
    private static final String HIVE_VERSION = "0.13.1";

    public static void main(String[] args) {

        final ConfigurationProvider configurationProvider = new ConfigurationProvider();

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterName = Names.generateClusterName();

        final String createQueryLocation = configurationProvider.getResourcesFolder() + "/scripts/create.q";

        final String selectQueryLocation = configurationProvider.getResourcesFolder() + "/scripts/select.q";

        final String outputLocation = configurationProvider.getResultsLocation() + "/hive";

        //TODO
    }
}
