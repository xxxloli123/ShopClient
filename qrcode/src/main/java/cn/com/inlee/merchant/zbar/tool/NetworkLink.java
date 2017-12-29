package cn.com.inlee.merchant.zbar.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/28.
 * Android开发技术网络篇之── http协议post请求方式 http://blog.csdn.net/pjk1129/article/details/6583743
 * android 用post方式上传图片到服务器 http://blog.csdn.net/nosxcy/article/details/8060560
 * android以post方式实现上传图片和表单 http://blog.csdn.net/baoabaoww/article/details/50802077
 * Android文件图片上传的详细讲解（一）HTTP multipart/form-data 上传报文格式实现手机端上传 http://topmanopensource.iteye.com/blog/1605238
 * 安卓上传图片实例，multipart/form-data实现安卓上传文件的功能 http://blog.csdn.net/u010335298/article/details/51862624
 * Android网络编程—同时上传参数和文件到服务器 http://blog.csdn.net/carterjin/article/details/7571915
 * OkHttp使用详解 http://www.open-open.com/lib/view/open1482115993812.html
 * android 文件上传（POST方式模拟表单提交）http://blog.csdn.net/ylbf_dev/article/details/50468984
 *  android使用HttpURLConnection实现带参数文件上传 http://blog.csdn.net/crazy__chen/article/details/47703781
 *  android NetWorkInfo类的isConnected()与isAvailable()两个函数 http://www.ithao123.cn/content-8205811.html
 */

public class NetworkLink {
    private static int CONNECT_TIME_OUT = 6000;
    private static final String BOUNARY = "**";//UUID.randomUUID().toString();
    //实现判断当前网络是否可用 http://www.cnblogs.com/renqingping/archive/2012/10/18/Net.html
    public static boolean link(Context context){
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
        boolean bool = false;
        if (infos != null && infos.length > 0)
            for (int i = 0; i < infos.length; i++) {
                System.out.println(i + "===状态===" + infos[i].getState());
                System.out.println(i + "===类型===" + infos[i].getTypeName());
                //if (infos[i].getState() == NetworkInfo.State.CONNECTED)// 判断当前网络状态是否为连接状态
                if(infos[i].isAvailable()) {//判断网络可用
                    bool = true;
                    break;
                }
            }
        return bool;
    }
    public static byte[] getStream(String url,String parameter){
        byte[] result = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(CONNECT_TIME_OUT);
            conn.setDoOutput(true);// 允许输入流
            conn.setDoInput(true); // 允许输出流
            conn.setUseCaches(false);// 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);//使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            conn.setRequestProperty("Content-Length", String.valueOf(parameter.getBytes().length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(parameter.getBytes());
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int lenght = -1;
                while(-1 !=(lenght = is.read(buffer)))
                    baos.write(buffer,0,lenght);
                is.close();
                result = baos.toByteArray();
                baos.close();
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != conn)
                conn.disconnect();
            try {
                if(null != is)
                    is.close();
                if(null != baos)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String getJson(String url){
        String result = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(CONNECT_TIME_OUT);
            conn.setDoOutput(true);// 允许输入流
            conn.setDoInput(true); // 允许输出流
            conn.setUseCaches(false);// 不允许使用缓存
            conn.setRequestMethod("GET");
            conn.setRequestProperty("charset", "utf-8"); // 设置编码
            // 设置文件类型
            conn.setRequestProperty("Content-Type", "text/xml/json; charset=UTF-8");
            // 设置请求参数，可通过Servlet的getHeader()获取
            conn.setRequestProperty("Cookie", "AppName=" + URLEncoder.encode("你好", "UTF-8"));
            // 设置自定义参数
            conn.setRequestProperty("MyProperty", "this is me!");
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int lenght = -1;
                while(-1 !=(lenght = is.read(buffer)))
                    baos.write(buffer,0,lenght);
                is.close();
                result = baos.toString();
                baos.close();
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != conn)
                conn.disconnect();
            try {
                if(null != is)
                    is.close();
                if(null != baos)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    //合并请求参数
    private static byte[] mergeParameters(Map<String,String> parameters){
        StringBuilder result = new StringBuilder();
        for(Map.Entry<String,String> entry:parameters.entrySet()) {
            result.append("--" + BOUNARY + "\r\n");//分界符
            result.append("Content-Disposition: form-data; name=\""+entry.getKey() + "\"\r\n");
            result.append("Content-Type: text/plain; charset=utf-8" + "\r\n");
            result.append("Content-Transfer-Encoding: 8bit" + "\r\n\r\n");
            result.append(entry.getValue() + "\r\n");
        }
        Log.i("NetworkLink","merge>>>"+result);
        return result.toString().getBytes();
    }
    //合并文件
    private static byte[] mergeFile(String path){
        return null;
    }
    /*
    * parameter上传参数,fileKey服务器接受的key,path文件路径
    * */
    public static String postFile(String url, Map<String,String> parameter,String fileKey, String path){
        String result = null;
        DataOutputStream dos = null;
        BufferedReader responseReader = null;
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(CONNECT_TIME_OUT);
            conn.setDoOutput(true);// 允许输入流
            conn.setDoInput(true); // 允许输出流
            conn.setUseCaches(false);// 不允许使用缓存
            conn.setRequestMethod("POST");
            //conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("charset", "utf-8"); // 设置编码
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary="+BOUNARY);
            OutputStream outputStream = conn.getOutputStream();
            //获得输出流，向服务器写入数据
            if(null != parameter)
                outputStream.write(mergeParameters(parameter));
            if(null != path) {
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes("--"+BOUNARY+"\r\n");
                StringBuilder sb = new StringBuilder("--"+BOUNARY+"\r\n");
                // 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件filename是文件的名字，包含后缀名的 比如:abc.png
                dos.writeBytes("Content-Disposition: form-data; name=\""+fileKey+"\";filename=\""+path+"\"\r\n" );
                dos.writeBytes("Content-Type: application/octet-stream\r\n");
                dos.writeBytes("Content-Transfer-Encoding: binary");
                dos.writeBytes("\r\n\r\n");
                sb.append("Content-Disposition: form-data; name=\"pictures\";filename=\""+path+"\"\r\n" );
                sb.append("Content-Type: application/octet-stream\r\n" );
                sb.append("Content-Transfer-Encoding: binary" );
                sb.append("\r\n\r\n");
                InputStream is = new FileInputStream(path);
                byte[] buffer = new byte[1024];
                int len = 0;
                int i = 0;
                while(-1 != (len = is.read(buffer))) {
                    sb.append(buffer);
                    dos.write(buffer, 0, len);
                    i++;
                }
                dos.writeBytes("\r\n");
                dos.writeBytes("--"+BOUNARY+"--\r\n");
                sb.append("\r\n");
                sb.append("--"+BOUNARY+"--\r\n");
                outputStream.write(sb.toString().getBytes());
                Log.i("NetworkLink","postFile--"+sb+"i*1024 = "+(i*1024+len));
            }
            dos.flush();
            dos.close();
            //conn.connect();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                //当正确响应时处理数据
                StringBuffer sb = new StringBuffer();
                String readLine;
                //处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((readLine = responseReader.readLine()) != null)
                    sb.append(readLine).append("\n");
                responseReader.close();
                result = sb.toString();
            }else
                Log.i("NetworkLink","postJson--连接失败");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != dos)
                    dos.close();
                if(null != responseReader)
                    responseReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String postImage(String url,Map<String,String> parameter,String fileKey, Bitmap bitmap){
        String result = null;
        DataOutputStream dos = null;
        BufferedReader responseReader = null;
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(CONNECT_TIME_OUT);
            conn.setDoOutput(true);// 允许输入流
            conn.setDoInput(true); // 允许输出流
            conn.setUseCaches(false);// 不允许使用缓存
            conn.setRequestMethod("POST");
            //conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("charset", "utf-8"); // 设置编码
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary="+BOUNARY);
            OutputStream outputStream = conn.getOutputStream();
            //获得输出流，向服务器写入数据
            if(null != parameter)
                outputStream.write(mergeParameters(parameter));
            if(null != bitmap) {
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes("--"+BOUNARY+"\r\n");
                // 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件filename是文件的名字，包含后缀名的 比如:abc.png
                dos.writeBytes("Content-Disposition: form-data; name=\""+fileKey+"\";filename=\"image.jpg\"\r\n" );
                dos.writeBytes("Content-Type: application/octet-stream\r\n");
                dos.writeBytes("Content-Transfer-Encoding: binary");
                dos.writeBytes("\r\n\r\n");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                dos.write(baos.toByteArray());
                dos.writeBytes("\r\n");
                dos.writeBytes("--"+BOUNARY+"--\r\n");
            }
            dos.flush();
            dos.close();
            //conn.connect();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                //当正确响应时处理数据
                StringBuffer sb = new StringBuffer();
                String readLine;
                //处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((readLine = responseReader.readLine()) != null)
                    sb.append(readLine).append("\n");
                responseReader.close();
                result = sb.toString();
            }else
                Log.i("NetworkLink","postJson--连接失败");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != dos)
                    dos.close();
                if(null != responseReader)
                    responseReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    //parameter并不能保证是对象,如果是基本数据类型比如字符串了就有局限性了，所以在调用前转换成字符串通用性更高
    public static String postJson(String url,byte[] parameter){
        String result = null;
        BufferedReader responseReader = null;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setConnectTimeout(3000);//设置连接超时时间
            httpURLConnection.setDoInput(true);//打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);//打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");//设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);//使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameter.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(parameter);
            int response = httpURLConnection.getResponseCode();//获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                //当正确响应时处理数据
                StringBuffer sb = new StringBuffer();
                String readLine;
                //处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                result = sb.toString();
            }else
                Log.i("NetworkLink","postJson--连接失败");
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != responseReader)
                    responseReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String getXml(String path, Object object){
        return null;
    }
    public static String postXml(String path,Object object){
        return null;
    }
}
