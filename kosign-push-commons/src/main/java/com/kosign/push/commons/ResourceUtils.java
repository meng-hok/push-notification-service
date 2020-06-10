package com.kosign.push.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ResourceUtils {

	final static Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);

	public static String getPathToResource(Object obj, String subPath) {
		return getPathToResource(obj.getClass(), subPath);
	}

	public static String getPathToResource(Class<?> clazz, String subPath) {
		String pathToResource = clazz.getResource("").getPath();
//		AppLogManager.debug(pathToResource);

		String finalPath = null;

		if(StringUtils.contains(pathToResource, "!")) {
			finalPath = StringUtils.substringAfterLast(clazz.getResource("").getPath(), "!") + subPath;
		}
		else {
//			finalPath = "/" + StringUtils.remove(
//					clazz.getResource("").getPath(),
//					clazz.getResource("/").getPath()) + subPath;
			finalPath = clazz.getResource("").getPath() + subPath;
		}

		return finalPath;
	}

	@Deprecated
	public static URI getResourceAsURI(Class<?> clazz, String str) {
		try {
			return clazz.getResource(str).toURI();
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static File getResourceAsFile(Class<?> clazz, String str) {
//		ClassLoader classLoader = clazz.getClassLoader();
//		AppLogManager.debug(LOG, clazz.getResource("").getPath());
//		AppLogManager.debug(LOG, clazz.getResource("/").getPath());
		return new File(getPathToResource(clazz, str));
	}

}
