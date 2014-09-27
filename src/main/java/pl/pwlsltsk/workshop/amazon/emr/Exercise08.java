package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.Monitors;
import pl.pwlsltsk.workshop.util.Names;
import pl.pwlsltsk.workshop.util.configuration.ConfigurationProvider;

import java.util.Arrays;

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

        final JobFlowInstancesConfig instancesConfig = new JobFlowInstancesConfig().
                withInstanceGroups(new InstanceGroupConfig(InstanceRoleType.MASTER, InstanceType.M1Medium.toString(), 1)).
                withInstanceGroups(new InstanceGroupConfig(InstanceRoleType.CORE, InstanceType.M1Medium.toString(), 1)).
                withInstanceGroups(new InstanceGroupConfig(InstanceRoleType.TASK, InstanceType.M1Medium.toString(), 1)).
                withKeepJobFlowAliveWhenNoSteps(true);

        final BootstrapActionConfig installImpala = new BootstrapActionConfig("Install Impala",
                new ScriptBootstrapActionConfig("s3://elasticmapreduce/libs/impala/setup-impala",
                        Arrays.asList("--base-path", "s3://elasticmapreduce", "--impala-version", "1.2.4")));

        final StepConfig copyData = new StepConfig("Copy data from S3",
                new HadoopJarStepConfig("/home/hadoop/lib/emr-s3distcp-1.0.jar")
                        .withArgs("--src", configurationProvider.getDataLocation(), "--dest", "hdfs:///data"))
                .withActionOnFailure(ActionOnFailure.CONTINUE);

        final RunJobFlowRequest request = new RunJobFlowRequest(clusterName, instancesConfig).
                withAmiVersion(configurationProvider.getAmiVersion()).
                withLogUri(configurationProvider.getLogsLocation()).
                withBootstrapActions(installImpala).
                withSteps(copyData).
                withTags(new Tag("Name", clusterName));

        final RunJobFlowResult result = client.runJobFlow(request);

        log.info(String.format("Started setup of %s (%s)", clusterName, result.getJobFlowId()));

        Monitors.monitorClusterState(result.getJobFlowId(), ClusterState.WAITING);
    }
}
