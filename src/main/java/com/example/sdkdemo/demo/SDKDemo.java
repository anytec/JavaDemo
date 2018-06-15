package com.example.sdkdemo.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import java.io.*;

public class SDKDemo {

    private static final Logger logger = LoggerFactory.getLogger(SDKDemo.class);

    static String token = "LpgB-nMPq";


    public static byte[] getLocalFile() {
        File file = new File("/home/n-tech-admin/Pictures/allface.png");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        byte[] b = new byte[1024];
        int len = -1;
        byte[] bytes = null;
        try {
            while ((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * 人脸探测
     *
     */
    public static void detect() throws IOException {
        String url = "http://localhost:8000/v0/detect";
        HttpResponse response = Request.Post(url)
                .connectTimeout(10000)
                .socketTimeout(30000)
                .addHeader("Authorization", "Token " + token)
                .body(MultipartEntityBuilder
                        .create()
                        .addBinaryBody("photo", getLocalFile(), ContentType.create("image/jpeg"), "photo.jpg")
//                        .addTextBody("photo", "pictureUrl")   //图片url地址
                        .build())
                .execute().returnResponse();
        if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
            logger.info("detect result:\n"+EntityUtils.toString(response.getEntity()));
        }
    }


    /**
     * 人脸比对 1:1
     */
    static void verify() {
        String url = "http://localhost:8000/v0/verify";
        HttpResponse response = null;
        try {
            response = Request.Post(url)
                    .connectTimeout(10000)
                    .socketTimeout(30000)
                    .addHeader("Authorization", "Token " + token)
                    .body(MultipartEntityBuilder
                            .create()
                            .addBinaryBody("photo1", getLocalFile(), ContentType.create("image/jpeg"), "photo.jpg")
                            .addBinaryBody("photo2", getLocalFile(), ContentType.create("image/jpeg"), "photo.jpg")
                            //                        .addTextBody("photo", "pictureUrl")   //图片url地址
                            .build())
                    .execute().returnResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
            try {
                logger.info("verify result:\n"+EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            logger.error("");
        }
    }


    /**
     * 人脸搜索比对 1:N
     */

    static void identify() {
        String url = "http://localhost:8000/v0/identify";
        HttpResponse response = null;
        try {
            response = Request.Post(url)
                    .connectTimeout(10000)
                    .socketTimeout(30000)
                    .addHeader("Authorization", "Token " + token)
                    .body(MultipartEntityBuilder
                            .create()
                            .addBinaryBody("photo", getLocalFile(), ContentType.create("image/jpeg"), "photo.jpg")
                            //                        .addTextBody("photo", "pictureUrl")   //图片url地址
                            .build())
                    .execute().returnResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
            try {
                logger.info("identify result:\n"+EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        SDKDemo.detect();
        SDKDemo.verify();
        SDKDemo.identify();
    }

}
