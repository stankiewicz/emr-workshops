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
 * Create long-running Impala cluster
 */
public class Exercise08 {

    private static final Logger log = LoggerFactory.getLogger(Exercise08.class);

    public static void main(String[] args) {

        final ConfigurationProvider configurationProvider = new ConfigurationProvider();

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterName = Names.generateClusterName();

        //TODO
    }
}
