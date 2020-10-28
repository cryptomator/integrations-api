# Cryptomator Integrations API

This library defines the API of various integrations used by Cryptomator. The implementations  are then loaded during runtime via the ServiceLoader API.

To add an integration to Cryptomator, simply create a library, implement the provider interface you're interested in, add your implementation's fully qualified class name to the provider configuration file and publish your library as a jar.

For example let's say you want to add a new keychain integration. You just need these three steps:
1. Create a class, e.g. `com.example.mycryptomatorplugin.PwManager3000Integration` which extends `org.cryptomator.integrations.keychain.KeychainAccessProvider` and implement the methods according to the interface.
1. Create a provider configuration file at `META-INF/services/org.cryptomator.integrations.keychain.KeychainAccessProvider` and add your implementation (`com.example.mycryptomatorplugin.PwManager3000Integration`)
1. Publish your library as a jar file and include it to Cryptomator's class path at runtime (PRs are welcome)
