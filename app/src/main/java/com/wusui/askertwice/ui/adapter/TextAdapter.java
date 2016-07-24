package com.wusui.askertwice.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wusui.askertwice.R;

import butterknife.BindView;

/**
 * Created by fg on 2016/7/20.
 */

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {

    private @BindView(R.id.frag_type)
    TextView type;
    private @BindView(R.id.frag_title) TextView title;
    private @BindView(R.id.frag_time) TextView time;
    private @BindView(R.id.frag_name) TextView name;
    private @BindView(R.id.frag_text) TextView text;
    private @BindView(R.id.frag_comment) ImageView comment;
    private @BindView(R.id.frag_star) ImageView star;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }


}
