package me.github.geocoding.api.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import me.github.geocoding.api.util.GeocodingUrl;
import me.github.geocoding.api.util.RestUtil;

public class GeocodingPerformanceTest extends GeocodingAPITestBase {
	static String address, url;
	static RestUtil geocoding;
	static long startTime, endTime;

	@BeforeClass
	public static void setupBeforeClass() {
		geocoding = new RestUtil();
		address = "Colin P Kelly Junior Street";
		url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		startTime = System.currentTimeMillis();
		System.out.println("Load test started at " + startTime);
	}

	@Test(invocationCount = 1000, threadPoolSize = 100)
	public void loadTest() {
		System.out.printf("%nThread Id %s Started... ", Thread.currentThread().getId());
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code is incorrect.");
		System.out.printf("%nThread Id %s Ended... ", Thread.currentThread().getId());
	}

	@AfterClass
	public static void teardown() {
		endTime = System.currentTimeMillis();
		System.out.println("Load test ended at " + endTime);
		System.out.println("Total time spent: " + (endTime - startTime) / 1000 + "s");
	}

}
