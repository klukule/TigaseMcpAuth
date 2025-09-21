package cz.klukule.tigase;

import tigase.auth.credentials.Credentials;
import tigase.db.*;
import tigase.kernel.beans.config.ConfigField;
import tigase.xmpp.jid.BareJID;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class McpAuthRepository implements AuthRepository {
  private static final Logger log = Logger.getLogger(McpAuthRepository.class.getName());

  private final TokenVerifier tokenVerifier = new TokenVerifier();
  private boolean initialized = false;

  @ConfigField(desc = "Auth token verify URL", alias = "verifyUrl")
  private String verifyUrl;
  @ConfigField(desc = "Allow HTTP (non-HTTPS) verify URL", alias = "allowHttp")
  private boolean allowHttp = false;
  @ConfigField(desc = "HTTP timeout in milliseconds", alias = "httpTimeoutMs")
  private int httpTimeoutMs = 2000;
  @ConfigField(desc = "HTTP User-Agent header", alias = "httpUserAgent")
  private String httpUserAgent = "tigase-mcp-verifier/1.0";

  public void initRepository() {
    if (initialized)
      return;
    tokenVerifier.setVerifyUrl(verifyUrl);
    tokenVerifier.setAllowHttp(allowHttp);
    tokenVerifier.setHttpTimeoutMs(httpTimeoutMs);
    tokenVerifier.setHttpUserAgent(httpUserAgent);
    tokenVerifier.init();

    if (log.isLoggable(Level.CONFIG)) {
      log.config("McpAuthRepository initialized");
      log.config(" verifyUrl=" + verifyUrl);
      log.config(" allowHttp=" + allowHttp);
      log.config(" httpTimeoutMs=" + httpTimeoutMs);
      log.config(" httpUserAgent=" + httpUserAgent);
    }
    initialized = true;
  }

  @Override
  public Credentials getCredentials(BareJID user, String credentialId) throws TigaseDBException {
    // Official initRepository function got deprecated in 8.0 just call it here manually, not ideal but works.
    initRepository();

    return new SingleCredential(user, getAccountStatus(user), new McpPasswordEntry(user, tokenVerifier));
  }

  @Override
  public AccountStatus getAccountStatus(BareJID user) throws TigaseDBException {
    return AccountStatus.active;
  }

  @Override
  public void loggedIn(BareJID user) throws TigaseDBException {
  }

  @Override
  public void logout(BareJID user) throws TigaseDBException {
  }

  @Override
  public void addUser(BareJID user, String password) throws TigaseDBException {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public String getPassword(BareJID user) throws TigaseDBException {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public String getResourceUri() {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public long getActiveUsersCountIn(Duration duration) {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public long getUsersCount() {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public long getUsersCount(String domain) {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public boolean otherAuth(Map<String, Object> authProps) throws TigaseDBException, AuthorizationException {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public void queryAuth(Map<String, Object> authProps) {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public void removeUser(BareJID user) throws TigaseDBException {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public void setAccountStatus(BareJID user, AccountStatus status) throws TigaseDBException {
    throw new UnsupportedOperationException("Not available");
  }

  @Override
  public void updatePassword(BareJID user, String password) throws TigaseDBException {
    throw new UnsupportedOperationException("Not available");
  }
}