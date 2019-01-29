package com.example.nicolaebogdan.smartcity;

import android.content.SharedPreferences;

import com.example.nicolaebogdan.smartcity.common.SmartCityPreferences;

import static com.example.nicolaebogdan.smartcity.common.SharedPreferencesUtil.*;


class SmartCityPrefImpl implements SmartCityPreferences {

	private static final String TOKEN = "TOKEN";
	private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	private static final String FACEBOOK_TOKEN = "FACEBOOK_TOKEN";
	private static final String USER_NAME = "USER_NAME";
	private static final String USER_EMAIL = "USER_EMAIL";
	private static final String CRM_MEMBER_ID = "CRM_MEMBER_ID";
	private static final String USER_FIRST_NAME = "USER_FIRST_NAME";
	private static final String USER_LAST_NAME = "USER_LAST_NAME";
	private static final String USER_EMAIL_FROM_API = "USER_EMAIL_FROM_API";
	private static final String CREDIT_CARDS = "CREDIT_CARDS";

	private final SharedPreferences sharedPreferences;

	public SmartCityPrefImpl(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}

	@Override
	public String getLatestToken() {
		return getString(sharedPreferences, TOKEN);
	}

	@Override
	public String getRefreshToken() {
		return getString(sharedPreferences, REFRESH_TOKEN);
	}

	@Override
	public String getFacebookToken() {
		return getString(sharedPreferences,FACEBOOK_TOKEN);
	}

	@Override
	public boolean hasToken() {
		return sharedPreferences.contains(TOKEN);
	}

	@Override
	public int getCRMMemberId() {
		return getInt(sharedPreferences,CRM_MEMBER_ID);
	}

	@Override
	public void setCRMMemberId(int crmMemberId) {
		 putValueInSharedPref(sharedPreferences,CRM_MEMBER_ID,crmMemberId);
	}

	@Override
	public void clearCRMMemberId() {
		removeKeys(sharedPreferences,CRM_MEMBER_ID);
	}

	@Override
	public String getUserFirstName() {
		return getString(sharedPreferences,USER_FIRST_NAME);
	}

	@Override
	public String getUserLastName() {
		return getString(sharedPreferences,USER_LAST_NAME);
	}

	@Override
	public String getUserEmail() {
		return getString(sharedPreferences,USER_EMAIL_FROM_API);
	}

	@Override
	public void setUserFirstName(String firstName) {
		putValueInSharedPref(sharedPreferences,USER_FIRST_NAME,firstName);
	}

	@Override
	public void setUserLastName(String lastName) {
		putValueInSharedPref(sharedPreferences,USER_LAST_NAME,lastName);
	}

	@Override
	public void setUserEmail(String userEmail) {
		putValueInSharedPref(sharedPreferences,USER_EMAIL_FROM_API,userEmail);
	}


	@Override
	public void clearUserInfo() {
		removeKeys(sharedPreferences,USER_FIRST_NAME);
		removeKeys(sharedPreferences,USER_LAST_NAME);
		removeKeys(sharedPreferences,USER_EMAIL_FROM_API);
	}

	@Override
	public void setListOfCards(String creditCardList) {
		putValueInSharedPref(sharedPreferences, CREDIT_CARDS + getCRMMemberId(), creditCardList);
	}

	@Override
	public String getListOfCards() {
		return getString(sharedPreferences, CREDIT_CARDS + getCRMMemberId());
	}

	@Override
	public void clearLIstOfCards() {
		removeKeys(sharedPreferences,CREDIT_CARDS);
	}

	@Override
	public void setLatestToken(String latestToken) {
		putValueInSharedPref(sharedPreferences, TOKEN, latestToken);
	}

	@Override
	public void setRefreshToken(String refreshToken) {
		putValueInSharedPref(sharedPreferences, REFRESH_TOKEN, refreshToken);
	}

	@Override
	public void setFacebookToken(String facebookToken) {
		putValueInSharedPref(sharedPreferences, FACEBOOK_TOKEN , facebookToken);
	}

	@Override
	public void clearToken() {
		removeKeys(sharedPreferences, TOKEN);
	}

	@Override
	public void clearRefreshToken() {
		removeKeys(sharedPreferences, REFRESH_TOKEN);
	}

	@Override
	public void clearFacebookToken() {
		removeKeys(sharedPreferences,FACEBOOK_TOKEN);
	}

	@Override
	public void setCurrentUser(String accessToken, String name, String email) {
		putValueInSharedPref(sharedPreferences, TOKEN, accessToken);
		putValueInSharedPref(sharedPreferences, USER_NAME, name);
		putValueInSharedPref(sharedPreferences, USER_EMAIL, email);
	}
}
