package net.m3u8.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * http 请求工具类
 *
 * @author Liushaoxiong
 * @date 2022-06-12 17:48:00
 */
public class HttpUtils {

    /**
     * 信任所有证书
     * Start
     */
    static {
        try {
            //trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }

    //private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
    //    TrustManager[] trustAllCerts = new TrustManager[1];
    //    trustAllCerts[0] = new SMCHttpUtil.TrustAllManager();
    //    SSLContext sc = SSLContext.getInstance("SSL");
    //    sc.init(null, trustAllCerts, null);
    //    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    //}

    /**
     * 信任所有证书
     * End
     */

    /**
     * Get请求
     *
     * @param getUrl  请求地址
     * @param param   携带参数
     * @param headers headers参数
     * @return
     * @throws Exception
     */
    public static String doGet(String getUrl, Map<String, Object> param, Map<String, String> headers) throws Exception {
        // 封装发送的请求参数
        StringBuffer buffer = new StringBuffer();
        if (param != null) {
            buffer.append("?");
            int x = 0;
            for (Map.Entry<String, Object> map : param.entrySet()) {
                buffer.append(map.getKey()).append("=").append(map.getValue().toString());
                if (x != param.size() - 1) {
                    buffer.append("&");
                }
                x++;
            }
        }
        String urlPath = getUrl + buffer.toString();
        URL url = new URL(urlPath);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
        // 设置请求头属性参数
        httpUrlConnection.setRequestProperty("charset", "UTF-8");
        httpUrlConnection.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        httpUrlConnection.setRequestProperty("connection", "keep-alive");
        httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/4.76");
        if (headers != null) {
            for (Map.Entry<String, String> map : headers.entrySet()) {
                httpUrlConnection.setRequestProperty(map.getKey(), map.getValue());
            }
        }
        String response = "";// 响应内容
        String status = "";// 响应状态6
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            httpUrlConnection.connect();
            // 定义BufferedReader输入流来读取URL的响应数据
            in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            // 获得URL的响应状态码
            status = new Integer(httpUrlConnection.getResponseCode()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("响应状态:" + status);
        System.out.println("响应数据:" + response);
        return response;
    }

    /**
     * Post请求
     *
     * @param postUrl 请求地址
     * @param param   请求参数
     * @param headers headers参数
     * @return
     * @throws Exception
     */
    public static String doPost(String postUrl, Map<String, Object> param, Map<String, String> headers) throws Exception {
        // 封装发送的请求参数
        StringBuffer buffer = new StringBuffer();
        if (param != null) {
            int x = 0;
            for (Map.Entry<String, Object> map : param.entrySet()) {
                buffer.append(map.getKey()).append("=").append(map.getValue().toString());
                if (x != param.size() - 1) {
                    buffer.append("&");
                }
                x++;
            }
        }
        URL url = new URL(postUrl);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
        // 设置请求头属性参数
        httpUrlConnection.setRequestProperty("charset", "UTF-8");
        if (headers != null) {
            for (Map.Entry<String, String> map : headers.entrySet()) {
                httpUrlConnection.setRequestProperty(map.getKey(), map.getValue());
            }
        }
        // 输入输出都打开
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        String response = "";// 响应内容
        String status = "";// 响应状态
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConnection.getOutputStream());
            // 发送请求参数
            out.write(buffer.toString());
            // flush输出流的缓冲
            out.flush();
            httpUrlConnection.connect();
            // 定义BufferedReader输入流来读取URL的响应数据
            in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            // 获得URL的响应状态码
            status = new Integer(httpUrlConnection.getResponseCode()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("响应状态:" + status);
        System.out.println("响应数据:" + response);
        return response;
    }

    /**
     * 携带Json串的网络请求
     *
     * @param hostUrl     请求地址
     * @param requestType 请求类型  POST | DELETE | UPDATE
     * @param json        Json字符串
     * @return
     * @throws Exception
     */
    public static String doJson(String hostUrl, String requestType, String json) throws Exception {
        OutputStream out = null;
        BufferedReader br = null;
        String response = "";// 响应内容
        String status = "";// 响应状态
        try {
            // 创建 URL
            URL restUrl = new URL(hostUrl);
            // 打开连接
            HttpURLConnection httpUrlConnection = (HttpURLConnection) restUrl.openConnection();
            // 设置请求方式
            httpUrlConnection.setRequestMethod(requestType);
            httpUrlConnection.setRequestProperty("Connection", "keep-Alive");
            //设置发送文件类型
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            // 输入输出都打开
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            //开始连接
            httpUrlConnection.connect();
            // 传递参数流的方式
            out = httpUrlConnection.getOutputStream();
            out.write(json.getBytes());
            out.flush();
            // 读取数据
            br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "utf-8"));
            String line = null;
            while (null != (line = br.readLine())) {
                response += line;
            }
            // 获得URL的响应状态码
            status = new Integer(httpUrlConnection.getResponseCode()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("响应状态:" + status);
        System.out.println("响应数据:" + response);
        return response;
    }

    /**
     * 读取本地html文件里的html代码
     *
     * @return
     */
    public static String toHtmlString(File file) {
        // 获取HTML文件流
        StringBuffer htmlSb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "unicode"));
            while (br.ready()) {
                htmlSb.append(br.readLine());
            }
            br.close();
            // 删除临时文件
            //file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // HTML文件字符串
        String htmlStr = htmlSb.toString();
        // 返回经过清洁的html文本
        return htmlStr;
    }

    public static void getHtml(String htmlStr) {
        try {
            //String htmlStr = HttpUtils.doGet("https://paoyou.ml/lists/2/12.html", null, null);
            //解析字符串为Document对象
            Document doc = Jsoup.parse(htmlStr);
            //获取body元素，获取class="fc"的table元素
            Elements table = doc.body().getElementsByClass("fed-list-info fed-part-rows");
            //获取body元素
            Elements children = table.first().children();
            //获取tr元素集合
            Elements tr = children.get(0).getElementsByTag("ul");
            //遍历tr元素，获取td元素，并打印
            for (int i = 0; i < tr.size(); i++) {
                Element e1 = tr.get(i);
                Elements td = e1.getElementsByTag("li");
                for (int j = 0; j < td.size(); j++) {
                    String value = td.get(j).text();
                    System.out.print("  " + value);
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
