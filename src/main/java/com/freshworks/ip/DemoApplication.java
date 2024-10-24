package com.freshworks.ip;

import com.freshworks.ip.external.demo.JIRAToFSMigrationWorker;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI("http://conductor.inboxgrader.com/api/"); // Point this to the server API

        int threadCount =                2; // number of threads used to execute workers.  To avoid starvation, should be
        // same or more than number of workers

        Worker worker1 = new JIRAToFSMigrationWorker("jira_fs_connector2");
//        Worker worker2 = new SampleWorker("task_5");

        // Create TaskRunnerConfigurer
        TaskRunnerConfigurer configurer =
                new TaskRunnerConfigurer.Builder(taskClient, Arrays.asList(worker1))
                        .withThreadCount(threadCount)
                        .build();

        // Start the polling and execution of tasks
        configurer.init();
	}

}
