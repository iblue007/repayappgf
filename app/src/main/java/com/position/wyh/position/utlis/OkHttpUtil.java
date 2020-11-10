package com.position.wyh.position.utlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xujie on 2018-01-25.
 * OkHttp的工具类
 */
public class OkHttpUtil {

    private static OkHttpClient okHttpClient;
    private static Handler handler = new Handler();

    public static void initOkHttp() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }


    public static void downJSON(final String url, Request.Builder request, final OnDownDataListener onDownDataListener) {

        Request build = request.build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (onDownDataListener != null) {
                            try {
                                onDownDataListener.onFailure(url, e.getMessage());
                                Logger.getLogger().e(url + "----------------------------" + e.getMessage());
                            } catch (Exception e) {
                                Logger.getLogger().e("----------------------------" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDownDataListener != null) {
                            try {

                                Log.e("okhttp", url + "\n" + str);
                                onDownDataListener.onResponse(url, str);
                                onDownDataListener.onResponse(url, response);
                            } catch (Exception e) {
                                Logger.getLogger().e("--------onResponse--------------------" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }


    public static void _downJSON(final String url, final OnDownDataListener onDownDataListener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (onDownDataListener != null) {
                            try {
                                onDownDataListener.onFailure(url, e.getMessage());
                            } catch (Exception e) {
                                Logger.getLogger().e("----------------------------" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                onDownDataListener.onResponse(url, response);
                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDownDataListener != null) {
                            try {

                                Log.e("okhttp", url + "\n" + str);
                                onDownDataListener.onResponse(url, str);

                            } catch (Exception e) {
                                Logger.getLogger().e("--------onResponse--------------------" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 同步get请求 -- 让子类调用
     *
     * @return
     */
    public static String downResponse(final String url, Request.Builder request) {
        Request build = request.build();
        Call call = okHttpClient.newCall(build);
        try {
            return call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 同步get请求 -- 让子类调用
     *
     * @return
     */
    public static Response downResponse(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * post提交表单
     */
    public static void _postSubmitForm(final String url, Request.Builder request, final Map<String, String> params, final OnDownDataListener onDownDataListener) {
        if (params.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                builder.add(key, value);
            }
            FormBody formBody = builder.build();
            Request build = request.post(formBody).build();
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (onDownDataListener != null) {
                                    onDownDataListener.onFailure(url, e.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String str = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onDownDataListener != null) {
                                onDownDataListener.onResponse(url, str);
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * post提交表单
     */
    public static void _postSubmitForm(final String url, final Map<String, String> params, final OnDownDataListener onDownDataListener) {
        if (params.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                builder.add(key, value);
            }
            FormBody formBody = builder.build();

            final Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (onDownDataListener != null) {
                                    onDownDataListener.onFailure(url, e.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String str = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onDownDataListener != null) {
                                onDownDataListener.onResponse(url, str);
                            }
                        }
                    });
                }
            });
        }
    }


    public static String postSubmitFormsynchronization(final String url, final Map<String, String> params) {
        if (params.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                builder.add(key, value);
            }
            FormBody formBody = builder.build();

            final Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            try {
                Response execute = okHttpClient.newCall(request).execute();
                return execute.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        return null;
    }

    /**
     * post表单提交json
     */
    public static void _postSubmitFormJson(final String url, Request.Builder request, final OnDownDataListener onDownDataListener) {

        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (onDownDataListener != null) {
                                onDownDataListener.onFailure(url, e.getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDownDataListener != null) {
                            onDownDataListener.onResponse(url, response);
                            onDownDataListener.onResponse(url, str);
                        }
                    }
                });
            }
        });

    }


    public interface OnDownDataListener {
        void onResponse(String url, Response response);

        void onResponse(String url, String json);

        void onFailure(String url, String error);
    }


    /**
     * 获取文件MimeType
     *
     * @param filename
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }


    /**
     * 获得Request实例(不带进度)
     *
     * @param url
     * @return
     */
    private static Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames));
        return builder.build();
    }


    /**
     * 同步get请求 -- 让子类调用
     *
     * @return
     */
    public static String downJson(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response execute = call.execute();
            return execute.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过上传的文件的完整路径生成RequestBody
     *
     * @param fileNames 完整的文件路径
     * @return
     */
    private static RequestBody getRequestBody(List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart( //给Builder添加上传的文件
                    "image",  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
            );
        }
        return builder.build(); //根据Builder创建请求
    }


    /**
     * 根据url，发送异步Post请求(不带进度)
     *
     * @param url       提交到服务器的地址
     * @param fileNames 完整的上传的文件的路径名
     * @param callback  OkHttp的回调接口
     */
    public static void doPostRequest(String url, List<String> fileNames, Callback callback) {
        Call call = okHttpClient.newCall(getRequest(url, fileNames));
        call.enqueue(callback);
    }

    //获取字符串
    public static String getString(Response response) throws IOException {
        if (response != null && response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }

    public static void post_file(final String url, final Map<String, String> map, List<String> fileNames, Context context, final OnDownDataListener onDownDataListener) {

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            Logger.getLogger().e("压缩前：" + file.length());
           /* Bitmap bitmap = BitmapUtil.doParse(1000, 1000, Global.File2byte(file), Bitmap.Config.ARGB_4444);//图片压缩
            Global.saveBitmapFile(bitmap, file);*/

            Logger.getLogger().e("压缩后：" + file.length());
            if (file != null) {
                // MediaType.parse() 里面是上传的文件类型。
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                String filename = file.getName();
                // 参数分别为， 请求key ，文件名称 ， RequestBody
                requestBody.addFormDataPart("headImage", file.getName(), body);
            }
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
        // readTimeout("请求超时时间" , 时间单位);
        okHttpClient.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDownDataListener != null) {
                            try {
                                onDownDataListener.onFailure(url, e.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (onDownDataListener != null) {
                                onDownDataListener.onResponse(url, str);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    public static void _post_OneFile(Request.Builder tag, final String url, final Map<String, String> map, File file, Context context, final OnDownDataListener onDownDataListener) {


        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // MediaType.parse() 里面是上传的文件类型。
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        String filename = file.getName();
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("headImage", file.getName(), body);

        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        tag.post(requestBody.build());
        Request request = tag.build();
        // readTimeout("请求超时时间" , 时间单位);
        okHttpClient.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDownDataListener != null) {
                            try {
                                onDownDataListener.onFailure(url, e.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (onDownDataListener != null) {
                                onDownDataListener.onResponse(url, str);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public static byte[] File2byte(File file) {
        byte[] buffer = null;
        try {

            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 把batmap 转file
     *
     * @param bitmap
     */
    public static File saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
