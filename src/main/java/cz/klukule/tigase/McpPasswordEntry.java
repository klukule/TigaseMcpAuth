package cz.klukule.tigase;

import tigase.auth.credentials.Credentials;
import tigase.xmpp.jid.BareJID;

public final class McpPasswordEntry implements Credentials.Entry {
  private final TokenVerifier verifier;
  private final BareJID account;

  public McpPasswordEntry(BareJID account, TokenVerifier verifier) {
    this.verifier = verifier;
    this.account = account;
  }

  @Override
  public String getMechanism() {
    return "PLAIN";
  }

  @Override
  public boolean verifyPlainPassword(String token) {
    if (token.isEmpty())
      return false;

    return verifier.verify(account.getLocalpart(), token);
  }
}