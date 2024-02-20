package org.cryptomator.integrations.common;

@CheckAvailability
public class InitExceptionTestClass {

	private static final String TEST;

	static {
		TEST = throwSomething();
	}

	public InitExceptionTestClass() {

	}

	static String throwSomething() {
		throw new RuntimeException("STATIC FAIL");
	}

	@CheckAvailability
	public static boolean test() {
		return true;
	}

}
