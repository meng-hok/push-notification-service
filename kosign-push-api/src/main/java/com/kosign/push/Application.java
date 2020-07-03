package com.kosign.push;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;

//import org.springframework.boot.WebApplicationType;

//@EnableJpaAuditing // JPA Auditing 활성화

@SpringBootApplication
public class Application implements CommandLineRunner {

	private DataSource dataSource;

	@Autowired
	public Application(DataSource dataSource){
		this.dataSource = dataSource;
	}

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "/app/config/springboot-webservice/real-application.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		String script = "kosign-push-api/src/main/resources/";
		String schema = script+"schema.sql";
		// Approch 1: using native way to creat e instance of Connection
		ScriptRunner scriptRunner =  new ScriptRunner(dataSource.getConnection());

		// Approch 2: using spring boot injected DataSource to get the connection
		//ScriptRunner scriptRunner = new ScriptRunner(datasource.getConnection());
		//scriptRunner.runScript(new BufferedReader(new FileReader(schema)));
	}
}
