package com.example.myapplication2game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ScoreAdapter  extends RecyclerView.Adapter<ScoreAdapter.Holder> {

    public List<Score> mDataSet = new ArrayList<>();
    private ItemClickListener mListener;
    private Context c;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_score_item,parent,false);
        Holder holder = new Holder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface ItemClickListener{
    }

    public void setOnItemClickListener(ItemClickListener listener ){
        mListener = listener;
    }

    public void addScore(Score score){
        mDataSet.add(mDataSet.size(),score);
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView playerNameTextView;
        TextView playerScoreTextView;

        public Holder(View itemView , final ItemClickListener listener) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerName);
            playerScoreTextView = itemView.findViewById(R.id.playerScore);
        }

        public void setItem(int position){
            playerNameTextView.setText(mDataSet.get(position).getPlayerName());
            playerScoreTextView.setText(Integer.toString(mDataSet.get(position).getScore()));
        }
    }
}
