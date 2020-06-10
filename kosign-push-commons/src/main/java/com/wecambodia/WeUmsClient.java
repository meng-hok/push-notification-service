package com.wecambodia;

import com.kosign.push.beans.SingleTextualMessage;
import com.kosign.push.commons.GenericRestClient;
import com.kosign.push.commons.HttpUtils;
import com.kosign.push.commons.ObjectUtils;
import com.kosign.push.logging.AppLogManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.wecambodia.cmo.SingleTextualMessageIn;
import com.wecambodia.cmo.SingleTextualMessageOut;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.auth.UsernamePasswordCredentials;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URL;

public class WeUmsClient extends GenericRestClient {
	
	static private String API_URL = "http://192.168.178.172:3000";
//	static private String USERNAME = "sarith";
//	static private String PASSWORD = "Qwer123!";
//	static private String API_URL = "https://api.wecambodia.com";
//	static private String USERNAME = "developer123";
//	static private String PASSWORD = "Developer123!@#";
	static private String USERNAME = "kosignkhapi";
	static private String PASSWORD = "1S3@0bc!";

	private static UsernamePasswordCredentials usernamePasswordCredentials;

	static {
		//TODO: read from config resource
//		if(JexSystemConfig.get("weums", "url") != null) API_URL = JexSystemConfig.get("weums", "url");
//		if(JexSystemConfig.get("weums", "username") != null) USERNAME = JexSystemConfig.get("weums", "username");
//		if(JexSystemConfig.get("weums", "password") != null) PASSWORD = JexSystemConfig.get("weums", "password");

		usernamePasswordCredentials = new UsernamePasswordCredentials(
				USERNAME
			, 	PASSWORD
		);
	}

	public static <T> Object sendRequest(T request) throws Exception {
		return sendRequest(usernamePasswordCredentials, request);
	}

	public static <T> Object sendRequest(UsernamePasswordCredentials usernamePasswordCredentials, T request) throws Exception {

		Base64 base64 = new Base64();

		String username = usernamePasswordCredentials.getUserName();
		String password = usernamePasswordCredentials.getPassword();

		String base64Credentials = new String(new Base64().encode((username+":"+password).getBytes()));
		String authorization = "Basic " + base64Credentials;
		AppLogManager.debug(authorization);

		return sendRequest(authorization, request);
	}
	
	public static <T> Object sendRequest(String authorization, T request) throws Exception {
		
		StringBuilder apiUrl = new StringBuilder(API_URL);

		MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
		headerMap.add("Authorization", authorization);
		headerMap.add("Content-Type",  "application/json");
//		headerMap.put("Content-Type",  "application/x-www-form-urlencoded;charset=UTF-8");
		headerMap.add("Accept", "application/json");

		String reqData = ObjectUtils.writeValueAsString(request);
		AppLogManager.debug(reqData);

		String respData = null;

		if(request instanceof SingleTextualMessageIn) {
			apiUrl.append("/weums/v1/sms/single");

			respData = HttpUtils.post(headerMap, new URL(apiUrl.toString()), reqData);
		}

		AppLogManager.info(WeUmsClient.class, "[Url] [" + apiUrl + "]");
		AppLogManager.info(WeUmsClient.class, "[Request] [" + reqData + "]");

//		String responseData = HttpUtils.getResponse(new URL(apiUrl.toString()), Method.GET, headerMap, gson.toJson(request));
		
		if(respData != null) {
			AppLogManager.info(WeUmsClient.class, "[Response] [" + respData + "]");
			return parseData(respData, request);
		}
		
		return null;
	}
	
	public static <T> Object parseData(String responseData, T request) throws Exception {
		if(request instanceof SingleTextualMessageIn) {
			return ObjectUtils.readValue(responseData, new TypeReference<SingleTextualMessageOut>(){} );
		}

		return null;
	}

	/*
	public static void main(String[] args) throws Exception {
		SingleTextualMessageIn apiIn = new SingleTextualMessageIn();
		apiIn.setFrom("SMS Info");
		apiIn.setTo("85592333208");
		apiIn.setText("hi i'm vansak");
		apiIn.setAgent(true);

		WeUmsClient.sendRequest(apiIn);
	}
	*/

	public static boolean send(SingleTextualMessage message) throws Exception {
		SingleTextualMessageIn apiIn = new SingleTextualMessageIn();
		apiIn.setFrom(message.getFrom());
		apiIn.setTo(message.getRecipient());
		apiIn.setText(message.getText());

		SingleTextualMessageOut apiOut = (SingleTextualMessageOut)WeUmsClient.sendRequest(apiIn);

		return apiOut != null && apiOut.isSuccess() && apiOut.getMessages() != null && apiOut.getMessages().get(0).getStatus().getId() == 0;
	}
}