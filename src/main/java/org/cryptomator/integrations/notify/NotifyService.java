package org.cryptomator.integrations.notify;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.stream.Stream;

public interface NotifyService {

	/**
	 * Sends a notification to the user.
	 * <p>
	 * A (toast) notification is a message displayed to the user, informing them about an event.
	 * In general, a notification has the following elements:
	 * <ul>
	 *     <li>header - the title what the notification is about</li>
	 *     <li>description - more information about the notification</li>
	 *     <li>interactive elements - actions the user can perform depening on the notification (e.g. buttons)</li>
	 * </ul>
	 *
	 * @param header      Header of the notification
	 * @param description Description of the notification
	 * @param actions     Zero or more {@link NotifyService.Action}s the user can trigger/interact in the notification
	 * @throws NotifyServiceException if an error on displaying the notificaiton occurs
	 */
	void sendNotification(String header, String description, NotifyService.Action... actions) throws NotifyServiceException;

	/**
	 * Record representing possible actions for a notification.
	 * <p>
	 * Performing the actions should invoke the callback.
	 *
	 * @param label _short_ description of the action
	 * @param returnMessage    message sent on action execution
	 */
	record Action(String label, String returnMessage) {
	}


	/**
	 * Loads all supported service providers.
	 *
	 * @return Stream of supported {@link NotifyService} implementations (may be empty)
	 */
	static Stream<NotifyService> loadAll() {
		return IntegrationsLoader.loadAll(NotifyService.class);
	}
}
