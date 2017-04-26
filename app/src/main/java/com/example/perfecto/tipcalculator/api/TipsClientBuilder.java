package com.example.perfecto.tipcalculator.api;

import com.example.perfecto.tipcalculator.api.service.TipsServiceInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by paulb on 4/24/17.
 */

public class TipsClientBuilder {

    public static TipsServiceInterface build() {

        String API_BASE_URL = "http://paulsbruce-androiddemoappsvc.ngrok.io/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                //.proxy(new Proxy(Proxy.Type.HTTP,  new InetSocketAddress("paulsbruce-androiddemoappproxy.ngrok.io", 80)))
        ;

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        TipsServiceInterface client =  retrofit.create(TipsServiceInterface.class);

        return client;
    }



}
