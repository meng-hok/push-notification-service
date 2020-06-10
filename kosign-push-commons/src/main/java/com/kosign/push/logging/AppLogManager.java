package com.kosign.push.logging;

import com.kosign.push.commons.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class AppLogManager {

    /*private static Class getCallerClass(int level) {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String rawFQN = stElements[level+1].toString().split("\\(")[0];
        try {
            return Class.forName(rawFQN.substring(0, rawFQN.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }*/

    private static String getCallerAsString(int level) {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String rawFQN = stElements[level+1].toString().split("\\(")[0];

//        System.out.println(rawFQN);

        return rawFQN.substring(0, StringUtils.lastIndexOf(rawFQN, "."));
    }

    private static String[] parseCaller(String str, String msg) {
        return new String[]{
                str
            ,   MessageFormat.format("{0}:{1} - {2}", StringUtils.substringAfterLast(str, ".") + ".java", Thread.currentThread().getStackTrace()[4].getLineNumber(), msg)
        };
    }

    private static String[] parseCaller(String msg) {
        return parseCaller(getCallerAsString(3), msg);
    }

    public static void debug(String msg) {
        String[] parsed = parseCaller(msg);
        debug(parsed[0], parsed[1]);
    }

    public static void error(String msg) {
        String[] parsed = parseCaller(msg);
        error(parsed[0], parsed[1]);
    }

    public static void info(String msg) {
        String[] parsed = parseCaller(msg);
        info(parsed[0], parsed[1]);
    }

    public static void error(Throwable ex) {
        String[] parsed = parseCaller(ex.getMessage());
        error(parsed[0], ex.getMessage(), ex);
    }

//    public static void info(String msg) {
//        info(getLogger(getCallerName(2)), msg);
//    }

//    public static void debug(Class<?> caller, String msg) {
//        debug(caller.getName(), msg);
//    }

    public static void debug(String category, String msg) {
        Logger logger = getLogger(category);
        debug(logger, msg);
    }

    private static void debug(Logger logger, String msg) {
        logger.debug("####################################################################################################");
        logger.debug(msg);
        logger.debug("####################################################################################################");
    }

//    public static void error(Class<?> caller, Throwable ex) {
//        String[] parsed = parseCaller();
//        error(getLogger(caller), MessageFormat.format("{0}:{1} - {2}", parsed[1], Thread.currentThread().getStackTrace()[2].getLineNumber(), ex.getMessage()), ex);
//    }

    private static void error(Logger logger, String msg) {
        error(logger, msg, null);
    }

    private static void error(Logger logger, String msg, Throwable ex) {
        logger.error("####################################################################################################");
        logger.error(msg);
        logger.error("####################################################################################################");

        if(ex != null) {
            ex.printStackTrace();
        }
    }

    public static void info(Class<?> caller, String msg) {
        String[] parsed = parseCaller(msg);
        info(caller.getName(), parsed[1]);
    }

    public static void info(String category, String msg) {
        Logger logger = LoggerFactory.getLogger(category);
        info(logger, msg);
    }

    public static void error(String category, String msg) {
        error(category, msg, null);
    }

    public static void error(String category, String msg, Throwable ex) {
        Logger logger = LoggerFactory.getLogger(category);
        error(logger, msg, ex);
    }

    private static void info(Logger logger, String msg) {
        logger.info(msg);
    }

//    public static Logger getLogger(Class<?> caller) {
//        if(caller == null) return getRootLogger();
//        return LoggerFactory.getLogger(caller);
//    }

    public static Logger getLogger(String category) {
        if(StringUtils.isBlank(category)) return getRootLogger();
        return LoggerFactory.getLogger(category);
    }

    public static Logger getRootLogger() {
        return LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

    /*
    private static HashMap<String, Category> categoryHash = new HashMap<>();
    private static String defaultLog = "CONSOLE";

    static {

        String logConfigPath = System.getProperty("logConfigPath");

        if (StringUtils.isBlank(logConfigPath)) {
            Appender consoleAppender = new ConsoleAppender(new PatternLayout(
                    "%m%n"));
            Logger logger = Logger.getRootLogger();
            logger.setLevel(Level.DEBUG);
            logger.addAppender(consoleAppender);
            categoryHash.put("CONSOLE", logger);
            console("로그 설정을 로드하는데 실패하였습니다.");
            console("로그출력을 System.out으로 설정 합니다.");
        } else {
            init(logConfigPath);
        }
    }

    public static void init(String logConfigPath) {
        DOMConfigurator.configure(logConfigPath);

        Enumeration<?> catList = LogManager.getCurrentLoggers();
        while (catList.hasMoreElements()) {
            Category category = (Category) catList.nextElement();
            String categoryName = category.getName();
            categoryHash.put(categoryName, category);
        }
    }

    public static Logger getLogger(String name) {
        Logger logger = null;

        if (StringUtils.isBlank(name)) {
            logger = Logger.getLogger(defaultLog);
        } else {
            logger = (Logger) categoryHash.get(name);
        }

        if (logger == null) {
            logger = Logger.getRootLogger();

//			System.out.println("log config file에 등록되지 않은 로그 정보 사용중 >>>" + name);
            try {
                logger.error(
                        "log config file에서 매치 되는 로그 모듈을 찾지 못함:[" + name + "]",
                        new Exception("AppLogManager 사용 오류:" + name));
            } catch (Exception e) {
                AppLogManager.error(e);
            }

        }

        return logger;
    }

    public static void console(Object msg) {
        log("CONSOLE", msg, Level.OFF);
    }

    public static void debug(Object msg) {
        debug(defaultLog, msg);
    }

    public static void debug(String categoryName, Object msg) {
        log(categoryName, msg, Level.DEBUG);
    }

    public static void info(Object msg) {
        info(defaultLog, msg);
    }

    public static void info(String categoryName, Object msg) {
        log(categoryName, msg, Level.INFO);
    }

    public static void error(Object msg) {
        error(defaultLog, msg, null, null);
    }

    public static void error(Object msg, Throwable ex) {
        error(defaultLog, msg, ex, null);
    }

    public static void error(Throwable ex) {
        error(defaultLog, ex.getMessage(), ex, null);
    }

    public static void error(Throwable ex, String tid) {
        error(defaultLog, ex.getMessage(), ex, tid);
    }

    public static void error(String categoryName, Object msg) {
        log(categoryName, msg, Level.ERROR, null, null);
    }

    public static void error(String categoryName, Object msg, Throwable ex, String tid) {
        log(categoryName, msg, Level.ERROR, ex, tid);
    }

    private static void log(String categoryName, Object msg, Level lvl) {
        log(categoryName, msg, lvl, null, null);
    }

    private static void log(String categoryName, Object msg, Level lvl, Throwable ex, String tid) {
        Logger logger = getLogger(categoryName);

        if (logger != null) {
            if ("CONSOLE".equals(categoryName))
                logger.log("AppLogManager", lvl, "##########################################################################################", null);

            logger.log("AppLogManager", lvl, msg, ex);

            if ("CONSOLE".equals(categoryName))
                logger.log("AppLogManager", lvl, "##########################################################################################", null);

        } else {
            System.out.println("##########################################################################################");
            System.out.println("[" + categoryName + "] " + msg + "");
            System.out.println("##########################################################################################");
        }

    }
    */

    public static void main(String[] args) {
        info("test");
    }
}