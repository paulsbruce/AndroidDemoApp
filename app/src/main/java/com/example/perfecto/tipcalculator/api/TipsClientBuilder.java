package com.example.perfecto.tipcalculator.api;

import android.support.test.espresso.core.deps.dagger.Provides;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by paulb on 4/24/17.
 */

public class TipsClientBuilder {

    public static TipsClient build() {

        String API_BASE_URL = "http://paulsbruce-androiddemoappsvc.ngrok.io/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

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

        TipsClient client =  retrofit.create(TipsClient.class);

        return client;
    }

    public class TipsResponse {

        @SerializedName("results")
        private List<Tip> results;

        public List<Tip> getResults() {
            return this.results;
        }
    }

}
