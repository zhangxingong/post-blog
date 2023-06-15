package client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by xgzhang on 2023/6/14.
 */
public class RestApi {
    private static final Log log = LogFactory.getLog(RestApi.class);

    /**
     * 原生字符串发送get请求
     *
     * @param url
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String doGet(String url, String token) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("DataEncoding", "UTF-8");
        httpGet.setHeader("Authorization", token);
        httpGet.setHeader("Accept", "application/vnd.github+json");
        httpGet.setHeader("X-GitHub-Api-Version", "2022-11-28");
//        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
//        httpGet.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            //log.info("请求成功");
            log.error("GET="+result);
            return result;
        } catch (ClientProtocolException e) {
             log.error(e);
        } catch (IOException e) {
             log.error(e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                     log.error(e);
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                     log.error(e);
                }
            }
        }
        return null;
    }

    /**
     * 原生字符串发送post请求
     *
     * @param url
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static String doPost(String url, String token, String jsonStr) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
//        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
//        httpPost.setConfig(requestConfig);
//        httpPost.setHeader("Content-type", "application/json");
//        httpPost.setHeader("DataEncoding", "UTF-8");
        httpPost.setHeader("Accept", "application/vnd.github+json");
        httpPost.setHeader("X-GitHub-Api-Version", "2022-11-28");
        httpPost.setHeader("Authorization", token);

        CloseableHttpResponse httpResponse = null;
        try {
            httpPost.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            log.error("POST="+result);
            return result;
        } catch (ClientProtocolException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
        return null;
    }

    /**
     * 原生字符串发送put请求
     *
     * @param url
     * @param token
     * @param jsonStr
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPut(String url, String token, String jsonStr) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
//        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
//        httpPut.setConfig(requestConfig);
//        httpPut.setHeader("Content-type", "application/json");
//        httpPut.setHeader("DataEncoding", "UTF-8");
        httpPut.setHeader("Accept", "application/vnd.github+json");
        httpPut.setHeader("X-GitHub-Api-Version", "2022-11-28");
        httpPut.setHeader("Authorization", token);

        CloseableHttpResponse httpResponse = null;
        try {
            httpPut.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPut);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            log.error("PUT="+result);
            return result;
        } catch (ClientProtocolException e) {
             log.error(e);
        } catch (IOException e) {
             log.error(e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                     log.error(e);
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                     log.error(e);
                }
            }
        }
        return null;
    }

    /**
     * 发送delete请求
     *
     * @param url
     * @param token
     * @param jsonStr
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doDelete(String url, String token, String jsonStr) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
//        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
//        httpDelete.setConfig(requestConfig);
//        httpDelete.setHeader("Content-type", "application/json");
//        httpDelete.setHeader("DataEncoding", "UTF-8");
        httpDelete.setHeader("Accept", "application/vnd.github+json");
        httpDelete.setHeader("X-GitHub-Api-Version", "2022-11-28");
        httpDelete.setHeader("Authorization", token);

        CloseableHttpResponse httpResponse = null;
        try {
            httpDelete.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpDelete);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            log.error("DELETE="+result);
            return result;
        } catch (ClientProtocolException e) {
             log.error(e);
        } catch (IOException e) {
             log.error(e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.info("e = " + e);
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                     log.error(e);
                }
            }
        }
        return null;
    }
}