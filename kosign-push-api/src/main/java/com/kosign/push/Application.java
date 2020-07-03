package com.kosign.push;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

//import org.springframework.boot.WebApplicationType;

//@EnableJpaAuditing // JPA Auditing 활성화

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	

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

		
		
		// System.out.println( ResourceUtils.getFile( ResourceUtils.CLASSPATH_URL_PREFIX+"schema.sql").exists());
		ScriptRunner scriptRunner =  new ScriptRunner(dataSource.getConnection());
		//  pathList = file.list();
	
		for (String filename : pathList) {
			File file = ResourceUtils.getFile( ResourceUtils.CLASSPATH_URL_PREFIX+filename);
			scriptRunner.runScript(new BufferedReader(new FileReader(file)));
		}
		

		
		
		
	
	}
}
