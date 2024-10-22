package com.freshworks.ip;

import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;


public class JIRAToFSMigrationWorker {

    @WorkerTask(value = "jira_fs_migration")
    public TaskResult work(Task task) {
        task.setStatus(Task.Status.COMPLETED);
        System.out.println("invoked");
        return new TaskResult(task);
    }
}


