package me.github.geocoding.api.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import me.github.geocoding.api.util.GeocodingUrl;
import me.github.geocoding.api.util.RestUtil;

public class ReverseGeocodingAPITest extends GeocodingAPITestBase {
	static Response res;
	static RestUtil geocoding;
	static String url;
	static JsonPath expVal, result;
	static String latlng;

	@BeforeClass
	public static void setupBeforeClass() {
		expVal = new JsonPath(new File(System.getProperty("user.dir") + "/src/test/data/expVal.json"));
		expVal.setRoot("results[0]");
		latlng = expVal.getString("geometry.location.lat") + "," + expVal.getString("geometry.location.lng");
		geocoding = new RestUtil();
		url = GeocodingUrl.REVERSE_GEOCODING_DEFAULT.getUrl(latlng);
		res = geocoding.getTestCall().log().all().get(url);
		getResult();
		System.out.println(result.getString("formatted_address"));
	}

	private static JsonPath getResult() {
		for (int i = 0; i < res.jsonPath().getList("results").size(); i++) {
			if (res.jsonPath().getString("results[" + i + "].formatted_address")
					.equals(expVal.getString("formatted_address"))) {
				result = res.jsonPath().setRoot("results[" + i + "]");
			}
		}
		return result;
	}

	@DataProvider(name = "ResultFields")
	public static String[] jsonString() {
		return new String[] { "formatted_address", "place_id", "status", "types" };
	}

	@DataProvider(name = "AddressComponents")
	public static String[] components() {
		List<String> data = new ArrayList<String>();
		String components = "address_components";
		for (int i = 0; i < expVal.getList(components).size(); i++) {
			data.add(components + "[" + i + "].short_name");
			data.add(components + "[" + i + "].long_name");
			data.add(components + "[" + i + "].types");
		}
		return data.toArray(new String[data.size()]);
	}

	@DataProvider(name = "Geometry")
	public static String[] geometry() {
		return new String[] { "geometry.location.lat", "geometry.location.lng", "geometry.location_type",
				"geometry.location.viewport.southwest.lat", "geometry.viewport.southwest.lng",
				"geometry.viewport.northeast.lng", "geometry.viewport.northeast.lat", };
	}

	@Test
	public void verifyStatusCode() {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(res.getStatusCode(), 200, "Status code is incorrect.");

	}

	@Test
	public void verifyResultListSize() {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(res.jsonPath().getList("results").size(), 10, "Result list size is incorrect.");

	}

	@Test
	public void verifyNumComponents() {
		checkSetupFailure(getMethodName());
		String validationField = "address_components";
		Assert.assertEquals(result.getList(validationField).size(), expVal.getList(validationField).size(),
				"Number of address components was incorrect.");
	}

	@Test(dataProvider = "ResultFields")
	public void verifyAddressInfo(String field) {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(result.getString(field), expVal.getString(field), field + " was incorrect.");
	}

	@Test(dataProvider = "AddressComponents")
	public void verifyAddressComponents(String field) {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(result.getString(field), expVal.getString(field), field + " was incorrect.");
	}

	@Test(dataProvider = "Geometry")
	public void verifyGeometryInfo(String field) {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(result.getString(field), expVal.getString(field), field + " was incorrect.");
	}

	@Test
	public void verifyAddressLatlngInt() {
		checkSetupFailure(getMethodName());
		String latlng = "45,12";
		String url = GeocodingUrl.REVERSE_GEOCODING_DEFAULT.getUrl(latlng);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
	}
}
