package com.example.servicehub.util;

public abstract class ProjectUtils {

	private ProjectUtils() {
	}

	/**
	 * 현재 환경의 도메인을 반환한다.
	 * 반드시 정의해주어야함.
	 */
	public static String getDomain() {
		return "http://impatient.iptime.org:8079";
	}
}
