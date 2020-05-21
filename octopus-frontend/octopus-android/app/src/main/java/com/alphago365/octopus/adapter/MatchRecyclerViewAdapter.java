package com.alphago365.octopus.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alphago365.octopus.R;
import com.alphago365.octopus.databinding.ItemMatchChildBinding;
import com.alphago365.octopus.databinding.ItemMatchHeaderBinding;
import com.alphago365.octopus.mvp.model.ListItem;
import com.alphago365.octopus.payload.MatchChild;
import com.alphago365.octopus.payload.MatchHeader;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MatchRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AsyncListDiffer<ListItem> mDiffer = new AsyncListDiffer(this, DIFF_CALLBACK);

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public void submitList(List<ListItem> list) {
        mDiffer.submitList(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_HEADER:
                ItemMatchHeaderBinding headerBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_match_header, parent, false);
                return new MatchHeaderViewHolder(headerBinding);
            case ListItem.TYPE_ITEM:
                ItemMatchChildBinding childBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_match_child, parent, false);
                return new MatchChildViewHolder(childBinding);
            default:
                throw new IllegalArgumentException("Illegal item view type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        ListItem listItem = mDiffer.getCurrentList().get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ListItem.TYPE_HEADER:
                ((MatchHeaderViewHolder) holder).dataBinding.setMatchHeader((MatchHeader) listItem);
                break;
            case ListItem.TYPE_ITEM:
                ((MatchChildViewHolder) holder).dataBinding.setMatchChild((MatchChild) listItem);
                break;
            default:
                throw new IllegalArgumentException("Illegal item view type: " + itemViewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDiffer.getCurrentList().get(position).getType();
    }

    private static final DiffUtil.ItemCallback<ListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ListItem>() {

        @Override
        public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            return oldItem.areItemsTheSame(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            return oldItem.areContentsTheSame(newItem);
        }
    };

    static class MatchHeaderViewHolder extends RecyclerView.ViewHolder {

        private ItemMatchHeaderBinding dataBinding;


        MatchHeaderViewHolder(ItemMatchHeaderBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }
    }

    static class MatchChildViewHolder extends RecyclerView.ViewHolder {

        private ItemMatchChildBinding dataBinding;


        MatchChildViewHolder(ItemMatchChildBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }
    }

}
