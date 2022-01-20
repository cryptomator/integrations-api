package org.cryptomator.integrations.tray;


import org.cryptomator.integrations.common.Priority;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Priority(Priority.FALLBACK)
public class AwtTrayMenuController implements TrayMenuController {

	private TrayIcon trayIcon;

	@Override
	public void showTrayIcon(InputStream iconData, String tooltip) throws IOException {
		var image = Toolkit.getDefaultToolkit().createImage(iconData.readAllBytes());
		trayIcon = new TrayIcon(image, tooltip);
	}

	@Override
	public void setTrayMenu(List<TrayMenuItem> items) {
		if (trayIcon != null) {
			var menu = new PopupMenu();
			addChildren(menu, items);
			trayIcon.setPopupMenu(menu);
		}
	}

	private void addChildren(Menu menu, List<TrayMenuItem> items) {
		for (var item : items) {
			// TODO: use Pattern Matching for switch, once available
			if (item instanceof ActionItem a) {
				var menuItem = new MenuItem(a.title());
				menuItem.addActionListener(evt -> a.action().run());
				menu.add(menuItem);
			} else if (item instanceof SeparatorItem) {
				menu.addSeparator();
			} else if (item instanceof SubMenuItem s) {
				var submenu = new Menu(s.title());
				addChildren(submenu, s.items());
				menu.add(submenu);
			}
		}
	}

}
