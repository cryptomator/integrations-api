package org.cryptomator.integrations.common;

@CheckAvailability
public class InitExceptionTestClass {

	private static final String TEST;

	static {
		TEST = throwSomethig();
	}

	public InitExceptionTestClass() {

	}

	static String throwSomethig() {
		throw new RuntimeException("STATIC FAIL");
	}

	@CheckAvailability
	public static boolean test() {
		return true;
	}

}
