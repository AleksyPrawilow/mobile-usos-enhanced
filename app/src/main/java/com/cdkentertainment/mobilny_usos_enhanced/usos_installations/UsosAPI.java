package com.cdkentertainment.mobilny_usos_enhanced.usos_installations;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class UsosAPI extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://usosapps.amu.edu.pl/services/oauth/authorize?oauth_token=%s";

    private static class InstanceHolder {
        private static final UsosAPI INSTANCE = new UsosAPI();
    }

    public static UsosAPI instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint(){
        return "https://usosapps.amu.edu.pl/services/oauth/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://usosapps.amu.edu.pl/services/oauth/request_token?scopes=grades|crstests|email|cards|personal|offline_access|studies|photo|payments|other_emails|mobile_numbers";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}