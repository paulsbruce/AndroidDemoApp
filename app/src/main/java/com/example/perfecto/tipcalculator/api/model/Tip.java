package com.example.perfecto.tipcalculator.api.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by paulb on 4/24/17.
 */

public class Tip {

    @SerializedName("id")
    private int id = -1;
    @SerializedName("subtotal")
    private Double subtotal = 0d;
    @SerializedName("percent")
    private Double percent = 0d;
    @SerializedName("split")
    private int split = 0;
    @SerializedName("lastUsed")
    private String lastUsed;

    public int getId() { return id; }
    public BigDecimal getSubtotal() { return new BigDecimal(subtotal); }
    public BigDecimal getPercent() { return new BigDecimal(percent); }
    public int getSplit() { return split; }
    public String getLastUsed() { return lastUsed; }
}