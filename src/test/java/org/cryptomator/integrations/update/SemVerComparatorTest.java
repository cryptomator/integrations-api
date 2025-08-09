package org.cryptomator.integrations.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Comparator;

public class SemVerComparatorTest {

	private final Comparator<String> semVerComparator = SemVerComparator.INSTANCE;

	// equal versions

	@ParameterizedTest
	@CsvSource({
			"1.23.4, 1.23.4",
			"1.23.4-alpha, 1.23.4-alpha",
			"1.23.4+20170101, 1.23.4+20171231",
			"1.23.4-alpha+20170101, 1.23.4-alpha+20171231"
	})
	public void compareEqualVersions(String left, String right) {
		Assertions.assertEquals(0, Integer.signum(semVerComparator.compare(left, right)));
	}

	// newer versions in first argument

	@ParameterizedTest
	@CsvSource({
			"1.23.5, 1.23.4",
			"1.24.4, 1.23.4",
			"1.23.4, 1.23",
			"1.23.4, 1.23.4-SNAPSHOT",
			"1.23.4, 1.23.4-56.78",
			"1.23.4-beta, 1.23.4-alpha",
			"1.23.4-alpha.1, 1.23.4-alpha",
			"1.23.4-56.79, 1.23.4-56.78",
			"1.23.4-alpha, 1.23.4-1",
	})
	public void compareHigherToLowerVersions(String higherVersion, String lowerVersion) {
		Assertions.assertEquals(1, Integer.signum(semVerComparator.compare(higherVersion, lowerVersion)));
	}

	// newer versions in second argument

	@ParameterizedTest
	@CsvSource({
			"1.23.4, 1.23.5",
			"1.23.4, 1.24.4",
			"1.23, 1.23.4",
			"1.23.4-SNAPSHOT, 1.23.4",
			"1.23.4-56.78, 1.23.4",
			"1.23.4-alpha, 1.23.4-beta",
			"1.23.4-alpha, 1.23.4-alpha.1",
			"1.23.4-56.78, 1.23.4-56.79",
			"1.23.4-1, 1.23.4-alpha",
	})
	public void compareLowerToHigherVersions(String lowerVersion, String higherVersion) {
		Assertions.assertEquals(-1, Integer.signum(semVerComparator.compare(lowerVersion, higherVersion)));
	}

	// test vector from https://semver.org/spec/v2.0.0.html#spec-item-11:
	// Example: 1.0.0-alpha < 1.0.0-alpha.1 < 1.0.0-alpha.beta < 1.0.0-beta < 1.0.0-beta.2 < 1.0.0-beta.11 < 1.0.0-rc.1 < 1.0.0.
	@ParameterizedTest
	@CsvSource({
			"1.0.0-alpha, 1.0.0-alpha.1",
			"1.0.0-alpha.1, 1.0.0-alpha.beta",
			"1.0.0-alpha.beta, 1.0.0-beta",
			"1.0.0-beta, 1.0.0-beta.2",
			"1.0.0-beta.2, 1.0.0-beta.11",
			"1.0.0-beta.11, 1.0.0-rc.1",
			"1.0.0-rc.1, 1.0.0"
	})
	public void testPrecedenceSpec(String left, String right) {
		Assertions.assertEquals(-1, Integer.signum(semVerComparator.compare(left, right)));
	}

}
