package me.github.geocoding.api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import me.github.geocoding.api.util.CommonLib;
import me.github.geocoding.api.util.GeocodingUrl;
import me.github.geocoding.api.util.RestUtil;

public class GeocodingNegativeTest extends GeocodingAPITestBase {
	static RestUtil geocoding;
	static String address, latlng, placeId;

	enum Status {
		INVALID_REQUEST, REQUEST_DENIED, ZERO_RESULTS, UNKNOWN_ERROR;
	}

	@BeforeClass
	public static void setupBeforeClass() {
		geocoding = new RestUtil();
		address = "Colin P Kelly Junior Street";
		latlng = "37.78226710000001,-122.3912479";
		placeId = "ChIJU-lq_neAhYAR9LiPJPEp-Bw";
	}

	@Test
	public void testExceedsMaxUrlChar() {
		checkSetupFailure(getMethodName());
		String address = CommonLib.randomString(8162);
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}

	@Test
	public void testMissingRequiredParam() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		Response res = geocoding.getTestCall().param("region", "us").get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("status"), Status.INVALID_REQUEST.toString(),
				"Status was incorrect.");
	}

	@Test
	public void testUndifiedParam() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().param("undefined", "undefined").get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
	}

	@Test
	public void testInvalidAddressInput() {
		checkSetupFailure(getMethodName());
		String address = CommonLib.randomString(40);
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("status"), Status.ZERO_RESULTS.toString(),
				"Status was incorrect.");
	}

	@Test
	public void testAddressIntOnly() {
		checkSetupFailure(getMethodName());
		String address = CommonLib.randomNumber(15);
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("status"), Status.ZERO_RESULTS.toString(),
				"Status was incorrect.");
	}

	@Test
	public void testInvalidLatlngType() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.REVERSE_GEOCODING_DEFAULT.getUrl("invalid");
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("status"), Status.INVALID_REQUEST.toString(),
				"Status was incorrect.");
	}

	@Test
	public void testLatLngOutOfRange() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.REVERSE_GEOCODING_DEFAULT.getUrl("91,181");
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}

	@Test
	public void testExclusiveComponentFilter() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		String component = "administrative_area:TX|country:CN";
		Response res = geocoding.getTestCall().param("components", component).get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
	}

	@Test
	public void testInvalidComponentType() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		String component = "invalidType:TX";
		Response res = geocoding.getTestCall().param("components", component).get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}

	@Test
	public void testComponentFilterInvalidFormat() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		String component = "administrative_area:TX(country:CN)";
		Response res = geocoding.getTestCall().param("components", component).get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}

	@Test
	public void testBoundsInvalidFormat() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		String bounds = "34,-118(34,-181)";
		Response res = geocoding.getTestCall().param("bounds", bounds).get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}

	@Test
	public void testComponentBoundsInvalidValue() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		String bounds = "invalidValue";
		Response res = geocoding.getTestCall().param("bounds", bounds).get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}

	@Test
	public void testComponentBoundsOutOfRange() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		String bounds = "34,-118|34,-181";
		Response res = geocoding.getTestCall().param("bounds", bounds).get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}

	@Test
	public void testInvalidRegion() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		String region = "invalid";
		Response res = geocoding.getTestCall().param("region", region).get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
	}

	@Test
	public void testRequiredParamEmptyValue() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(" ");
		Response res = geocoding.getTestCall().get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("status"), Status.ZERO_RESULTS.toString(),
				"Status was incorrect.");
	}

	@Test
	public void testAddressWithLatlng() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().param("latlng", "12.321001,-112.3461").get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
	}

	@Test
	public void testMethodNotAllowed() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().delete(url);
		Assert.assertEquals(res.getStatusCode(), 405, "Status code was incorrect.");
	}

	@Test
	public void testPlaceIdAccessDenied() {
		checkSetupFailure(getMethodName());
		String url = GeocodingUrl.GEOCODING_JSON.getUrl();
		Response res = geocoding.getTestCall().param("place_id", placeId).get(url);
		Assert.assertEquals(res.getStatusCode(), 200, "Status code was incorrect.");
		Assert.assertEquals(res.jsonPath().getString("status"), Status.REQUEST_DENIED.toString(),
				"Status was incorrect.");
	}

	@Test
	public void testInvalidURL() {
		checkSetupFailure(getMethodName());
		String url = "/geocode/v1/json";
		Response res = geocoding.getTestCall().param("address", address).get(url);
		Assert.assertEquals(res.getStatusCode(), 404, "Status code was incorrect.");
	}

	@Test
	public void testNoURLEncoding() {
		checkSetupFailure(getMethodName());
		String address = CommonLib.randomString(8162);
		String url = GeocodingUrl.GEOCODING_DEFAULT.getUrl(address);
		Response res = geocoding.getTestCall().urlEncodingEnabled(false).get(url);
		Assert.assertEquals(res.getStatusCode(), 400, "Status code was incorrect.");
	}
}
