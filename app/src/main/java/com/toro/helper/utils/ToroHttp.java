package com.toro.helper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Create By liujia
 * on 2018/10/22.
 **/
public class ToroHttp {

    public static int ConnectTimeOut = 5000;

    public interface HttpResponseHandler {
        public void onSuccess(String result);
        public void onFailed();
    }

    public interface NetworkBitmapRespense {
        public void onSuccess(Bitmap bitmap);
        public void onFailed();
    }

    public static void setNetworkBitmap(final String url, final NetworkBitmapRespense handler) {
        Runnable networkImg = new Runnable() {
            @Override
            public void run() {
                try {
                    URL conn = new URL(url);
                    InputStream in = conn.openConnection().getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    if(bitmap != null) {
                        handler.onSuccess(bitmap);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.onFailed();
            }
        };
        new Thread(networkImg).start();
    }

    public static String doGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString=url;
            if(param!=null) {
                //String urlS= URLEncoder.encode(param, "utf-8");
                urlNameString = url + "?" + param;
            }
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("referer", "http://www.huitongshidai.info/");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String doPost(String url,JSONObject obj) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            //添加http头信息
            httppost.addHeader("Content-Type", "application/json");
            httppost.setEntity(new StringEntity(obj.toString()));
            HttpResponse response;
            response = httpclient.execute(httppost);
            //检验状态码，如果成功接收数据
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPostForToken(String url,JSONObject obj,String token) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            //添加http头信息
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("Authorization", token);
            httppost.setEntity(new StringEntity(obj.toString()));
            HttpResponse response;
            response = httpclient.execute(httppost);
            //检验状态码，如果成功接收数据
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadFile(String url,ArrayList<String> paths, String token){
        String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            conn.setRequestProperty("Charset", "UTF-8");
            //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", token);
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            for(int i = 0; i < paths.size(); i++){
                File file = new File(paths.get(i));
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"img" + i + "\"; filename=\"" + file.getName()+"\""+LINE_END);
                //sb.append("Content-Type: application/octet-stream; charset="+ "UTF-8" +LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = -1;
                while((len=is.read(bytes))!=-1) {
                    dos.write(bytes, 0, len);
                }

                dos.write(LINE_END.getBytes());
                is.close();

            }
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();

            int res = conn.getResponseCode();
            //  Log.e(TAG, "response code:"+res);
            if(res==200){
//                        listener.onFinish(res);
                String result = conn.getResponseMessage();
                return result;
            }

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

}
