package org.cryptomator.integrations.update;

public record BasicUpdateInfo(String version, UpdateMechanism<BasicUpdateInfo> updateMechanism) implements UpdateInfo<BasicUpdateInfo> {
}
