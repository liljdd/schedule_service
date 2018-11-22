package cn.dianzhi.task.business.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http工具类.
 * 
 * @author lee
 * @since 2016年5月20日
 */
public final class HttpUtils {

  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HttpUtils.class);

  /**
   * POST请求.
   * 
   * @param uri
   *          请求地址
   * @param data
   *          请求数据
   * @param timeout
   *          超时时间(默认5秒)
   * @param retryCount
   *          错误时重试次数(默认3次)
   * @return 请求结果
   * @throws IOException IOException
   * @throws ClientProtocolException ClientProtocolException
   */
  public static String post(final String uri, Map<String, String> data, int timeout, int retryCount)
      throws ClientProtocolException, IOException {
    CloseableHttpClient client = null;
    HttpPost post = new HttpPost(uri);
    if (data != null) {
      post.setEntity(getFormData(data));
    }

    StandardHttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler(retryCount, false);
    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectionRequestTimeout(timeout).build();

    client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setRetryHandler(retryHandler).build();
    
    HttpResponse response = client.execute(post);
    int statusCode = response.getStatusLine().getStatusCode();
    logger.debug("statusCode:{}", statusCode);
    String result = EntityUtils.toString(response.getEntity());
    logger.debug("result:{}", result);
    return result;
  }

  /**
   * POST请求.
   * 
   * @param uri
   *          请求地址
   * @param data
   *          请求数据
   * @throws IOException IOException
   * @throws ClientProtocolException ClientProtocolException
   */
  public static String post(final String uri, Map<String, String> data) throws ClientProtocolException, IOException {
    return post(uri, data, 10000, 3);
  }

  /**
   * POST请求.
   * 
   * @param uri
   *          请求地址
   * @param data
   *          请求数据
   * @param retryCount
   *          重试次数
   * @throws IOException IOException
   * @throws ClientProtocolException ClientProtocolException
   */
  public static String post(final String uri, Map<String, String> data, int retryCount) throws ClientProtocolException, IOException {
    return post(uri, data, 10000, retryCount);
  }

  /**
   * 转换请求参数.
   * 
   * @param dataMap
   *          map类型参数
   * @return 表单参数实体
   */
  private static UrlEncodedFormEntity getFormData(Map<String, String> dataMap) {
    try {
      List<BasicNameValuePair> data = new ArrayList<BasicNameValuePair>();
      Iterator<String> keys = dataMap.keySet().iterator();
      while (keys.hasNext()) {
        String key = keys.next();
        data.add(new BasicNameValuePair(key, dataMap.get(key)));
      }
      UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(data);
      return uefEntity;
    } catch (Exception err) {
      logger.error("获取参数异常{}", err);
    }
    return null;
  }

}
