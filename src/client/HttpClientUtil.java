package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created on 2017/5/21.
 *
 * @author stormma
 */
public class HttpClientUtil {
    static Log log = LogFactory.getLog(HttpClientUtil.class);

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String doGet(String url, String param) {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url+"?"+param);
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static JSONObject doGet(String url) {
        JSONObject resObj = null;
        GetMethod method = new GetMethod(url);
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
        try {
            // Execute the method.
            client.executeMethod(method);
            String res = method.getResponseBodyAsString();
            resObj = JSON.parseObject(res);

        } catch (HttpException e) {
            log.error("Fatal protocol violation: " + e.getMessage());
            StackTraceElement[] el = e.getStackTrace();
            log.error(el.length);
            for (int i = 0; i < el.length; i++)
                log.error(el[i].toString());

        } catch (IOException e) {
            log.error("Fatal transport error: " + e.getMessage());
            StackTraceElement[] el = e.getStackTrace();
            log.error(el.length);
            for (int i = 0; i < el.length; i++)
                log.error(el[i].toString());
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return resObj;
    }


    public static String doPost1(String url, Map map){
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
        String result = null;
        try {
            PostMethod postMethod  = new PostMethod(url);
            postMethod.setRequestHeader("Content-type","application/x-www-form-urlencoded; charset=UTF-8");
            //postMethod.setRequestHeader("charset","UTF-8");
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                String value = (String) map.get(key);
                postMethod.addParameter(key, value);
            }
            client.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return result;




       // 执行POST方法
    }
    /**
     * https post request
     *
     * @param url
     * @param map
     * @param charset
     * @return
     */
    public static String doPost(String url, Map<String, String> map, String charset) {
        HttpClient httpClient;
        HttpPost httpPost;
        String result = null;
        try {
            httpClient = new SSLClient();

            httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("charset","UTF-8");
            //设置参数
            List<NameValuePair> list = new ArrayList<>();
            //参数key值迭代器
            if (!CollectionUtils.isEmpty(map)) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
                if (list.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                    httpPost.setEntity(entity);
                }
            }

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return result;
    }

    public static JSONObject doPostHTTPSJsonParam(String url, String json) {
        String returnValue = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json, "UTF-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，必须用此方法

        } catch (Exception e) {
            log.error(e);

        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }
        //第五步：处理返回值
        return (JSONObject) JSON.parse(returnValue);
    }

    public static JSONObject doJsonPost(String url, JSONObject postData) {
        JSONObject result = null;
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
        PostMethod method = new PostMethod(url);
        try {
            // Execute the method.
            method.setRequestBody(postData.toString());
            method.getParams().setContentCharset("utf-8");
            client.executeMethod(method);
            String res = method.getResponseBodyAsString();
            result = JSONObject.parseObject(res);
            log.info("post url:" + url);
            log.info("post param:" + postData);
            log.info("post result:" + result);

        } catch (HttpException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            log.info(e.getMessage());
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        if (result == null)
            result = new JSONObject();
        return result;
    }


    /**
     * fashttps xml参数的请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doPostHttpsXMLParam(String url, String param) {
        HttpClient httpClient;
        HttpPost httpPost;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            if (isNotBlank(param)) {
                //设置参数
                StringEntity entity = new StringEntity(
                        param, "text/xml", "UTF-8");
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return result;
    }

    public static String asUrlParams(Map<String, String> source){
        Iterator<String> it = source.keySet().iterator();
        StringBuilder paramStr = new StringBuilder();
        while (it.hasNext()){
            String key = it.next();
            if (isBlank(source.get(key))){
                continue;
            }
            paramStr.append("&").append(key).append("=").append(source.get(key));
        }
        // 去掉第一个&
        return paramStr.substring(1);
    }

    public static boolean isBlank(String s) {
        if (s == null)
            return true;
        if (s.length() == 0)
            return true;

        return s.matches("(\\s)*");
    }

    public static boolean isNotBlank(String s) {
        return  !isBlank(s);
    }


    public static boolean isNotFalse(String value) {
        if (value == null)
            return true;
        value = value.trim();
        return !(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0")
                || value.equalsIgnoreCase("no"));
    }

    public static boolean isValid(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null || strings[i].trim().length() == 0)
                return false;
        }
        return true;
    }

    public static boolean isValid(String s1) {
        return (s1 != null && s1.trim().length() > 0);
    }

    public static boolean isValid(String s1, String s2) {
        return (isValid(s1) && isValid(s2));
    }

    public static boolean isValid(String s1, String s2, String s3) {
        return (isValid(s1) && isValid(s2) && isValid(s3));
    }

}
