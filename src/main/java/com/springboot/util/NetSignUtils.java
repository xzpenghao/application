package com.springboot.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NetSignUtils {

    private final static String url="http://172.18.0.112:8088/BDC_tongshan/BDCSrv.asmx?wsdl";


    public static void main(String[] args) throws Exception {
        List<String> stringList=new ArrayList<>();
        String htxx=spfyght("2019062570014");
        String qlrxx=spfQlrxx("2019062570014");
        stringList.add(htxx);
        stringList.add(qlrxx);
    }



    public static String spfyght(String contractNumber) throws Exception {
        //soap服务地址

        String soapXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
                + "<soap12:Body>" + "<FC_SPFYGHT>" + "<sParams>" + "HTBH="
                + contractNumber + ",SPFHTBAH="+ contractNumber +"</sParams>" + "</FC_SPFYGHT>" + "</soap12:Body>" + "</soap12:Envelope>";

        return client(soapXml);
    }


    public static String spfQlrxx(String contractNumber) throws Exception {
        //soap服务地址
        String url = "http://172.18.0.112:8088/BDC_tongshan/BDCSrv.asmx?wsdl";

        String soapXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
                + "<soap12:Body>" + "<SPF_FC_CLMMHT>" + "<sParams>" + "HTBAH="
                + contractNumber + "</sParams>" + "</SPF_FC_CLMMHT>" + "</soap12:Body>" + "</soap12:Envelope>";

        return client(soapXml);
    }



    private static String client(String soapXml) throws Exception{
        //创建httpcleint对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建http Post请求
        HttpPost httpPost = new HttpPost(url);
        String str="";
        // 构建请求配置信息
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) // 创建连接的最长时间
                .setConnectionRequestTimeout(500) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(3 * 1000) // 数据传输的最长时间10s
                .build();
        httpPost.setConfig(config);
        CloseableHttpResponse response = null;
        try {
            //采用SOAP1.1调用服务端，这种方式能调用服务端为soap1.1和soap1.2的服务
//            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
//            httpPost.setHeader("SOAPAction", "");
            //采用SOAP1.2调用服务端，这种方式只能调用服务端为soap1.2的服务
            httpPost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
            StringEntity stringEntity = new StringEntity(soapXml, Charset.forName("UTF-8"));
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(content);
                return  content;
            } else {
                System.out.println("调用失败!" + response.getStatusLine().toString());
                return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                response.close();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
        return str;
    }




}
