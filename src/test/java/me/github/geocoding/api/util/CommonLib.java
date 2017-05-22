package me.github.geocoding.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommonLib {
	static final String A_Z = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static final String NUMBER = "0123456789";
	static Random rnd = new Random();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(A_Z.charAt(rnd.nextInt(A_Z.length())));
		return sb.toString();
	}

	public static String randomNumber(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(NUMBER.charAt(rnd.nextInt(NUMBER.length())));
		return sb.toString();
	}

	public static Map<String, String> createMap(String mapValues) {
		Map<String, String> map = new HashMap<String, String>();
		String[] pairs = mapValues.split(",");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			String[] keyValue = pair.split("->");
			map.put(keyValue[0], keyValue[1]);
		}
		return map;
	}
}
