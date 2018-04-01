package com.annasedykh.moneytracker.api;

import com.google.gson.annotations.SerializedName;

public class BalanceResult {
    public String status;
    @SerializedName("total_expenses")
    public int expenses;
    @SerializedName("total_income")
    public int income;
}


