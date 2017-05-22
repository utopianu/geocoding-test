package me.github.geocoding.api.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import me.github.geocoding.api.util.CommonLib;
import me.github.geocoding.api.util.GeocodingUrl;
import me.github.geocoding.api.util.RestUtil;

public class GeocodingAPITest extends GeocodingAPITestBase {
	static Response res;
	static RestUtil geocoding;
	static String url;
	static JsonPath expVal;
	static String address;

	@BeforeClass
	public static void setupBeforeClass() {
		expVal = new JsonPath(new File(System.getProperty("user.dir") + "/src/test/data/expVal.json"));
		address = expVal.getString("results.formatted_address");
		geocoding = new RestUtil();
		url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		res = geocoding.getTestCall().log().all().get(url);
		res.prettyPrint();
	}

	@DataProvider(name = "ResultFields")
	public static String[] jsonString() {
		return new String[] { "results.formatted_address", "results.place_id", "status", "results.types" };
	}

	@DataProvider(name = "AddressComponents")
	public static String[] components() {
		List<String> data = new ArrayList<String>();
		String components = "results[0].address_components";
		for (int i = 0; i < expVal.getList(components).size(); i++) {
			data.add(components + "[" + i + "].short_name");
			data.add(components + "[" + i + "].long_name");
			data.add(components + "[" + i + "].types");
		}
		return data.toArray(new String[data.size()]);
	}

	@DataProvider(name = "Geometry")
	public static String[] geometry() {
		return new String[] { "results.geometry.location.lat", "results.geometry.location.lng",
				"results.geometry.location_type", "results.geometry.location.viewport.southwest.lat",
				"results.geometry.viewport.southwest.lng", "results.geometry.viewport.northeast.lng",
				"results.geometry.viewport.northeast.lat", };
	}

	@Test
	public void verifyStatusCode() {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(res.getStatusCode(), 200, "Status code is incorrect.");

	}

	@Test(dataProvider = "ResultFields")
	public void verifyAddressInfo(String field) {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(res.jsonPath().getString(field), expVal.getString(field), field + " was incorrect.");
	}

	@Test
	public void verifyNumComponents() {
		checkSetupFailure(getMethodName());
		String validationField = "results.address_components";
		Assert.assertEquals(res.jsonPath().getList(validationField).size(), expVal.getList(validationField).size(),
				"Number of address components was incorrect.");
	}

	@Test(dataProvider = "AddressComponents")
	public void verifyAddressComponents(String field) {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(res.jsonPath().getString(field), expVal.getString(field), field + " was incorrect.");
	}

	@Test(dataProvider = "Geometry")
	public void verifyGeometryInfo(String field) {
		checkSetupFailure(getMethodName());
		Assert.assertEquals(res.jsonPath().getString(field), expVal.getString(field), field + " was incorrect.");
	}

	@Test
	public void verifyAddressTypeZipcode() {
		checkSetupFailure(getMethodName());
		String address = "94107";
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("results[0].types[0]"), "postal_code",
				"Result type is incorrect.");
	}

	@Test
	public void verifyAddressTypeAirport() {
		checkSetupFailure(getMethodName());
		String address = "SFO";
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("results[0].types[0]"), "airport", "Result type is incorrect.");
	}

	@Test
	public void verifyAddressRecommationTypo() {
		checkSetupFailure(getMethodName());
		String address = expVal.getString("results.formatted_address").replaceAll("Colin", "Collin");
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("result.formatted_address"),
				expVal.getString("result.formatted_address"), "formatted_address is incorrect.");
	}

	@Test
	public void verifyAddressCity() {
		checkSetupFailure(getMethodName());
		String address = "San Francisco";
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("results[0].types[0]"), "locality", "Result type is incorrect.");
	}

	@Test
	public void verifyAddressCountry() {
		checkSetupFailure(getMethodName());
		String address = "USA";
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("results[0].types[0]"), "country", "Result type is incorrect.");
	}

	@Test
	public void verifyAddressHeaderOverride() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		Response res = geocoding.getTestCall().accept(ContentType.XML).param("address", address).get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("status"), "OK", "Status message is incorrect.");
	}

	@Test
	public void verifyAddressParamRegion() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		String address = "kings road";
		Response res = geocoding.getTestCall().param("address", address).param("region", "gb").get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertTrue(res.jsonPath().getString("results[0].formatted_address").contains("UK"),
				"Address not in the region specified.");
	}

	@Test
	public void verifyAddressParamBounds() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		String address = "Winnetka";
		String bounds = "34.172684,-118.604794|34.236144,-118.500938";
		Response res = geocoding.getTestCall().param("address", address).param("bounds", bounds).get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertTrue(res.jsonPath().getString("results[0].formatted_address").contains("Los Angeles"),
				"Address not in the region specified.");
	}

	@Test
	public void verifyAddressParamBoundsInt() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		String address = "Winnetka";
		String bounds = "34,-118|34,-118";
		Response res = geocoding.getTestCall().param("address", address).param("bounds", bounds).get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertTrue(res.jsonPath().getString("results[0].formatted_address").contains("Los Angeles"),
				"Address not in the region specified.");
	}

	@Test
	public void verifyAddressComponentFilter() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl("88 Colin st");
		Response res = geocoding.getTestCall().param("components", "country:GB").get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertTrue(res.jsonPath().getString("results[0].formatted_address").contains("UK"),
				"Address not in the region specified.");
	}

	@Test
	public void verifyAddressComponentFilterOnly() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		String component = "administrative_area:TX|country:US";
		Response res = geocoding.getTestCall().param("components", component).get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("results[0].formatted_address"), "Texas, USA",
				"Address not in the region specified.");
	}

	@Test
	public void verifyAddressMaxChars() {
		checkSetupFailure(getMethodName());
		String address = CommonLib.randomString(8161);
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
	}

	@Test
	public void verifyAddressSpecialChars() {
		checkSetupFailure(getMethodName());
		String address = "Colin P Kelly Jr. & Townsend St.";
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
	}
}
