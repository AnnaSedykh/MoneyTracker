package com.loftschool.moneytracker;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemsActivity extends AppCompatActivity {
    private static final String TAG = "ItemsActivity";

    private RecyclerView recycler;
    private ItemsAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        adapter = new ItemsAdapter();
        recycler = findViewById(R.id.list);
        recycler.setLayoutManager(new LinearLayoutManager(this));
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
