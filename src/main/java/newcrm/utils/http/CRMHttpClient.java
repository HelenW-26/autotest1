package newcrm.utils.http;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import newcrm.listeners.AllureHttpFilter;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 实现httpclient类，可以发送post，get均带有body。
 * 为各种API测试以及访问线上数据库的基础类
 * @author FengLiu
 *
 */
public class CRMHttpClient {
	
	//继承HttpPost，修改方法为get，这样可以发送带有body的get请求
	protected class GetWithEntity extends HttpPost {

		public final static String METHOD_NAME = "GET";
		public GetWithEntity(URI build) {
			// TODO Auto-generated constructor stub
			super(build);
		}
		@Override
		public String getMethod() {
			// TODO Auto-generated method stub
			return METHOD_NAME;
		}
		
	}
	
	private  HttpClient httpClient;    
    private  CookieStore httpCookieStore;
    public CRMHttpClient() {
    	this.httpClient = getClient();
	}


	public HttpResponse getGetResponse(String url,Map<String,String> header) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);  
    	HttpGet httpGet = new HttpGet(uriBuilder.build()); 
		if(header !=null) {
			for(Entry<String,String> e: header.entrySet()) {
    			httpGet.addHeader(e.getKey(), e.getValue());
    		}
		}
    	HttpResponse response = httpClient.execute(httpGet);
    	
    	return response;
	}
	
	@SuppressWarnings("unchecked")
	public HttpResponse getGetResponse(String url,Map<String,String> header,Object body) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);  
    	GetWithEntity httpGet = new GetWithEntity(uriBuilder.build()); 
		if(header !=null) {
            for(Entry<String,String> e: header.entrySet()) {
                httpGet.addHeader(e.getKey(), e.getValue());
            }
		}
		
		if(body.getClass()==HashMap.class) {
        	List<NameValuePair> nvps = new ArrayList<>();
            for(Entry<String,String> entry: ((HashMap<String,String>) body).entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
        	UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
        	formEntity.setContentEncoding("UTF-8");
        	httpGet.setEntity(formEntity);
        }else {
        	String charset = "UTF-8";
            StringEntity se = new StringEntity(body.toString());
            se.setContentEncoding(charset);
            String contentType = header.get("Content-Type");
            se.setContentType(contentType);
            httpGet.setEntity(se);
        }
		
    	HttpResponse response = httpClient.execute(httpGet);
    	
    	return response;
	}
	public HttpResponse getPostResponse(String url, Map<String,String> map, Object body, int timeoutMs) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);
		HttpPost httpPost = new HttpPost(uriBuilder.build());

		// 设置请求超时配置
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(timeoutMs)
				.setSocketTimeout(timeoutMs)
				.setConnectionRequestTimeout(timeoutMs)
				.build();
		httpPost.setConfig(requestConfig);

		//Set request header
		for(Entry<String, String> entry: map.entrySet()) {
			httpPost.addHeader(entry.getKey(),entry.getValue());
		}
		if(body.getClass()==HashMap.class) {
			List<NameValuePair> nvps = new ArrayList<>();
			for(Entry<String,String> entry: ((HashMap<String,String>) body).entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
			formEntity.setContentEncoding("UTF-8");
			httpPost.setEntity(formEntity);
		}else {
			String charset = "UTF-8";
			StringEntity se = new StringEntity(body.toString());
			se.setContentEncoding(charset);
			String contentType = map.get("Content-Type");
			se.setContentType(contentType);
			httpPost.setEntity(se);
		}

		HttpResponse response = httpClient.execute(httpPost);

		return response;
	}


	@SuppressWarnings("unchecked")
	public HttpResponse getPostResponse(String url, Map<String,String> map, Object body) throws ParseException, IOException, Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpPost httpPost = new HttpPost(uriBuilder.build());

        for(Entry<String, String> entry: map.entrySet()) {
            httpPost.addHeader(entry.getKey(),entry.getValue());
        }

        if(body.getClass()==HashMap.class) {
        	List<NameValuePair> nvps = new ArrayList<>();
        	for(Entry<String,String> entry: ((HashMap<String,String>) body).entrySet()) {
        		nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        	}
        	UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
        	formEntity.setContentEncoding("UTF-8");
        	httpPost.setEntity(formEntity);
        }else {
        	String charset = "UTF-8";
            StringEntity se = new StringEntity(body.toString());
            se.setContentEncoding(charset);
            String contentType = map.get("Content-Type");
            se.setContentType(contentType);
            httpPost.setEntity(se);
        }

        HttpResponse response = httpClient.execute(httpPost);

        return response;
    }
	@SuppressWarnings("unchecked")
	public HttpResponse uploadFilePostResponse(String url,Map<String,String> map, File file) throws ParseException, IOException, Exception {
		URIBuilder uriBuilder = new URIBuilder(url);
		HttpPost httpPost = new HttpPost(uriBuilder.build());

		//Set request header
		for(Entry<String, String> entry: map.entrySet()) {
			httpPost.addHeader(entry.getKey(),entry.getValue());
		}
		// Build multipart entity
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("upload_file", file, ContentType.create("text/csv"),file.getName());

		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);

		Header contentTypeHeader = entity.getContentType();
		if (entity.getContentType() != null) {
			httpPost.setHeader(contentTypeHeader);
		}
		return httpClient.execute(httpPost);
	}
	
	public void setCookie(Cookie cookie) {
		this.httpCookieStore.addCookie(cookie);
		this.httpClient = getClient();
	}

	/**
	 * @return
	 */
	private HttpClient getClient() {
		SocketConfig socketConfig = SocketConfig.custom()  
                .setSoTimeout(80000).setSoKeepAlive(true)  
                .setTcpNoDelay(true).build(); 
		
		httpCookieStore = new BasicCookieStore();
		/*
		httpClient = HttpClients.custom().setDefaultCookieStore(httpCookieStore)
				.setUserAgent("Mozilla/5.0 CRM-Automation")
				.build();*/
		RequestConfig.custom()  
                .setSocketTimeout(80000)  
                .setConnectTimeout(80000).build();  
 
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();  
        connManager.setMaxTotal(50);  
        connManager.setDefaultMaxPerRoute(50);  
        connManager.setDefaultSocketConfig(socketConfig);

		return HttpClients.custom().
				setConnectionManager(connManager)
				.setDefaultCookieStore(httpCookieStore)
				.addInterceptorFirst(new AllureHttpFilter.RequestInterceptor())
				.addInterceptorLast(new AllureHttpFilter.ResponseInterceptor())
				.build();
	}
	
	
    /* Added by Alex L
     * This rawPostAPI simply return all API return in HttpResponse format. */
    public HttpResponse rawPostAPI(String url, Map map, String body) throws ParseException, IOException, Exception {
    	HttpEntity entity = null;
   
        URIBuilder uriBuilder = new URIBuilder(url); 
        HttpPost httpPost = new HttpPost(uriBuilder.build());

        //Set request header
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            httpPost.addHeader((String) key, (String) val); 
        }

        //Config request body
        String charset = "UTF-8";
        StringEntity se = new StringEntity(body);
        se.setContentEncoding(charset);
        //System.out.println("body:"+body);
        		
        //Config content type
        String contentType = (String) map.get("Content-Type");
        se.setContentType(contentType);
        httpPost.setEntity(se);
        
        //httpPost.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpPost);   
        //System.out.println("Executing request " + httpPost.getRequestLine());   

		return response;
    }

	public HttpResponse getPatchResponse(String url, Map<String, String> header, Object body) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);
		HttpPatch httpPatch = new HttpPatch(uriBuilder.build());

		if (header != null) {
			for (Entry<String, String> entry : header.entrySet()) {
				httpPatch.addHeader(entry.getKey(), entry.getValue());
			}
		}

		if (body.getClass() == HashMap.class) {
			List<NameValuePair> nvps = new ArrayList<>();
			for (Entry<String, String> entry : ((HashMap<String, String>) body).entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
			httpPatch.setEntity(formEntity);
		} else {
			StringEntity entity = new StringEntity(body.toString(), "UTF-8");
			entity.setContentEncoding("UTF-8");

			// Set the content type from the header, default to application/json if not present
			String contentType = header != null && header.containsKey("Content-Type") ? header.get("Content-Type") : "application/json";
			entity.setContentType(contentType);

			httpPatch.setEntity(entity);
		}

		return httpClient.execute(httpPatch);
	}

	public String buildUrlQueryString(Map<String, String> params) {
		return "?" + params.entrySet().stream()
				.map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
				.collect(Collectors.joining("&"));
	}

	public static void main(String args[]) throws ParseException, IOException, Exception {
		CRMHttpClient client = new CRMHttpClient();
		HashMap<String,String> header = new HashMap<>();
		String user = "feng.liu@vantagemarkets.com";
		String password = "68-Dialogue-weighs-altered";
		String url = "https://archery.crm-prod.com/";
		header.put("accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Cookie","csrftoken=H2WSUDjh3hh6I1uSOqqC1Im0bNn5rHebbIchoVena6U7IJfjDWgsmLwV4c1AAV8y;sessionid=r6oyp7cz3cdo71aa12m4l62gujpa6hf");
		header.put("x-csrftoken", "H2WSUDjh3hh6I1uSOqqC1Im0bNn5rHebbIchoVena6U7IJfjDWgsmLwV4c1AAV8y");
		String sql_content = "select * from tb_user limit 3;";
		HashMap<String,String> body = new HashMap<>();
		
		body.put("instance_name", "kcm-prod-db01");
		body.put("db_name", "kcm_svg_business");
		body.put("schema_name", "");
		body.put("tb_name", "");
		body.put("sql_content", sql_content);
		body.put("limit_num", "100");
		
		String fullpath = url+"/query/";
		HttpResponse response = client.getPostResponse(fullpath, header, body);
        System.out.println(EntityUtils.toString(response.getEntity(),"utf-8"));
	}
}
