package com.filippoints.controller;

import android.content.Context;

import com.filippoints.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Properties;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hlib on 6/21/18.
 */

public class Controller {

    static final private String API_VERSION = "v1.0";
    static final private String BASE_URL =
            String.format("http://www.filippoints.com/api/%s/", API_VERSION);

    private static WebApi INSTANCE = null;
    private static final java.lang.String PASSWORD_PROPERTIES_FILE = "password.properties";

    public synchronized static WebApi getWebAPI(Context context) {
        if (null == INSTANCE) {
            INSTANCE = createWebAPI(context);
        }
        return INSTANCE;
    }

    private static WebApi createWebAPI(final Context context) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Properties prop = new Properties();
                InputStream propFile = context.getApplicationContext().getAssets().open(PASSWORD_PROPERTIES_FILE);
                prop.load(propFile);
                Request.Builder builder = originalRequest.newBuilder()
                        .header("Authorization",
                        Credentials.basic(prop.getProperty("username"), prop.getProperty("password")));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        WebApi webApi = retrofit.create(WebApi.class);
        return webApi;
    }
}