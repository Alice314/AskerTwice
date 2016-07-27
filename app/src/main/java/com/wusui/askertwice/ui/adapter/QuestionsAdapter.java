package com.wusui.askertwice.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wusui.askertwice.R;
import com.wusui.askertwice.model.QuestionsBean;

import java.util.List;

/**
 * Created by fg on 2016/7/20.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {


    private Context mContext;
    private List<QuestionsBean> mQuestions;


    public QuestionsAdapter(Context context, List<QuestionsBean> questions){
        mContext = context;
        mQuestions = questions;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
         TextView type;
         TextView title;
         TextView time;
         TextView name;
         TextView text;
         TextView comment;
         TextView star;
         CardView mCardView;
         ImageView answer;

        public ViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.q_tv_type);
            title = (TextView) itemView.findViewById(R.id.q_tv_title);
            time = (TextView) itemView.findViewById(R.id.q_tv_date);
            text = (TextView) itemView.findViewById(R.id.q_tv_content);
            name = (TextView) itemView.findViewById(R.id.q_tv_author);
            comment = (TextView) itemView.findViewById(R.id.q_tv_answer_count);
            star = (TextView) itemView.findViewById(R.id.q_tv_star_count);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            answer = (ImageView) itemView.findViewById(R.id.answer);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_questions,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (mQuestions.get(position) != null){
            holder.title.setText(mQuestions.get(position).getTitle());
            Log.e("QuestionsAdapter",mQuestions.get(position).getTitle());
            holder.type.setText(mQuestions.get(position).getType());
            holder.time.setText(mQuestions.get(position).getDate());
            holder.name.setText(mQuestions.get(position).getAuthorName());
            holder.text.setText(mQuestions.get(position).getContent());
            holder.comment.setText(mQuestions.get(position).getAnswerCount());
            holder.star.setText(mQuestions.get(position).getStarCount());
        }
        if (itemClickListener != null){
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    itemClickListener.onItemClick(holder.mCardView,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }


    public OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }


}
