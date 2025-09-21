package cz.klukule.tigase;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;

import org.json.JSONObject;

final class TokenVerifier {
    private URI verifyUri;
    private boolean allowHttp;
    private int httpTimeoutMs;
    private String httpUserAgent;

    private HttpClient client;

    void init() {
        if (verifyUri == null)
            throw new IllegalStateException("verifyUrl not set");
        if (!allowHttp && !"https".equalsIgnoreCase(verifyUri.getScheme()))
            throw new IllegalStateException("verifyUrl must be HTTPS (set allowHttp=true to override)");

        client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(httpTimeoutMs)).build();
    }

    boolean verify(String accountId, String bearerToken) {
        try {
            HttpRequest req = HttpRequest.newBuilder(verifyUri)
                    .timeout(Duration.ofMillis(httpTimeoutMs))
                    .header("Authorization", "Bearer " + bearerToken)
                    .header("User-Agent", httpUserAgent)
                    .GET()
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200)
                return false;

            final String body = resp.body();
            if (body == null || body.isBlank())
                return false;

            JSONObject json = new JSONObject(body);

            String jsonAccountId = json.optString("account_id", "").trim();
            if (jsonAccountId.isEmpty())
                return false;

            return jsonAccountId.equals(accountId);
        } catch (Exception e) {
            return false;
        }
    }

    public void setVerifyUrl(String url) {
        this.verifyUri = URI.create(url);
    }

    public void setAllowHttp(boolean allow) {
        this.allowHttp = allow;
    }

    public void setHttpTimeoutMs(int ms) {
        this.httpTimeoutMs = ms;
    }

    public void setHttpUserAgent(String ua) {
        if (ua != null && !ua.isBlank())
            this.httpUserAgent = ua;
    }
}
