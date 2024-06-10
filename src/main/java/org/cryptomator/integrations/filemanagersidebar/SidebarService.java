package org.cryptomator.integrations.filemanagersidebar;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.Optional;

public interface SidebarService {

	SidebarEntry add();

	interface SidebarEntry {
		void remove();
	}

	static Optional<SidebarService> get() {
		return IntegrationsLoader.load(SidebarService.class);
	}
}
