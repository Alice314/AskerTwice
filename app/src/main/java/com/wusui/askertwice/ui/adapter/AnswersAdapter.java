package com.wusui.askertwice.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wusui.askertwice.R;
import com.wusui.askertwice.model.AnswerBean;

import java.util.List;

/**
 * Created by fg on 2016/7/25.
 */

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {


    private Context mContext;
    private List<AnswerBean> mAnswer;

    public AnswersAdapter(Context context,List<AnswerBean>answer){
        mContext = context;
        mAnswer = answer;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView type;
        TextView author;
        TextView date;
        ImageView like;
        TextView like_num;
        ImageView dislike;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
             type = (TextView) itemView.findViewById(R.id.tv_type);
             author = (TextView) itemView.findViewById(R.id.tv_author);
             date = (TextView) itemView.findViewById(R.id.tv_date);
             like = (ImageView) itemView.findViewById(R.id.iv_like);
            like_num = (TextView) itemView.findViewById(R.id.tv_like_number);
            dislike = (ImageView) itemView.findViewById(R.id.iv_dislike);
            content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_answers,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mAnswer.get(position) != null){
            //holder.type.setText(mAnswer.get(position).ge);
            holder.author.setText(mAnswer.get(position).getAuthorName());
            holder.date.setText(mAnswer.get(position).getDate());
           //holder.like_num.setText(mAnswer.get(position).getLikeNumber());
            holder.like_num.setText(String.valueOf(mAnswer.get(position).getLikeNumber()));
            holder.content.setText(mAnswer.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mAnswer.size();
    }
}
