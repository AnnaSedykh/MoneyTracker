package com.annasedykh.moneytracker;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annasedykh.moneytracker.api.Api;
import com.annasedykh.moneytracker.api.BalanceResult;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class BalanceFragment extends Fragment{

    private TextView total;
    private TextView income;
    private TextView expense;

    private DiagramView diagram;
    private Api api;
    private App app;

    public static BalanceFragment createBalanceFragment(){
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App) getActivity().getApplication();
        api = app.getApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        total = view.findViewById(R.id.total);
        income = view.findViewById(R.id.income);
        expense = view.findViewById(R.id.expense);
        diagram = view.findViewById(R.id.diagram);

        getBalance();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isResumed()) {
//            updateData();
//        }
//    }

    private void getBalance() {
        Call<BalanceResult> call = api.getBalance();
        call.enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                BalanceResult result = response.body();
                if(result != null && getString(R.string.success_msg).equals(result.status)){
                    total.setText(getString(R.string.price_int, result.income - result.expenses));
                    income.setText(getString(R.string.price_int, result.income));
                    expense.setText(getString(R.string.price_int, result.expenses));
                    diagram.update(result.income, result.expenses);
                }
            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {

            }
        });
    }
}
