package com.loftschool.moneytracker;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemsFragment extends Fragment {

    public static final int TYPE_INCOMES = 0;
    public static final int TYPE_EXPENSES = 1;
    public static final int TYPE_BALANCE = 2;
    private static final int TYPE_UNKNOWN = -1;
    private static final String TYPE_KEY = "type";

    private int type;

    private RecyclerView recycler;
    private ItemsAdapter adapter;

    public static ItemsFragment createItemsFragment(int type){
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ItemsFragment.TYPE_KEY, ItemsFragment.TYPE_INCOMES);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();

        Bundle bundle = getArguments();
        type = bundle.getInt(TYPE_KEY, TYPE_UNKNOWN);

        if(type == TYPE_UNKNOWN){
            throw new IllegalArgumentException("Unknown type");
        }
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

        int offset = getResources().getDimensionPixelOffset(R.dimen.item_offset);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(offset);
        recycler.addItemDecoration(itemDecoration);
    }

    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int offset;

        private ItemOffsetDecoration(int offset) {
            this.offset = offset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            outRect.right = offset;
            outRect.left = offset;
            outRect.top = offset / 2;
            outRect.bottom = offset / 2;

            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = offset;
            }

        }
    }
}
