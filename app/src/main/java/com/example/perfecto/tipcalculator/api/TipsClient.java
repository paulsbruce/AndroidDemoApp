package com.example.perfecto.tipcalculator.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by paulb on 4/24/17.
 */

public interface TipsClient {

    @GET("/tips")
    Call<TipsClientBuilder.TipsResponse> getTips ();

}