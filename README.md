Minimalistic implementation of auth repository for Tigase XMPP server for use with Unreal Tournament 4 MCP implementation.

The implementation verifies authentication tokens sent by the MCP and allows users to access the XMPP server

To configure set cls and verifyUrl for the auth repository inside config.tdsl
```
authRepository {
    'default' () {
        cls = 'cz.klukule.tigase.McpAuthRepository'
        verifyUrl = 'https://master-ut4.timiimit.com/account/api/oauth/verify'
    }
}
```

To build simply execute `mvn package`