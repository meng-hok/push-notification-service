package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

public class TemplateUtils {

	static private TemplateUtils INSTANCE = new TemplateUtils();
	
	private Configuration cfg;

	private TemplateUtils() {
		// Create your Configuration instance, and specify if up to what FreeMarker
		// version (here 2.3.27) do you want to apply the fixes that are not 100%
		// backward-compatible. See the Configuration JavaDoc for details.
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);

		// Specify the source where the template files come from. Here I set a
		// plain directory for it, but non-file-system sources are possible too:
//		cfg.setDirectoryForTemplateLoading(new File("/where/you/store/templates"));

		// Set the preferred charset template files are stored in. UTF-8 is
		// a good choice in most applications:
		cfg.setDefaultEncoding("UTF-8");

		// Sets how errors will appear.
		// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
		cfg.setLogTemplateExceptions(false);

		// Wrap unchecked exceptions thrown during template processing into TemplateException-s.
		cfg.setWrapUncheckedExceptions(true);
	}

	static public TemplateUtils getInstance() {
		return INSTANCE;
	}
	
	/*
	public TemplateUtils() {
		Properties veProp = new Properties();
		
		try {
			veProp.load(new FileInputStream(new File(AppConfigManager.getInstance().getVelocityPathToProperties())));
		} catch (FileNotFoundException e) {
			AppLogManager.error(e);
		} catch (IOException e) {
			AppLogManager.error(e);
		}
		
		this.engine = new VelocityEngine(veProp);
//		this.engine.setProperty( Velocity.INPUT_ENCODING, "UTF-8" );
        this.engine.init();
	}
	*/
	
//	private VelocityEngine engine;
//
//	public VelocityEngine getEngine() {
//		return engine;
//	}
//
//	public void setEngine(VelocityEngine engine) {
//		this.engine = engine;
//	}
	
//	public String getText(String filePath) {
//		return getText(filePath, null);
//	}
	
//	public String getText(String filePath, Map<String, String> queryParams) {
//		Template t = engine.getTemplate(filePath, "utf-8");
//
//        VelocityContext context = new VelocityContext();
//        setParams(context, queryParams);
//
//        StringWriter writer = new StringWriter();
//        t.merge( context, writer );
//        
//        String query = writer.toString();
//
//        AppLogManager.info(query);
//        
//        return query;
//	}
	
	public String merge(File file, Map<String, String> queryParams) throws Exception {
		return merge(FileUtils.readFileToString(file, String.valueOf(Charset.forName("utf-8"))), queryParams);
	}
	
	public String merge(String templateStr, Map<String, String> params) throws Exception {
		
		/*
		params = params.entrySet().stream()
			.collect(Collectors.toMap(map -> map.getKey(), map -> map.setValue(StringUtils.defaultString(map.getValue()))));
		*/
		
		for (Map.Entry<String, String> entry : params.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			entry.setValue(StringUtils.defaultString(entry.getValue()));
		}
		
		Template t = new Template("default", new StringReader(templateStr),
				cfg);

		StringWriter writer = new StringWriter();
		t.process(params, writer);
		
		String str = writer.toString();
		AppLogManager.debug(str);
	
		return str;
		
		/*
		VelocityContext context = new VelocityContext();
        setParams(context, queryParams);
		
        StringWriter writer = new StringWriter();
		getEngine().evaluate(context, writer, "", str);
        
        String query = writer.toString();

        AppLogManager.info(query);
        
        return query;
        */
	}
	
//	public static void setParams(VelocityContext context, Map<String, String> queryParams) {
//		if(queryParams != null) {
//			for(Entry<String, String> entry : queryParams.entrySet()) {
//				context.put(entry.getKey(), entry.getValue());
//			}
//		}
//	}
	
//	@Deprecated
//	public String getSqlFormFile(String sqlFile, Map<String, String> queryParams) {
//		String filePath = getBasePath() + "/"+sqlFile+".sql";
//		String sql = getText(filePath, queryParams);
//		
//		sql = sql.replace(System.getProperty("line.separator"), " ");
//		sql = sql.replace("\t", " ");
//		return sql;
//	}
	
//	public String getJsonFormFile(String jsonFile) {
//		return getJsonFormFile(jsonFile, null);
//	}
	
//	public String getJsonFormFile(String jsonFile, Map<String, String> queryParams) {
//		String filePath = getBasePath() + "json" + "/"+jsonFile+".json";
//		return getText(filePath, queryParams);
//	}
}
