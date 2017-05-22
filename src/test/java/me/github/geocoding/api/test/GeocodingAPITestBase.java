package me.github.geocoding.api.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;

public class GeocodingAPITestBase {
	protected static String host, context;
	protected static int port;
	protected static Properties configFile;
	protected static Exception setupFailure;

	public GeocodingAPITestBase() {
	}

	@BeforeClass
	public static void setupBeforeTest() {
		InputStream is = null;
		configFile = new Properties();
		try {
			is = new FileInputStream(System.getProperty("user.dir") + "/src/test/config/config.properties");
			configFile.load(is);
			init();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			setupFailure = e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public static void init() throws Exception {
		System.out.println("Initializing Geocoding API Tests...");
		host = configFile.getProperty("HOST");
		if (configFile.getProperty("PORT") != null && !configFile.getProperty("PORT").isEmpty())
			port = Integer.valueOf(configFile.getProperty("PORT"));
		context = (configFile.getProperty("CONTEXT"));
		if (host == null || host.isEmpty())
			throw new Exception("HOST or PORT can't be null");
	}

	protected static void checkSetupFailure() {
		checkSetupFailure("Geocoding API Test");
	}

	protected static void checkSetupFailure(String testName) {
		System.out.println("Init Test: " + testName + "...");
		Assert.assertNull(setupFailure, testName + " Test setup failed.");
	}

	protected String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	protected String getMethodName(int depth) {
		return Thread.currentThread().getStackTrace()[depth + 2].getMethodName();
	}

	/**
	 * @return the host
	 */
	public static String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public static void setHost(String host) {
		GeocodingAPITestBase.host = host;
	}

	/**
	 * @return the port
	 */
	public static int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public static void setPort(int port) {
		GeocodingAPITestBase.port = port;
	}

	/**
	 * @return the context
	 */
	public static String getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public static void setContext(String context) {
		GeocodingAPITestBase.context = context;
	}
}
