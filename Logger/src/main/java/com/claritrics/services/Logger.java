package com.claritrics.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.claritrics.service.exceptions.ServiceException;
import com.claritrics.store.DBFactory;
import com.claritrics.store.DBFactorySession;

/**
 * Singleton Logger to log info, debug, error, activity & profile messages in
 * database
 * 
 * @author Kishore
 * 
 */
public class Logger {

	private static DBFactorySession dbSession = null;
	private static Connection conn = null;
	private static String logPath = "";
	private static String infoLogPath = "";
	private static String errorLogPath = "";

	// create an object of SingleObject
	private static Logger instance = new Logger();

	// make the constructor private so that this class cannot be //instantiated
	private Logger() {
		try {
			dbSession = DBFactory.getLogSession();
//			conn = (dbSession != null ? dbSession.connect() : null);
			// System.out.println(new Date() +
			// "::DB session Logger object created");

			// load properties for file logger
			Properties properties = loadProperies("logger");
			if (properties != null) {
				infoLogPath = properties.getProperty("InfoLogPath", "");
				errorLogPath = properties.getProperty("ErrorLogPath", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

	public Logger(String logPath) {
		Logger.logPath = logPath;
	}

	public enum MessageType {
		INFO, ERROR, DEBUG
	}

	// Destroy the object properties
	public void destroy() {
		try {
			if (dbSession != null) {
				dbSession.disconnect(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Get the only object available
	public static Logger getInstance() {
		// System.out.println(new Date() + "::Logger object created");
		return instance;
	}

	/**
	 * Method to log info messages
	 * 
	 * @param message
	 * @return LogId
	 */
	public int info(final String message) {
		int eventId = 0;
		try {
			if (dbSession == null) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			} else if (conn == null || conn.isClosed()) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			}
			// Declaring as final as I do not want to change the query formation
			// structure. Once assigned that is all.
			// query = query + blah.. not allowed

			final String query = "INSERT INTO LogEvent(Message,LogType,Exception,EventDateTime) values("
					+ "?,'INFO','',?)";

			eventId = dbSession.executeUpdate(
					dbSession.prepareStatement(query, conn, message, dbSession.getFormattedDate()), conn);
		} catch (ServiceException | SQLException e) {
			System.out.println("Info - " + e.getMessage());
		}
		return eventId;
	}

	/**
	 * Method to log query type messages
	 * 
	 * @param message
	 * @return LogId
	 */
	public int queryInfo(final String message) {
		int eventId = 0;
		try {
			if (dbSession == null) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			} else if (conn == null || conn.isClosed()) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			}

			final String query = "INSERT INTO LogEvent(Message,LogType,Exception,EventDateTime) values("
					+ "?,'Query','',?)";

			eventId = dbSession.executeUpdate(
					dbSession.prepareStatement(query, conn, message, dbSession.getFormattedDate()), conn);
		} catch (ServiceException | SQLException e) {
			System.out.println("Query - " + e.getMessage());
		}
		return eventId;
	}

	/**
	 * Method to log debug messages
	 * 
	 * @param message
	 * @return LogId
	 */
	public int debug(final String message) {
		int eventId = 0;
		try {
			if (dbSession == null) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			} else if (conn == null || conn.isClosed()) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			}

			final String query = "INSERT INTO LogEvent(Message,LogType,Exception,EventDateTime) values("
					+ "?,'DEBUG','',?)";

			eventId = dbSession.executeUpdate(
					dbSession.prepareStatement(query, conn, message, dbSession.getFormattedDate()), conn);
		} catch (ServiceException | SQLException e) {
			System.out.println("Debug - " + e.getMessage());
		}
		return eventId;
	}

	/**
	 * Method to log error messages
	 * 
	 * @param message
	 * @param exception
	 * @return LogId
	 */
	public int error(final String message, final Exception exception) {
		int eventId = 0;
		try {
			if (dbSession == null) {
				dbSession = DBFactory.getLogSession();
//				conn = dbSession.connect();
			} else if (conn == null || conn.isClosed()) {
				dbSession = DBFactory.getLogSession();
//				conn = dbSession.connect();
			}
			StringBuffer stackTrace = new StringBuffer();
			String exceptionMsg = "";
			if (exception != null) {
				exceptionMsg = getExceptionCaughtMessage(exception);
				if (exceptionMsg != null) {
					stackTrace.append("Exception Message: " + exceptionMsg);
				}
				exceptionMsg = getExceptionMessageChain(exception);
				stackTrace.append("Masked Message: " + exceptionMsg);
				stackTrace.append("\nStacktrace: \n");
				if (exception.getStackTrace() != null) {
					final StackTraceElement[] trace = exception.getStackTrace();
					for (int i = 0; i < trace.length; i++) {
						stackTrace.append(trace[i].toString() + "\n");
					}
				}
			}
			System.out.println(stackTrace);
			final String query = "INSERT INTO LogEvent(Message,LogType,Exception,EventDateTime) values(" + "?,'ERROR',?"
					+ ",?)";
			eventId = dbSession.executeUpdate(dbSession.prepareStatement(query, conn,
					((message == null || message.isEmpty()) ? exceptionMsg : message), stackTrace.toString(),
					dbSession.getFormattedDate()), conn);
		} catch (ServiceException | SQLException e) {
			System.out.println("Error - " + e.getMessage());
		}
		return eventId;
	}

	/**
	 * Method to log activities
	 * 
	 * @param message
	 * @return LogId
	 */
	public int activity(final String referenceId, final String referenceType, final String activity,
			final String activityType) {
		int activityId = 0;
		try {
			if (dbSession == null) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			} else if (conn == null || conn.isClosed()) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			}
			final String query = "INSERT INTO logactivity(ReferenceId,ReferenceType,Activity,"
					+ "ActivityType,ActivityDateTime) values(?,?,?,?,?)";
			System.out.println("activityquery=" + query);
			activityId = dbSession.executeUpdate(dbSession.prepareStatement(query, conn, referenceId, referenceType,
					activity, activityType, dbSession.getFormattedDate()), conn);

		} catch (ServiceException | SQLException e) {
			System.out.println("Activity - " + e.getMessage());
		}
		return activityId;
	}

	/**
	 * Method to log profiling activities
	 * 
	 * @param message
	 * @param milliSeconds
	 * @param application
	 * @return LogId
	 */
	public int profile(final String message, final long milliSeconds, final String application) {
		int eventId = 0;
		try {
			if (dbSession == null) {
				dbSession = DBFactory.getLogSession();
			} else if (conn == null || conn.isClosed()) {
				dbSession = DBFactory.getLogSession();
				conn = dbSession.connect();
			}
			if (dbSession.isProfilingEnabled()) {
				if (conn == null) {
					conn = dbSession.connect();
				}
				final double seconds = ((double) milliSeconds) / 1000;
				final String query = "INSERT INTO profile(Message,Duration,Application,ProfileDateTime) values("
						+ "?,?,?,?)";
				eventId = dbSession.executeUpdate(dbSession.prepareStatement(query, conn, message,
						Double.toString(seconds), application, dbSession.getFormattedDate()), conn);
			}
		} catch (ServiceException | SQLException e) {
			System.out.println("Profile - " + e.getMessage());
		}
		return eventId;
	}

	private static String getExceptionMessageChain(Throwable throwable) {
		String exceptionMessages = "";
		while (throwable != null) {
			exceptionMessages += throwable.getMessage() + "\n";
			throwable = throwable.getCause();
		}
		return exceptionMessages; // ["THIRD EXCEPTION", "SECOND EXCEPTION",
		// "FIRST EXCEPTION"]
	}

	private static String getExceptionCaughtMessage(Exception ex) {
		String caughtMessage = "";
		try {
			ServiceException se = (ServiceException) ex;
			caughtMessage = se.getExceptionCaughtMessage();
		} catch (Exception e) {
		}
		return caughtMessage;
	}

	/**
	 * Log to file
	 * 
	 * @param type
	 * @param message
	 */

	public void logToFile(MessageType type, String message) {
		try {
			long pid = 0;
			try {
				pid = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
			} catch (Exception e) {

			}
			File f = new File(logPath);
			if (f.exists()) {
				// If file size > 1 MB, cut the file
				double bytes = f.length();
				if (bytes >= 1048576) {
					DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					File newFile = new File(
							f.getParent() + File.separator + f.getName().substring(0, f.getName().lastIndexOf("."))
									+ "_" + dateFormat.format(new Date()) + ".log");
					f.renameTo(newFile);
				}
			}

			String msg = dateFormat.format(new Date()) + "|" + pid + "|" + type.toString().toLowerCase() + "|" + message
					+ "\n";
			if (type == MessageType.ERROR) {
				System.err.print(msg);
			} else {
				System.out.print(msg);
			}
			FileOutputStream logStream = new FileOutputStream(new File(logPath), true);
			logStream.write(msg.getBytes());
			logStream.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void logMessage(MessageType type, String message, Exception... exception) {
		try {
			long pid = 0;
			try {
				pid = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
			} catch (Exception e) {

			}
			File file;
			switch (type) {
			case INFO:
				file = new File(infoLogPath);
				break;
			case ERROR:
				file = new File(errorLogPath);
				break;
			default:
				file = new File("");
				break;
			}
			if (file.getName().length() > 0) {
				if (file.exists()) {
					// If file size > 1 MB, cut the file
					double bytes = file.length();
					if (bytes >= 1048576) {
						DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
						File newFile = new File(file.getParent() + File.separator
								+ file.getName().substring(0, file.getName().lastIndexOf(".")) + "_"
								+ dateFormat.format(new Date()) + ".log");
						file.renameTo(newFile);
					}
				}

				String msg = dateFormat.format(new Date()) + "|" + pid + "|" + type.toString().toLowerCase() + "|"
						+ message + "\n";

				StringBuffer stackTrace = new StringBuffer();
				String exceptionMsg = "";
				if (exception.length > 0) {
					exceptionMsg = getExceptionCaughtMessage(exception[0]);
					if (exceptionMsg != null) {
						stackTrace.append("Exception Message: " + exceptionMsg);
					}
					exceptionMsg = getExceptionMessageChain(exception[0]);
					stackTrace.append("Masked Message: " + exceptionMsg);
					stackTrace.append("\nStacktrace: \n");
					if (exception[0].getStackTrace() != null) {
						final StackTraceElement[] trace = exception[0].getStackTrace();
						for (int i = 0; i < trace.length; i++) {
							stackTrace.append(trace[i].toString() + "\n");
						}
					}
				}
				FileOutputStream logStream = new FileOutputStream(file, true);
				logStream.write((msg + stackTrace + "\n").getBytes());
				logStream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Method to load the properties from the application folder
	 * 
	 * @param filename
	 * @return
	 */
	private static Properties loadProperies(String filename) {
		Properties prop = new Properties();
		try {
			String filePath = File.separator + "config" + File.separator + filename + ".properties";
			String propertiesFile = null;
			try {
				final File jarDir = new File(
						DBFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
								.getParentFile();

				if (jarDir != null && jarDir.isDirectory()) {
					propertiesFile = jarDir.getAbsolutePath() + filePath;
				} else {
					propertiesFile = System.getProperty("user.dir") + filePath;
				}

			} catch (Exception e) {
				propertiesFile = System.getProperty("user.dir") + filePath;
			}
			File f = new File(propertiesFile);
			if (f.exists()) {
				final FileInputStream fis = new FileInputStream(propertiesFile);
				prop.load(fis);
				fis.close();
			} else {
				prop = null;
			}
		} catch (Exception ex) {
			prop = null;
			ex.printStackTrace();
		}
		return prop;
	}
}
