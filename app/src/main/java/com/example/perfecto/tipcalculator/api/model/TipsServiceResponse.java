package com.example.perfecto.tipcalculator.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by paulb on 4/26/17.
 */

public class TipsServiceResponse {

    public class TipsResponse {

        @SerializedName("results")
        private List<Tip> results;

        public List<Tip> getTips() {
            return this.results;
        }
    }

    @SerializedName("response")
    public TipsResponse mTipsResponse;

    public TipsResponse getTipsResponse() {
        return mTipsResponse;
    }
}
