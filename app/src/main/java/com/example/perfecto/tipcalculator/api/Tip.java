package com.example.perfecto.tipcalculator.api;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by paulb on 4/24/17.
 */

public class Tip {

    @SerializedName("id")
    private int id;
    @SerializedName("subtotal")
    private Double subtotal;
    @SerializedName("percent")
    private Double percent;
    @SerializedName("split")
    private int split;
    @SerializedName("lastUsed")
    private String lastUsed;

    public int getId() { return id; }
    public BigDecimal getSubtotal() { return new BigDecimal(subtotal); }
    public BigDecimal getPercent() { return new BigDecimal(percent); }
    public int getSplit() { return split; }
    public String getLastUsed() { return lastUsed; }
}