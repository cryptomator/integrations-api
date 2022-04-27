package org.cryptomator.integrations.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class IntegrationsLoaderTest {

	@Nested
	@DisplayName("@CheckAvailability on static methods")
	public class StaticAvailabilityChecks {

		@CheckAvailability
		private static class StaticTrue {
			@CheckAvailability
			public static boolean test() {
				return true;
			}
		}

		@CheckAvailability
		private static class StaticFalse {
			@CheckAvailability
			public static boolean test() {
				return false;
			}
		}

		@Test
		@DisplayName("no @CheckAvailability will always pass")
		public void testPassesAvailabilityCheck0() {
			// @formatter:off
			class C1 {}
			@CheckAvailability class C2 {}
			class C3 {
				@CheckAvailability public static boolean test() { return false; }
			}
			// @formatter:on

			Assertions.assertTrue(IntegrationsLoader.passesStaticAvailabilityCheck(C1.class));
			Assertions.assertTrue(IntegrationsLoader.passesStaticAvailabilityCheck(C2.class));
			Assertions.assertTrue(IntegrationsLoader.passesStaticAvailabilityCheck(C3.class));
		}

		@Test
		@DisplayName("@CheckAvailability on non-conforming methods will be skipped")
		public void testPassesAvailabilityCheck1() {
			// @formatter:off
			@CheckAvailability class C1 {
				@CheckAvailability private static boolean test1() { return false; }
				@CheckAvailability public static boolean test2(String foo) { return false; }
				@CheckAvailability public static String test3() { return "false"; }
			}
			// @formatter:on

			Assertions.assertTrue(IntegrationsLoader.passesStaticAvailabilityCheck(C1.class));
		}

		@Test
		@DisplayName("@CheckAvailability on static method")
		public void testPassesAvailabilityCheck2() {
			Assertions.assertTrue(IntegrationsLoader.passesStaticAvailabilityCheck(StaticTrue.class));
			Assertions.assertFalse(IntegrationsLoader.passesStaticAvailabilityCheck(StaticFalse.class));
		}

		@Test
		@DisplayName("@CheckAvailability on inherited static method")
		public void testPassesAvailabilityCheck3() {
			// @formatter:off
			class C1 extends StaticTrue {}
			class C2 extends StaticFalse {}
			// @formatter:on

			Assertions.assertTrue(IntegrationsLoader.passesStaticAvailabilityCheck(C1.class));
			Assertions.assertFalse(IntegrationsLoader.passesStaticAvailabilityCheck(C2.class));
		}

		@Test
		@DisplayName("multiple @CheckAvailability methods")
		public void testPassesAvailabilityCheck4() {
			// @formatter:off
			class C1 extends StaticTrue {
				@CheckAvailability public static boolean test1() { return false; }
			}
			class C2 extends StaticFalse {
				@CheckAvailability public static boolean test1() { return true; }
			}
			@CheckAvailability class C3 {
				@CheckAvailability public static boolean test1() { return true; }
				@CheckAvailability public static boolean test2() { return false; }
			}
			// @formatter:on

			Assertions.assertFalse(IntegrationsLoader.passesStaticAvailabilityCheck(C1.class));
			Assertions.assertFalse(IntegrationsLoader.passesStaticAvailabilityCheck(C2.class));
			Assertions.assertFalse(IntegrationsLoader.passesStaticAvailabilityCheck(C3.class));
		}


	}

	@Nested
	@DisplayName("@CheckAvailability on instance methods")
	public class InstanceAvailabilityChecks {

		@CheckAvailability
		private static class InstanceTrue {
			@CheckAvailability
			public boolean test() {
				return true;
			}
		}

		@CheckAvailability
		private static class InstanceFalse {
			@CheckAvailability
			public boolean test() {
				return false;
			}
		}

		@Test
		@DisplayName("no @CheckAvailability will always pass")
		public void testPassesAvailabilityCheck0() {
			// @formatter:off
			class C1 {}
			@CheckAvailability class C2 {}
			class C3 {
				@CheckAvailability public boolean test() { return false; }
			}
			// @formatter:on

			Assertions.assertTrue(IntegrationsLoader.passesInstanceAvailabilityCheck(new C1()));
			Assertions.assertTrue(IntegrationsLoader.passesInstanceAvailabilityCheck(new C2()));
			Assertions.assertTrue(IntegrationsLoader.passesInstanceAvailabilityCheck(new C3()));
		}

		@Test
		@DisplayName("@CheckAvailability on non-conforming instance methods will be skipped")
		public void testPassesAvailabilityCheck1() {
			// @formatter:off
			@CheckAvailability class C1 {
				@CheckAvailability private boolean test1() { return false; }
				@CheckAvailability public boolean test2(String foo) { return false; }
				@CheckAvailability public String test3() { return "false"; }
			}
			// @formatter:on

			Assertions.assertTrue(IntegrationsLoader.passesInstanceAvailabilityCheck(C1.class));
		}

		@Test
		@DisplayName("@CheckAvailability on instance method")
		public void testPassesAvailabilityCheck2() {
			Assertions.assertTrue(IntegrationsLoader.passesInstanceAvailabilityCheck(new InstanceTrue()));
			Assertions.assertFalse(IntegrationsLoader.passesInstanceAvailabilityCheck(new InstanceFalse()));
		}

		@Test
		@DisplayName("@CheckAvailability on inherited instance method")
		public void testPassesAvailabilityCheck3() {
			// @formatter:off
			class C1 extends InstanceTrue {}
			class C2 extends InstanceFalse {}
			// @formatter:on

			Assertions.assertTrue(IntegrationsLoader.passesInstanceAvailabilityCheck(new C1()));
			Assertions.assertFalse(IntegrationsLoader.passesInstanceAvailabilityCheck(new C2()));
		}

		@Test
		@DisplayName("multiple @CheckAvailability methods")
		public void testPassesAvailabilityCheck4() {
			// @formatter:off
			class C1 extends InstanceTrue {
				@CheckAvailability public boolean test1() { return false; }
			}
			class C2 extends InstanceFalse {
				@CheckAvailability public boolean test1() { return true; }
			}
			@CheckAvailability class C3 {
				@CheckAvailability public boolean test1() { return true; }
				@CheckAvailability public boolean test2() { return false; }
			}
			// @formatter:on

			Assertions.assertFalse(IntegrationsLoader.passesInstanceAvailabilityCheck(new C1()));
			Assertions.assertFalse(IntegrationsLoader.passesInstanceAvailabilityCheck(new C2()));
			Assertions.assertFalse(IntegrationsLoader.passesInstanceAvailabilityCheck(new C3()));
		}

	}

}