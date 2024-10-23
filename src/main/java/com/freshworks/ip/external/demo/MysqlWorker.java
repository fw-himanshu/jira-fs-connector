package com.freshworks.ip.external.demo;

import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;


public class MysqlWorker {

    @WorkerTask(value = "mysql")
    public TaskResult work(Task task) {

            Map<String, Object> inputData = task.getInputData();
            Map<String, Object> connectionParameter = (Map) inputData.get("connectionParameter");
            Map<String, Object> additionalData = (Map) inputData.get("additionalData");

            if (connectionParameter.get("action").equals("select")) {
				select(connectionParameter, additionalData);
            }else if (connectionParameter.get("action").equals("update")) {
				update(connectionParameter, additionalData);
		}

        task.setStatus(Task.Status.COMPLETED);
        System.out.println("invoked");
        return new TaskResult(task);
    }



        private void select(Map<String, Object> connectionParameter, Map<String, Object> additionalData) {
             System.out.println("select invoked");
        }
        private void update(Map<String, Object> connectionParameter, Map<String, Object> additionalData) {
             System.out.println("update invoked");
        }
}


