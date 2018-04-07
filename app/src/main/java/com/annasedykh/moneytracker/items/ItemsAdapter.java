package com.annasedykh.moneytracker.items;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annasedykh.moneytracker.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> data = new ArrayList<>();
    private ItemsAdapterListener listener = null;

    public void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ItemViewHolder holder, int position) {
        Item item = data.get(position);
        holder.bind(item, position, listener, selections.get(position, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Item> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        data.add(0, item);
        notifyItemInserted(0);
    }

    private SparseBooleanArray selections = new SparseBooleanArray();

    void toggleSelection(int position) {
        if (selections.get(position, false)) {
            selections.delete(position);
        } else {
            selections.put(position, true);
        }
        notifyItemChanged(position);
    }

    void clearSelections() {
        selections.clear();
        notifyDataSetChanged();
    }

    int getSelectedItemCount() {
        return selections.size();
    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selections.size());
        for (int i = 0; i < selections.size(); i++) {
            items.add(selections.keyAt(i));
        }
        return items;
    }

    Item remove(int id) {
        for (int position = 0; position < data.size(); position++) {
            if (id == data.get(position).id) {
                final Item item = data.remove(position);
                notifyItemRemoved(position);
                return item;
            }
        }
        return null;
    }


    public List<Item> getData() {
        return data;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView price;
        private Context context;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            context = itemView.getContext();
        }

        public void bind(final Item item, final int position, final ItemsAdapterListener listener, boolean selected) {
            name.setText(item.name);
            if (item.price.endsWith(context.getString(R.string.ruble))) {
                price.setText(item.price);
            } else {
                price.setText(context.getString(R.string.price, item.price));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(item, position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onItemLongClick(item, position);
                    }
                    return true;
                }
            });
            itemView.setActivated(selected);
        }
    }
}
