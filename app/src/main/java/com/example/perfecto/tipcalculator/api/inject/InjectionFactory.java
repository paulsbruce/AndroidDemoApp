package com.example.perfecto.tipcalculator.api.inject;

/**
 * Created by paulb on 4/26/17.
 */

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InjectionFactory {

    public static OkHttpClient buildOkhttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public static Retrofit buildRetrofit(final String endpoint) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(endpoint).client(buildOkhttpClient())
                .build();
    }
}