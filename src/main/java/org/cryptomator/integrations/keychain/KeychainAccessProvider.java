package org.cryptomator.integrations.keychain;

/**
 * This is the interface used by Cryptomator to store passwords securely in external keychains, such as system keychains or password managers.
 */
public interface KeychainAccessProvider {

	/**
	 * Associates a passphrase with a given key.
	 *
	 * @param key        Key used to retrieve the passphrase via {@link #loadPassphrase(String)}.
	 * @param passphrase The secret to store in this keychain.
	 * @throws KeychainAccessException If storing the password failed
	 */
	void storePassphrase(String key, CharSequence passphrase) throws KeychainAccessException;

	/**
	 * @param key Unique key previously used while {@link #storePassphrase(String, CharSequence) storing a passphrase}.
	 * @return The stored passphrase for the given key or <code>null</code> if no value for the given key could be found.
	 * @throws KeychainAccessException If loading the password failed
	 */
	char[] loadPassphrase(String key) throws KeychainAccessException;

	/**
	 * Deletes a passphrase with a given key.
	 *
	 * @param key Unique key previously used while {@link #storePassphrase(String, CharSequence) storing a passphrase}.
	 * @throws KeychainAccessException If deleting the password failed
	 */
	void deletePassphrase(String key) throws KeychainAccessException;

	/**
	 * Updates a passphrase with a given key. Noop, if there is no item for the given key.
	 *
	 * @param key        Unique key previously used while {@link #storePassphrase(String, CharSequence) storing a passphrase}.
	 * @param passphrase The secret to be updated in this keychain.
	 * @throws KeychainAccessException If changing the password failed
	 */
	void changePassphrase(String key, CharSequence passphrase) throws KeychainAccessException;

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

	/**
	 * @return <code>true</code> if this KeychainAccessIntegration needs to be associated with the KeychainBackend. If a backend requires a handshaking before it can be accessed,
	 * e.g. the KeychainAccessIntegration and the backend exchange cryptographic keys to establish an encrypted connection, this method checks whether the KeychainAccessIntegration
	 * already has been associated to the backend or not.
	 * @implSpec This method must not throw any exceptions and should fail fast
	 * returning <code>false</code> if the KeychainAccessIntegration was associated with the KeychainBackend and the KeychainAccessIntegration can use the KeychainBackend.
	 */
	default boolean needsAssociation() { return false; }

	/**
	 * @return <code>true</code> if associating the KeychainAccessIntegration with the KeychainBackend was successfull. If a backend requires a handshaking before it can be accessed,
	 * e.g. the KeychainAccessIntegration and the backend exchange cryptographic keys to establish an encrypted connection, this method associates the KeychainAccessIntegration with the
	 * backend. The keychain backend will show an association dialog.
	 * @implSpec This method must not throw any exceptions and should fail fast
	 * returning <code>false</code> if association failed
	 */
	default boolean associate() { return true; }

}
