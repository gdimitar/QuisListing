package com.quislisting.retrofit.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quislisting.endpoints.rest.BaseConfigurations;
import com.quislisting.endpoints.rest.RestRouter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(BaseConfigurations.RetrofitTimeoutConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(BaseConfigurations.RetrofitTimeoutConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(BaseConfigurations.RetrofitTimeoutConfig.WRITE_TIMEOUT, TimeUnit.SECONDS).build();

        final Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(RestRouter.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }
}
