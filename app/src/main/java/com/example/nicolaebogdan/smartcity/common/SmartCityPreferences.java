package com.example.nicolaebogdan.smartcity.common;

public interface SmartCityPreferences {

	String getLatestToken();
	String getRefreshToken();
	String getFacebookToken();
	void setLatestToken(String latestToken);
	void setRefreshToken(String refreshToken);
	void setFacebookToken(String facebookToken);
	void clearToken();
	void clearRefreshToken();
	void clearFacebookToken();
	boolean hasToken();
	int getCRMMemberId();
	void setCRMMemberId(int crmMemberId);
	void clearCRMMemberId();
	String getUserFirstName();
	String getUserLastName();
	String getUserEmail();
	void setUserFirstName(String firstName);
	void setUserLastName(String lastName);
	void setUserEmail(String userEmail);
	void clearUserInfo();

	void setListOfCards(String creditCardList);
	String getListOfCards();
	void clearLIstOfCards();

	void setCurrentUser(String accessToken, String name, String email);
}
