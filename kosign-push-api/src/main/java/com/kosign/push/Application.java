package com.kosign.push;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.sql.DataSource;

import com.kosign.push.utils.FileStorageUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;

//import org.springframework.boot.WebApplicationType;

//@EnableJpaAuditing // JPA Auditing 활성화
@CrossOrigin
@SpringBootApplication
public class Application extends SpringBootServletInitializer  implements CommandLineRunner {

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
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
	@Override
	public void run(String... args) throws Exception {
		// String script = "kosign-push-api/build/resources/main/";
		// String script = "kosign-push-api/src/main/resources/";
		// String script = "classpath:";
		
		// String schema = "schema.sql";
		
		String[] pathList = {"schema.sql","data.sql"};

		System.out.println(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX) );
		System.out.println(ResourceUtils.getURL(ResourceUtils.FILE_URL_PREFIX) );
		System.out.println("P 8 File");
		System.out.println(FileStorageUtil.PUTP8FILEPATH);
		// System.out.println( ResourceUtils.getFile( ResourceUtils.CLASSPATH_URL_PREFIX+"schema.sql").exists());
		ScriptRunner scriptRunner =  new ScriptRunner(dataSource.getConnection());
		//  pathList = file.list();
	
		// for (String filename : pathList) {
		// 	File file = ResourceUtils.getFile( ResourceUtils.CLASSPATH_URL_PREFIX+filename);
		// 	scriptRunner.runScript(new BufferedReader(new FileReader(file)));
		// }
			
		
	
	}

	
}
