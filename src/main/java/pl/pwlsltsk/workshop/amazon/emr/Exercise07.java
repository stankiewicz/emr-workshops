package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.Monitors;
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

        final JobFlowInstancesConfig instancesConfig = new JobFlowInstancesConfig().
                withInstanceGroups(new InstanceGroupConfig(InstanceRoleType.MASTER, InstanceType.M1Medium.toString(), 1)).
                withInstanceGroups(new InstanceGroupConfig(InstanceRoleType.CORE, InstanceType.M1Medium.toString(), 1)).
                withInstanceGroups(new InstanceGroupConfig(InstanceRoleType.TASK, InstanceType.M1Medium.toString(), 2));

        final StepFactory stepFactory = new StepFactory();

        final StepConfig setupHive = new StepConfig("Setup Hive", stepFactory.newInstallHiveStep(HIVE_VERSION));

        final StepConfig runCreateTableQuery = new StepConfig("Create external table",
                stepFactory.newRunHiveScriptStepVersioned(createQueryLocation, HIVE_VERSION, "-d",
                        "OUTPUT=" + configurationProvider.getDataLocation()));

        final StepConfig runSelectQuery = new StepConfig("Query external table",
                stepFactory.newRunHiveScriptStep(selectQueryLocation, HIVE_VERSION, "-d",
                        "OUTPUT=" + outputLocation));

        final RunJobFlowRequest request = new RunJobFlowRequest(clusterName, instancesConfig).
                withAmiVersion(configurationProvider.getAmiVersion()).
                withLogUri(configurationProvider.getLogsLocation()).
                withTags(new Tag("Name", clusterName)).
                withSteps(setupHive, runCreateTableQuery, runSelectQuery);

        final RunJobFlowResult result = client.runJobFlow(request);

        log.info(String.format("Started setup of %s (%s)", clusterName, result.getJobFlowId()));

        Monitors.monitorClusterState(result.getJobFlowId(), ClusterState.TERMINATED);
    }
}
