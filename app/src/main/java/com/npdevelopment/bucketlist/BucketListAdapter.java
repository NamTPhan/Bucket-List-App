package com.npdevelopment.bucketlist;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private List<BucketListItem> bucketList;
    public BucketListListener bucketListListener;
    private final int ITEM_NOT_DONE = 0;

    public BucketListAdapter(List<BucketListItem> bucketList, BucketListListener bucketListListener) {
        this.bucketList = bucketList;
        this.bucketListListener = bucketListListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBucketListTitle;
        private TextView tvBucketListDescription;
        private CheckBox checkBoxDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxDone = itemView.findViewById(R.id.checkBoxItem);
            tvBucketListTitle = itemView.findViewById(R.id.bucketListTitletv);
            tvBucketListDescription = itemView.findViewById(R.id.bucketListDesctv);

            checkBoxDone.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    bucketListListener.onCheckBoxClick(bucketList.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public BucketListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_bucket_list, viewGroup, false);
        return new BucketListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.checkBoxDone.setChecked((bucketList.get(viewHolder.getAdapterPosition()).getDone() == ITEM_NOT_DONE) ? false : true);
        viewHolder.tvBucketListTitle.setText(bucketList.get(viewHolder.getAdapterPosition()).getTitle());
        viewHolder.tvBucketListDescription.setText(bucketList.get(viewHolder.getAdapterPosition()).getDescription());

        if (bucketList.get(viewHolder.getAdapterPosition()).getDone() == ITEM_NOT_DONE) {
            // Remove the strike through the text
            viewHolder.tvBucketListTitle.setPaintFlags(viewHolder.tvBucketListTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvBucketListDescription.setPaintFlags(viewHolder.tvBucketListDescription.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // Add the strike through the text
            viewHolder.tvBucketListTitle.setPaintFlags(viewHolder.tvBucketListTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvBucketListDescription.setPaintFlags(viewHolder.tvBucketListDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    public interface BucketListListener {
        void onCheckBoxClick(BucketListItem bucketListItem);
    }
}
