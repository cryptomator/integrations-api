package org.cryptomator.integrations.keychain;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class KeychainAccessProviderTest {

	@Test
	public void testStorePassphrase() throws KeychainAccessException {
		var provider = Mockito.mock(KeychainAccessProvider.class);
		Mockito.doCallRealMethod().when(provider).storePassphrase(Mockito.anyString(), Mockito.anyString());

		provider.storePassphrase("key", "pass");

		Mockito.verify(provider).storePassphrase("key", null, "pass");
	}

	@Test
	public void testChangePassphrase() throws KeychainAccessException {
		var provider = Mockito.mock(KeychainAccessProvider.class);
		Mockito.doCallRealMethod().when(provider).changePassphrase(Mockito.anyString(), Mockito.anyString());

		provider.changePassphrase("key", "pass");

		Mockito.verify(provider).changePassphrase("key", null, "pass");
	}

}