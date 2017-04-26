package com.example.perfecto.tipcalculator.api.service;

import com.example.perfecto.tipcalculator.api.inject.InjectionFactory;
import com.example.perfecto.tipcalculator.api.model.Tip;

import java.io.IOException;
import java.util.List;

/**
 * Created by paulb on 4/26/17.
 */

public class TipsServiceManager
{
    private final TipsServiceInterface mTipsClient;

    public TipsServiceManager(String endpoint) {
        mTipsClient = InjectionFactory.buildRetrofit(endpoint)
                .create(TipsServiceInterface.class);
    }
    public List<Tip> getTips() throws IOException {
        return mTipsClient.getTips().execute().body().getTipsResponse().getTips();
    }

}
