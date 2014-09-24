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
 * Setup simple transient EMR cluster with Scalding step
 */
public class Exercise06 {

    private static final Logger log = LoggerFactory.getLogger(Exercise06.class);

    public static void main(String[] args) {

        final ConfigurationProvider configurationProvider = new ConfigurationProvider();

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterName = Names.generateClusterName();

        final String bootstrapActionScriptLocation = configurationProvider.getResourcesFolder()
                + "/bootstrap-actions/install-scalding-deps.sh";

        final String jarLocation = configurationProvider.getJarsLocation() + "/scalding-count.jar";

        final String inputLocation = configurationProvider.getDataLocation();

        final String outputLocation = configurationProvider.getResultsLocation() + "/shc";

        //TODO
    }
}
