package org.cryptomator.integrations.common;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(DistributionChannel.DistributionChannels.class)
@ApiStatus.Experimental
public @interface DistributionChannel {
	Value value() default Value.UNKNOWN;

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@interface DistributionChannels {
		DistributionChannel[] value();
	}

	enum Value {
		LINUX_APPIMAGE,
		LINUX_AUR,
		LINUX_FLATPAK,
		LINUX_NIXOS,
		LINUX_PPA,
		MAC_BREW,
		MAC_DMG,
		WINDOWS_EXE,
		WINDOWS_MSI,
		WINDOWS_WINGET,
		UNKNOWN;

	}
}
