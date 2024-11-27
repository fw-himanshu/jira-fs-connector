package com.freshworks.ip.external.demo;

import com.freshworks.core.shared.ApplicationContextUtil;
import com.freshworks.core.shared.SyncServiceContainer;
import com.freshworks.core.shared.consumer.ConsumerService;
import com.freshworks.core.shared.sync.SyncService;
import com.freshworks.core.traverser.ParentStep;
import com.freshworks.ip.external.demo.hagrid.assets.JiraIssue;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.netflix.conductor", "com.freshworks" })
public class FreshWorksConnector {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(FreshWorksConnector.class, args);
	}

}
