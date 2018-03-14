package com.loftschool.moneytracker;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 13.03.2018.
 */

public class ItemListActivity extends AppCompatActivity {
    private static final String TAG = "ItemListActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Record> data = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        createData();

        int offset = getResources().getDimensionPixelOffset(R.dimen.item_offset);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(offset);
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new ItemListAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void createData() {
        for (int i = 0; i < 100; i++) {
            data.add(new Record("Record #" + i, (int) (Math.random() * 10000)));
        }
    }

    private class ItemListAdapter extends RecyclerView.Adapter<RecordViewHolder> {
        @Override
        public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG, "OnCreateVewHolder");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
            return new RecordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecordViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder #" + position);
            Record record = data.get(position);
            holder.applyData(record);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class RecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView price;

        public RecordViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }

        public void applyData(Record record) {
            title.setText(record.getTitle());
            String priceFormat = String.format(getString(R.string.price), String.valueOf(record.getPrice()));
            price.setText(priceFormat);
        }
    }

    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int offset;

        public ItemOffsetDecoration(int offset) {
            this.offset = offset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            outRect.right = offset;
            outRect.left = offset;
            outRect.top = offset/2;
            outRect.bottom = offset/2;

            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = offset;
            }

        }
    }
}
