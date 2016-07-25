package com.wusui.askertwice.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wusui.askertwice.R;

import butterknife.BindView;

/**
 * Created by fg on 2016/7/25.
 */

public class AnswersAdapter extends RecyclerView.Adapter {

    private Context mContext;

    public AnswersAdapter(Context context){
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
        @BindView(R.id.tv_type)TextView tv_type;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_answers,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
