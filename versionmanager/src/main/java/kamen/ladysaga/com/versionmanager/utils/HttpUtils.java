package kamen.ladysaga.com.versionmanager.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SK on 2018/7/3.
 */

public class HttpUtils {
    public static String getHtml(final String path) {
        try{
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            InputStream inStream = conn.getInputStream();
            byte[] data = readInputStream(inStream);
            String html = new String(data, "utf-8");
            if(html != null && html.length() > 0) {
                return html;
            }
        } catch(Exception e) {
        }
        return "";
    }

    /**
     * 读取输入流，得到html的二进制数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
