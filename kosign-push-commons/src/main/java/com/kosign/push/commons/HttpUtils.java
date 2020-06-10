package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;
import org.glassfish.jersey.client.ClientProperties;

import javax.net.ssl.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpUtils {

    final static int CONNECT_TIMEOUT = 20000;
    final static int READ_TIMEOUT = 20000;

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    public static String getClientIpAddress(HttpServletRequest request) {
        /*try {
            return get(new URL("https://ipecho.net/plain"));
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }

        return request.getRemoteAddr();*/

        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }
    
    public static boolean isDevTeamIp(String ipAddr) {
    	return StringUtils.startsWith(ipAddr, "192.168.178") && !"192.168.178.148".equals(ipAddr);
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static String post(MultivaluedMap<String, Object> headers, URL url, String body) throws Exception {
        return post(headers, url, body, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String post(MultivaluedMap<String, Object> headers, URL url, String body, int connectTimeout, int readTimeout) throws Exception {
        Client client = getUnsecureClient();

        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        client.property(ClientProperties.READ_TIMEOUT, readTimeout);

        WebTarget webTarget = client.target(url.toString());

        return webTarget
                .request()
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(body), String.class);
    }

    public static String post(URL url, MultivaluedMap<String, String> formData) throws Exception {
        return post(null, url, formData, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String post(URL url, Form formData) throws Exception {
        return post(null, url, formData.asMap(), CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String post(MultivaluedMap<String, Object> headers, URL url, MultivaluedMap<String, String> formData) throws Exception {
        return post(headers, url, formData, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String post(MultivaluedMap<String, Object> headers, URL url, MultivaluedMap<String, String> formData, int connectTimeout, int readTimeout) throws Exception {
        Client client = getUnsecureClient();

        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        client.property(ClientProperties.READ_TIMEOUT, readTimeout);

        WebTarget webTarget = client.target(url.toString());

        Invocation.Builder builder = webTarget.request();

        if(headers != null) {
            builder = builder.headers(headers);
        }

        return builder
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.form(formData), String.class);
    }

    public static String put(MultivaluedMap<String, Object> headers, URL url, String body) throws Exception {
        return put(headers, url, body, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String put(MultivaluedMap<String, Object> headers, URL url, String body, int connectTimeout, int readTimeout) throws Exception {
        Client client = getUnsecureClient();

        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        client.property(ClientProperties.READ_TIMEOUT, readTimeout);

        WebTarget webTarget = client.target(url.toString());

        return webTarget
                .request()
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.json(body), String.class);
    }

    public static String put(URL url, MultivaluedMap<String, String> formData) throws Exception {
        return put(null, url, formData, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String put(MultivaluedMap<String, Object> headers, URL url, MultivaluedMap<String, String> formData) throws Exception {
        return put(headers, url, formData, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String put(MultivaluedMap<String, Object> headers, URL url, MultivaluedMap<String, String> formData, int connectTimeout, int readTimeout) throws Exception {
        Client client = getUnsecureClient();

        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        client.property(ClientProperties.READ_TIMEOUT, readTimeout);

        WebTarget webTarget = client.target(url.toString());

        Invocation.Builder builder = webTarget.request();

        if(headers != null) {
            builder = builder.headers(headers);
        }

        return builder
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.form(formData), String.class);
    }

    public static String get(URL url) throws Exception {
        return get(null, url, null);
    }

    public static String get(MultivaluedMap<String, Object> headers, URL url) throws Exception {
        return get(headers, url, null);
    }

    public static String get(MultivaluedMap<String, Object> headers, URL url, Map<String, Object> params) throws Exception {
        return get(headers, url, params, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String get(MultivaluedMap<String, Object> headers, URL url, Map<String, Object> params, int connectTimeout, int readTimeout) throws Exception {
        Client client = getUnsecureClient();

        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        client.property(ClientProperties.READ_TIMEOUT, readTimeout);

        WebTarget webTarget = client.target(url.toString());

        if(params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
            }
        }

        return webTarget
                .request()
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
    }

    public static boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toUpperCase();
        AppLogManager.debug(userAgent);

        return userAgent.indexOf("MOBILE") > -1;
    }

    public static String sendRequest(HttpServletRequest req, HttpServletResponse res, URL url) throws Exception {
        return sendRequest(req, res, url, null);
    }

    public static String sendRequest(HttpServletRequest req, HttpServletResponse res, URL url, String reqData) throws Exception {

        String data = null;

        if(StringUtils.isNotBlank(reqData)) {
            data = "_JSON_=" + URLEncoder.encode(reqData, "UTF-8");
        }

        // Send data
        StringBuilder sb = new StringBuilder();
        InputStream is;

		CookieManager cookieManager = new CookieManager();  
		CookieHandler.setDefault(cookieManager);

        if(url.getProtocol().equals("https")) {

            HttpsURLConnection.setDefaultHostnameVerifier(new CustomVF());

            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            //conn.setRequestProperty("Cookie", "JSESSIONID=" + URLEncoder.encode(req.getSession().getId(), "UTF-8"));

            try{
                for (Cookie cookie : req.getCookies()) {
                    conn.addRequestProperty("Cookie", cookie.getName()+"="+cookie.getValue());
                }
            }catch(NullPointerException e){
                System.out.println("null_cookie=001");
            }

            conn.connect();

            OutputStreamWriter wr = null;

            if(data != null) {
                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
            }

            try {
                is = conn.getInputStream();
            } catch(FileNotFoundException e){
                e.printStackTrace(System.out);
                is = conn.getErrorStream();
            }

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            if(wr != null) wr.close();
            if(rd != null) rd.close();

        }
        else if(url.getProtocol().equals("http")) {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            

            try{
                for (Cookie cookie : req.getCookies()) {
                    conn.addRequestProperty("Cookie", cookie.getName()+"="+cookie.getValue());
                }
            }catch(NullPointerException e){

            }

            conn.connect();

            OutputStreamWriter wr = null;

            if(data != null) {
                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
            }

            try {
                is = conn.getInputStream();
            } catch(FileNotFoundException e){
                e.printStackTrace(System.out);
                is = conn.getErrorStream();
            }

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            if(wr != null) wr.close();
            if(rd != null) rd.close();
        }

//        return URLDecoder.decode(sb.toString(), "utf-8");
        return sb.toString();
    }

    public static Client getUnsecureClient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[]{new X509TrustManager()
        {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{}
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{}
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }

        }}, new java.security.SecureRandom());


        HostnameVerifier allowAll = new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        return ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier(allowAll).build();
//        return ClientBuilder.newBuilder()
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                }).build();
    }

    public static void main(String[] args) throws Exception {
        AppLogManager.debug(get(new URL("https://ipecho.net/plain")));
    }

}

class CustomVF implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        // TODO Auto-generated method stub
        return true;
    }
}