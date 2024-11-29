package com.freshworks.ip.external.demo.worker;

import com.freshworks.core.shared.ApplicationContextUtil;
import com.freshworks.core.shared.SyncServiceContainer;
import com.freshworks.core.shared.consumer.ConsumerService;
import com.freshworks.core.shared.infra.InfraService;
import com.freshworks.core.shared.sync.SyncService;
import com.freshworks.core.shared.sync.SyncStatusService;
import com.freshworks.core.traverser.ParentStep;
import com.freshworks.freshindex.index.query.JsonQueryService;
import com.freshworks.ip.external.demo.hagrid.assets.JiraIssue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class JiraWorker {

  private final TaskClient httpTaskClient;

  @Autowired
  public JiraWorker(TaskClient httpTaskClient) {
    this.httpTaskClient = httpTaskClient;
  }


  @WorkerTask(value = "jira_fs_migration", threadCount = 10)
  public TaskResult work(Task task) throws Exception {

    Map<String, Object> inputData = task.getInputData();
    SyncService syncService = ApplicationContextUtil.getBean(SyncService.class);
    ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
        .put("jiraAcount", (String) inputData.get("jiraAcount"))
        .put("jiraEmail", (String) inputData.get("jiraEmail"))
        .put("jiraAPIToken", (String) inputData.get("jiraAPIToken"))
        .build();
    SyncServiceContainer syncServiceContainer = syncService.startSync(ParentStep.class, UUID.randomUUID().toString(), 1,params);
    SyncStatusService syncStatusService =  syncServiceContainer.getSyncStatusService();
    ConsumerService consumerService = syncServiceContainer.getConsumerService();
    while (syncStatusService.getSyncStatus() == 0) {
      System.out.println("Syncing.......");
    }
    List<JiraIssue> jiraIssues = consumerService.getAssetByAssetType(JiraIssue.class);

    for (JiraIssue jiraIssue : jiraIssues) {
      System.out.println(jiraIssue);
    }
    //PersistenceManager.putData(Scope.TASK, "test", "test");

    Map<String, Object> outputData = task.getOutputData();
    outputData.put("jiraIssues", jiraIssues);
    task.setStatus(Task.Status.COMPLETED);
    System.out.println("invoked");
    return new TaskResult(task);
  }

}


