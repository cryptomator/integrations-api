package org.cryptomator.integrations.keychain;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * This is the interface used by Cryptomator to store passwords securely in external keychains, such as system keychains or password managers.
 */
public interface KeychainAccessProvider {

	/**
	 * Loads all available KeychainAccessProvider.
	 *
	 * @return a stream of {@link #isSupported() supported} KeychainAccessProviders
	 * @since 1.1.0
	 */
	static Stream<KeychainAccessProvider> get() {
		return IntegrationsLoader.loadAll(KeychainAccessProvider.class).filter(KeychainAccessProvider::isSupported);
	}

	/**
	 * A name to display in UI elements. If required, this should be localized.
	 *
	 * @return user-friendly name (must not be null or empty)
	 */
	@Nls(capitalization = Nls.Capitalization.Title)
	String displayName();

	/**
	 * Associates a passphrase with a given key.
	 *
	 * @param key        Key used to retrieve the passphrase via {@link #loadPassphrase(String)}.
	 * @param passphrase The secret to store in this keychain.
	 * @throws KeychainAccessException If storing the password failed
	 * @deprecated Please use {@link #storePassphrase(String, String, CharSequence)} instead
	 */
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
	default void storePassphrase(String key, CharSequence passphrase) throws KeychainAccessException {
		storePassphrase(key, null, passphrase);
	}

	/**
	 * Associates a passphrase with a given key and a name for that key.
	 *
	 * @param key         Key used to retrieve the passphrase via {@link #loadPassphrase(String)}.
	 * @param displayName The according name to the key. That's the name of the vault displayed in the UI.
	 *                    It's passed to the keychain as an additional information about the vault besides the key.
	 *                    The parameter does not need to be unique or be checked by the keychain.
	 * @param passphrase  The secret to store in this keychain.
	 * @throws KeychainAccessException If storing the password failed
	 */
	@Blocking
	void storePassphrase(String key, @Nullable String displayName, CharSequence passphrase) throws KeychainAccessException;

	 * {@link #storePassphrase(String key, String displayName, CharSequence passphrase)}, the user needs to authenticate
	 * to store a passphrase. The authentication mechanism is provided by the operating system dependent
	 *
	 * @param key         Key used to retrieve the passphrase via {@link #loadPassphrase(String)}.
	 * @param displayName The according name to the key. That's the name of the vault displayed in the UI.
	 *                    It's passed to the keychain as an additional information about the vault besides the key.
	 *                    The parameter does not need to be unique or be checked by the keychain.
	 * @param passphrase  The secret to store in this keychain.
	 * @throws KeychainAccessException If storing the password failed
	 */
	@Blocking
	void storePassphraseForAuthenticatedUser(String key, @Nullable String displayName, CharSequence passphrase) throws KeychainAccessException;

	/**
	 * @param key Unique key previously used while {@link #storePassphrase(String, String, CharSequence)}  storing a passphrase}.
	 * @return The stored passphrase for the given key or <code>null</code> if no value for the given key could be found.
	 * @throws KeychainAccessException If loading the password failed
	 */
	@Blocking
	char[] loadPassphrase(String key) throws KeychainAccessException;

	/**
	 * Deletes a passphrase with a given key.
	 *
	 * @param key Unique key previously used while {@link #storePassphrase(String, String, CharSequence)}  storing a passphrase}.
	 * @throws KeychainAccessException If deleting the password failed
	 */
	void deletePassphrase(String key) throws KeychainAccessException;

	/**
	 * Updates a passphrase with a given key. Noop, if there is no item for the given key.
	 *
	 * @param key        Unique key previously used while {@link #storePassphrase(String, String, CharSequence)}  storing a passphrase}.
	 * @param passphrase The secret to be updated in this keychain.
	 * @throws KeychainAccessException If changing the password failed
	 * @deprecated Please use {@link #changePassphrase(String, String, CharSequence)} instead
	 */
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
	default void changePassphrase(String key, CharSequence passphrase) throws KeychainAccessException {
		changePassphrase(key, null, passphrase);
	}

	/**
	 * Updates a passphrase with a given key and stores a name for that key. Noop, if there is no item for the given key.
	 *
	 * @param key         Unique key previously used while {@link #storePassphrase(String, String, CharSequence)}  storing a passphrase}.
	 * @param displayName The according name to the key. That's the name of the vault displayed in the UI.
	 *                    It's passed to the keychain as an additional information about the vault besides the key.
	 *                    The parameter does not need to be unique or be checked by the keychain.
	 * @param passphrase  The secret to be updated in this keychain.
	 * @throws KeychainAccessException If changing the password failed
	 */
	@Blocking
	void changePassphrase(String key, @Nullable String displayName, CharSequence passphrase) throws KeychainAccessException;

	/**
	 * @return <code>true</code> if this KeychainAccessIntegration works on the current machine.
	 * @implSpec This method must not throw any exceptions and should fail fast
	 * returning <code>false</code> if it can't determine availability of the checked strategy
	 */
	boolean isSupported();

	/**
	 * @return <code>true</code> if the keychain to be accessed is locked. Accesing a locked keychain
	 * requires to unlock the keychain. The keychain backend will show an unlock dialog.
	 * returning <code>false</code> if the keychain to be accessed is unlocked
	 */
	boolean isLocked();

}
