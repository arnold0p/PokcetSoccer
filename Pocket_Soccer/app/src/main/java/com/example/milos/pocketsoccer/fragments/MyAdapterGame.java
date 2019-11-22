package com.example.milos.pocketsoccer.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.milos.pocketsoccer.R;
import com.example.milos.pocketsoccer.database.entity.Game;
import com.example.milos.pocketsoccer.database.entity.Stats;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterGame extends RecyclerView.Adapter<MyAdapterGame.MyHolder> {

    private Context mContext;

    List<Game> stats = new ArrayList<>();

    public List<Game> getStats() {
        return stats;
    }

    public void setGames(List<Game> stats) {
        this.stats = stats;
        notifyDataSetChanged();
    }

    public MyAdapterGame(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.myholder,viewGroup,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.setmStats(stats.get(i));
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private Game mGame;

        private TextView mTextPlayer1;
        private TextView mTextResult;
        private TextView mTextPlayer2;
        private TextView date;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mTextPlayer1 = itemView.findViewById(R.id.textplayer1);
            mTextResult = itemView.findViewById(R.id.result);
            mTextPlayer2 = itemView.findViewById(R.id.textplayer2);
            date = itemView.findViewById(R.id.textdate);
        }

        public Game getmGame() {
            return mGame;
        }

        public void setmStats(Game mGame) {
            this.mGame = mGame;
            mTextPlayer1.setText(mGame.getPlayer1());
            mTextResult.setText(mGame.getRes1() + " : " + mGame.getRes2());
            mTextPlayer2.setText(mGame.getPlayer2());
            date.setText(mGame.getDate());
        }
    }
}
