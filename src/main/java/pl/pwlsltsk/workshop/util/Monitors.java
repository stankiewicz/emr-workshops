package pl.pwlsltsk.workshop.util;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pwlsltsk
 */
public final class Monitors {

    private static final Logger log = LoggerFactory.getLogger(Monitors.class);
    private static final AmazonElasticMapReduce emrClient = Clients.getEmrClient();

    public static void monitorClusterState(String clusterId, ClusterState targetState) {

        final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        final List<ClusterState> terminationClusterStates = Arrays.asList(ClusterState.TERMINATED,
                ClusterState.TERMINATED_WITH_ERRORS, ClusterState.TERMINATING);

        scheduledExecutorService.schedule(new Runnable() {

            @Override
            public void run() {
                try {
                    final DescribeClusterResult describeClusterResult = emrClient.describeCluster(
                            new DescribeClusterRequest().withClusterId(clusterId));
                    final ClusterStatus clusterStatus = describeClusterResult.getCluster().getStatus();
                    final ClusterState clusterState = ClusterState.fromValue(clusterStatus.getState());

                    if (!terminationClusterStates.contains(clusterState) && !clusterState.equals(targetState)) {
                        scheduledExecutorService.schedule(this, 60, TimeUnit.SECONDS);
                    } else {
                        scheduledExecutorService.shutdown();
                    }

                    log.info("Cluster state: " + clusterStatus.getState());
                } catch (Exception e) {
                    log.error("Error while monitoring cluster state " + clusterId, e);
                    scheduledExecutorService.shutdown();
                }
            }
        }, 30, TimeUnit.SECONDS);
    }

    public static void monitorStepState(String clusterId, String stepId) {

        final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        final List<StepState> terminationStepStates = Arrays.asList(StepState.CANCELLED, StepState.INTERRUPTED,
                StepState.FAILED);

        scheduledExecutorService.schedule(new Runnable() {

            @Override
            public void run() {
                try {
                    final DescribeStepResult result = emrClient.describeStep(new DescribeStepRequest().
                            withClusterId(clusterId).withStepId(stepId));
                    final StepStatus stepStatus = result.getStep().getStatus();
                    final StepState stepState = StepState.fromValue(stepStatus.getState());

                    if (!terminationStepStates.contains(stepState) && !stepState.equals(StepState.COMPLETED)) {
                        scheduledExecutorService.schedule(this, 60, TimeUnit.SECONDS);
                    } else {
                        scheduledExecutorService.shutdown();
                    }

                    log.info(String.format("Step %s state: %s", stepId, stepStatus.getState()));
                } catch (Exception e) {
                    log.error(String.format("Error while monitoring step %s for cluster %s", stepId, clusterId), e);
                    scheduledExecutorService.shutdown();
                }
            }
        }, 30, TimeUnit.SECONDS);
    }
}
