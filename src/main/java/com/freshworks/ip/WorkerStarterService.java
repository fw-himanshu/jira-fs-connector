package com.freshworks.ip;

import com.netflix.conductor.sdk.workflow.executor.WorkflowExecutor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkerStarterService {

    private WorkflowExecutor executor;

    @Value("${conductor.url}")
    private String conductorUrl;

    @PostConstruct
    public void init() {
        executor = new WorkflowExecutor(conductorUrl);
        executor.initWorkers("com.freshworks.ip");
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }
}

