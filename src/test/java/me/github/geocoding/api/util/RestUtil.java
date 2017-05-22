package me.github.geocoding.api.util;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import me.github.geocoding.api.test.GeocodingAPITestBase;

public class RestUtil {
	private RequestSpecification testCall;
	private String host, context, payload, uri;
	private int port;
	private String username, password;
	private Map<String, String> headers, params;
	private RequestType method;

	public RestUtil(String host, int port, String context) {
		RestAssured.baseURI = host;
		RestAssured.port = port;
		RestAssured.basePath = context;
	}

	public RestUtil() {
		this(GeocodingAPITestBase.getHost(), GeocodingAPITestBase.getPort(), GeocodingAPITestBase.getContext());
	}

	public RequestSpecification getTestCall(String uri, String payload, Map<String, String> header, RequestType method,
			ContentType contentType, Map<String, String> params) {
		setUri(uri);
		setParams(params);
		setHeaders(headers);
		setMethod(method);
		setPayload(payload);
		testCall = testCall.contentType(contentType);
		if (headers != null && !headers.isEmpty())
			testCall = testCall.headers(headers);
		if (payload != null && !payload.isEmpty())
			testCall = testCall.body(payload);
		if (params != null && !params.isEmpty())
			testCall = testCall.params(params);
		return testCall.log().all();
	}

	public enum RequestType {
		GET, POST, UPDATE, PATCH, DELETE;
	}

	public RequestSpecification getTestCall() {
		return RestAssured.given().log().all();
	}

	/**
	 * @param testCall
	 *            the testCall to set
	 */
	public void setTestCall(RequestSpecification testCall) {
		this.testCall = testCall;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * @return the method
	 */
	public RequestType getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(RequestType method) {
		this.method = method;
	}
}