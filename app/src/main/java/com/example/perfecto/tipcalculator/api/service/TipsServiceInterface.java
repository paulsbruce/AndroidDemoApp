package com.example.perfecto.tipcalculator.api.service;

import com.example.perfecto.tipcalculator.api.model.TipsServiceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by paulb on 4/24/17.
 */

public interface TipsServiceInterface {

    @GET("/tips")
    Call<TipsServiceResponse> getTips ();

}