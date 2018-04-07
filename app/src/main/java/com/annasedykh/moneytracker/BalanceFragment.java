package com.annasedykh.moneytracker;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annasedykh.moneytracker.api.Api;

public class BalanceFragment extends Fragment {

    private TextView total;
    private TextView income;
    private TextView expense;

    private DiagramView diagram;
    private Api api;
    private App app;

    public static BalanceFragment createBalanceFragment() {
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            getBalance();
        }
    }

    private void getBalance() {
        int resultIncome = ItemsFragment.getResultIncome();
        int resultExpenses = ItemsFragment.getResultExpenses();

        total.setText(getString(R.string.price_int, resultIncome - resultExpenses));
        income.setText(getString(R.string.price_int, resultIncome));
        expense.setText(getString(R.string.price_int, resultExpenses));
        diagram.update(resultIncome, resultExpenses);

    }
}
