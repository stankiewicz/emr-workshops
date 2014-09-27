package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.Monitors;
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

        final String jarMainClass = "pl.pwlsltsk.mapreduce.HashtagCount";

        final String inputLocation = configurationProvider.getDataLocation();

        final String outputLocation = configurationProvider.getResultsLocation() + "/mrhc";

        final HadoopJarStepConfig mapReduceStepConfig = new HadoopJarStepConfig(jarLocation)
                .withMainClass(jarMainClass)
                .withArgs(inputLocation, outputLocation);

        final StepConfig mapReduceStep = new StepConfig("MapReduce hashtag count", mapReduceStepConfig)
                .withActionOnFailure(ActionOnFailure.CONTINUE);

        final AddJobFlowStepsResult result = client.addJobFlowSteps(
                new AddJobFlowStepsRequest(clusterId).withSteps(mapReduceStep));

        log.info("Steps: " + result.toString());

        Monitors.monitorStepState(clusterId, result.getStepIds().get(0));
    }
}
