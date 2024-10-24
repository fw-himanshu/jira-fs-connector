package com.freshworks.ip.external.demo;

import com.freshworks.ip.dto.FreshserviceTicket;
import com.freshworks.ip.dto.JiraIssue;
import com.freshworks.ip.dto.JiraIssueResponse;
import com.freshworks.ip.service.FreshserviceService;
import com.freshworks.ip.service.JiraService;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.hc.core5.http.ParseException;


public class JIRAToFSMigrationWorker implements Worker {


    private final String name;

    public JIRAToFSMigrationWorker(String name) {
        this.name = name;
    }

    //    @WorkerTask(value = "jira_fs_connector2")
    public TaskResult work(Task task) {
        System.out.println("invoked");
        JiraService jiraService = new JiraService((String) task.getInputData().get("jiraAcount"),
                (String) task.getInputData().get("jiraEmail"),
                (String) task.getInputData().get("jiraAPIToken"));
        FreshserviceService freshserviceService = new FreshserviceService(
                (String) task.getInputData().get("fsAccount"),
                (String) task.getInputData().get("fsApiKey"));
        try {
            startMigration(jiraService, freshserviceService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        task.setStatus(Task.Status.COMPLETED);
        return new TaskResult(task);
    }


    public void startMigration(JiraService jiraService, FreshserviceService freshserviceService)
            throws IOException, URISyntaxException, ParseException {
        JiraIssueResponse response = jiraService.fetchIssues();
        //System.out.println(response);
        // Create tickets in Freshservice for each JIRA issue
        for (JiraIssue issue : response.getIssues()) {
            FreshserviceTicket ticket = new FreshserviceTicket();
            ticket.setDescription(issue.getFields().getDescription());
            ticket.setSubject("[" + issue.getKey() + "] " + issue.getFields().getSummary());
            ticket.setPriority(mapPriority(issue.getFields().getPriority().getName()));
            ticket.setEmail(issue.getFields().getCreator().getEmailAddress());
            freshserviceService.createTicket(ticket);
            System.out.println("Ticket created for JIRA issue: " + issue.getKey());
        }
    }

    // Map JIRA priority to Freshservice priority
    private static int mapPriority(String jiraPriority) {
        switch (jiraPriority) {
            case "Highest":
                return 1;
            case "High":
                return 2;
            case "Medium":
                return 3;
            case "Low":
                return 4;
            case "Lowest":
                return 5;
            default:
                return 3;  // Default to Medium
        }
    }

    // Driver code to test worker locally
    public static void main(String[] args) {
        JIRAToFSMigrationWorker jiraToFSMigrationWorker = new JIRAToFSMigrationWorker("jira_fs_connector2");
        Task task = new Task();
        task.getInputData().put("jiraAcount", "freshworks-ip");
        task.getInputData().put("jiraEmail", "himanshu@fwmstest.onmicrosoft.com");
        task.getInputData().put("jiraAPIToken",
                "<removed>");
        task.getInputData().put("fsAccount", "freshworks247");
        task.getInputData().put("fsApiKey", "<removed>");
        jiraToFSMigrationWorker.work(task);
    }

    @Override
    public String getTaskDefName() {
        return name;
    }

    @Override
    public TaskResult execute(Task task) {
        return work(task);
    }
}


