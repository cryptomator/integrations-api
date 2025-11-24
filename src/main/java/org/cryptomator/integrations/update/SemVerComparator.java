package org.cryptomator.integrations.update;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Compares version strings according to <a href="http://semver.org/spec/v2.0.0.html">SemVer 2.0.0</a>.
 */
public class SemVerComparator implements Comparator<String> {

	public static final SemVerComparator INSTANCE = new SemVerComparator();

	private static final Pattern VERSION_SEP = Pattern.compile("\\."); // http://semver.org/spec/v2.0.0.html#spec-item-2
	private static final String PRE_RELEASE_SEP = "-"; // http://semver.org/spec/v2.0.0.html#spec-item-9
	private static final String BUILD_SEP = "+"; // http://semver.org/spec/v2.0.0.html#spec-item-10

	@Override
	public int compare(String version1, String version2) {
		// "Build metadata SHOULD be ignored when determining version precedence.
		// Thus, two versions that differ only in the build metadata, have the same precedence."
		String trimmedV1 = substringBefore(version1, BUILD_SEP);
		String trimmedV2 = substringBefore(version2, BUILD_SEP);

		if (trimmedV1.equals(trimmedV2)) {
			return 0;
		}

		String v1MajorMinorPatch = substringBefore(trimmedV1, PRE_RELEASE_SEP);
		String v2MajorMinorPatch = substringBefore(trimmedV2, PRE_RELEASE_SEP);
		String v1PreReleaseVersion = substringAfter(trimmedV1, PRE_RELEASE_SEP);
		String v2PreReleaseVersion = substringAfter(trimmedV2, PRE_RELEASE_SEP);
		return compare(v1MajorMinorPatch, v1PreReleaseVersion, v2MajorMinorPatch, v2PreReleaseVersion);
	}

	private static int compare(String v1MajorMinorPatch, String v1PreReleaseVersion, String v2MajorMinorPatch, String v2PreReleaseVersion) {
		int comparisonResult = compareNumericallyThenLexicographically(v1MajorMinorPatch, v2MajorMinorPatch);
		if (comparisonResult == 0) {
			if (v1PreReleaseVersion.isEmpty()) {
				return 1; // 1.0.0 > 1.0.0-BETA
			} else if (v2PreReleaseVersion.isEmpty()) {
				return -1; // 1.0.0-BETA < 1.0.0
			} else {
				return compareNumericallyThenLexicographically(v1PreReleaseVersion, v2PreReleaseVersion);
			}
		} else {
			return comparisonResult;
		}
	}

	private static int compareNumericallyThenLexicographically(String version1, String version2) {
		final String[] vComps1 = VERSION_SEP.split(version1);
		final String[] vComps2 = VERSION_SEP.split(version2);
		final int commonCompCount = Math.min(vComps1.length, vComps2.length);

		for (int i = 0; i < commonCompCount; i++) {
			int subversionComparisonResult;
			try {
				final int v1 = Integer.parseInt(vComps1[i]);
				final int v2 = Integer.parseInt(vComps2[i]);
				subversionComparisonResult = v1 - v2;
			} catch (NumberFormatException ex) {
				// ok, lets compare this fragment lexicographically
				subversionComparisonResult = vComps1[i].compareTo(vComps2[i]);
			}
			if (subversionComparisonResult != 0) {
				return subversionComparisonResult;
			}
		}

		// all in common so far? longest version string is considered the higher version:
		return vComps1.length - vComps2.length;
	}

	private static String substringBefore(String str, String separator) {
		int index = str.indexOf(separator);
		return index == -1 ? str : str.substring(0, index);
	}

	private static String substringAfter(String str, String separator) {
		int index = str.indexOf(separator);
		return index == -1 ? "" : str.substring(index + separator.length());
	}

}