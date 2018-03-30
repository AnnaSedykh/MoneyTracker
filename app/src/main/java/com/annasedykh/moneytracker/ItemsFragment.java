package com.annasedykh.moneytracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.annasedykh.moneytracker.api.BalanceResult;
import com.annasedykh.moneytracker.api.ItemsResult;
import com.annasedykh.moneytracker.api.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemsFragment extends Fragment {

    public static final String TYPE_KEY = "type";
    public static final int ADD_ITEM_REQUEST_CODE = 123;

    private String type;

    private RecyclerView recycler;
    private ItemsAdapter adapter;
    private SwipeRefreshLayout refresh;

    private Api api;
    private App app;

    public static ItemsFragment createItemsFragment(String type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.TYPE_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();
        adapter.setListener(new ItemsAdapterListenerImpl());

        Bundle bundle = getArguments();
        type = bundle.getString(TYPE_KEY, Item.TYPE_UNKNOWN);

        if (type.equals(Item.TYPE_UNKNOWN)) {
            throw new IllegalArgumentException("Unknown type");
        }

        app = (App) getActivity().getApplication();
        api = app.getApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.list);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(Color.CYAN, Color.BLUE, Color.GREEN);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recycler.setItemAnimator(itemAnimator);

        loadData();
        getBalance();
    }

    private void getBalance() {
        Call<BalanceResult> call = api.getBalance();
        call.enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                BalanceResult result = response.body();
                if(result != null && getString(R.string.success_msg).equals(result.status)){
                    Log.i(TAG, "onResponse: getBalance expenses = " + result.expenses + " , income = " + result.income);
                }
            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {

            }
        });
    }

    private void loadData() {

        Call<List<Item>> call = api.getItems(type);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                adapter.setData(response.body());
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                refresh.setRefreshing(false);
            }
        });
    }

    private void addItem(final Item item) {
        Call<ItemsResult> call = api.addItem(item.price, item.name, item.type);
        call.enqueue(new Callback<ItemsResult>() {
            @Override
            public void onResponse(Call<ItemsResult> call, Response<ItemsResult> response) {
                ItemsResult result = response.body();
                if (result != null && getString(R.string.success_msg).equals(result.status)) {
                    item.id = result.id;
                    adapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<ItemsResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Item item = data.getParcelableExtra("item");
            if (item.type.equals(type)) {
                addItem(item);
            }
        }
    }

    /*   ACTION MODE    */

    private ActionMode actionMode = null;

    private void removeSelectedItems() {
        for (int i = adapter.getSelectedItemCount() - 1; i >= 0; i--) {
           int position = adapter.getSelectedItems().get(i);
            Call<ItemsResult> call = api.removeItem(adapter.getData().get(position).id);
            call.enqueue(new Callback<ItemsResult>() {
                @Override
                public void onResponse(Call<ItemsResult> call, Response<ItemsResult> response) {
                    ItemsResult result = response.body();
                    if (result != null && getString(R.string.success_msg).equals(result.status)) {
                        adapter.remove(result.id);
                    }
                }

                @Override
                public void onFailure(Call<ItemsResult> call, Throwable t) {

                }
            });

        }
        actionMode.finish();
    }

    private class ItemsAdapterListenerImpl implements ItemsAdapterListener {

        @Override
        public void onItemClick(Item item, int position) {
            if (isInActionMode()) {
                toggleSelection(position);
            }
        }

        @Override
        public void onItemLongClick(Item item, int position) {
            if (isInActionMode()) {
                return;
            }
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
            toggleSelection(position);
        }

        private void toggleSelection(int position) {
            adapter.toggleSelection(position);
            actionMode.setTitle(adapter.getSelectedItemCount() + " выбрано");
        }

        private boolean isInActionMode() {
            return actionMode != null;
        }
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = new MenuInflater(getContext());
            inflater.inflate(R.menu.items_menu, menu);

            refresh.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.remove:
                    showDialog();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;

            refresh.setEnabled(true);
        }
    };

    /*  CONFIRMATION DIALOG  */

    private void showDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setListener(new ConfirmationDialogListener());
        dialog.setActionMode(actionMode);
        dialog.show(getFragmentManager(), "ConfirmationDialog");
    }

    private class ConfirmationDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    removeSelectedItems();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    actionMode.finish();
            }
        }
    }

}
