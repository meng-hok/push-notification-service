package com.kosign.push;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;

//import org.springframework.boot.WebApplicationType;

//@EnableJpaAuditing // JPA Auditing 활성화
@CrossOrigin
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
		// String script = "kosign-push-api/build/resources/main/";
		// String script = "kosign-push-api/src/main/resources/";
		// String script = "classpath:";
		
		// String schema = "schema.sql";
		
		String[] pathList = {"schema.sql","data.sql"};

		System.out.println(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX));
		
		// System.out.println( ResourceUtils.getFile( ResourceUtils.CLASSPATH_URL_PREFIX+"schema.sql").exists());
		ScriptRunner scriptRunner =  new ScriptRunner(dataSource.getConnection());

		// Approch 2: using spring boot injected DataSource to get the connection
		//ScriptRunner scriptRunner = new ScriptRunner(datasource.getConnection());
		//scriptRunner.runScript(new BufferedReader(new FileReader(schema)));
	}
}
