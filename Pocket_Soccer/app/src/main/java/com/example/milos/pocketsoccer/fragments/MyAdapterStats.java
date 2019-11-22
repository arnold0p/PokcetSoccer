package com.example.milos.pocketsoccer.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.milos.pocketsoccer.MainActivity;
import com.example.milos.pocketsoccer.R;
import com.example.milos.pocketsoccer.database.entity.Stats;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterStats extends RecyclerView.Adapter<MyAdapterStats.MyHolder> {

    private Context mContext;

    List<Stats> stats = new ArrayList<>();

    public List<Stats> getStats() {
        return stats;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
        notifyDataSetChanged();
    }

    public MyAdapterStats(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.myholder, viewGroup, false);

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

        private Stats mStats;

        private TextView mTextPlayer1;
        private TextView mTextResult;
        private TextView mTextPlayer2;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mTextPlayer1 = itemView.findViewById(R.id.textplayer1);
            mTextResult = itemView.findViewById(R.id.result);
            mTextPlayer2 = itemView.findViewById(R.id.textplayer2);

            LinearLayout lp = itemView.findViewById(R.id.lin1);
            lp.removeViewAt(1); // and so on

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatsFragment.showGames(mStats.getPlayer1(), mStats.getPlayer2(),mStats.getWin1(),mStats.getWin2());
                }
            });


        }

        public Stats getmStats() {
            return mStats;
        }

        public void setmStats(Stats mStats) {
            this.mStats = mStats;
            mTextPlayer1.setText(mStats.getPlayer1());
            mTextResult.setText(mStats.getWin1() + " : " + mStats.getWin2());
            mTextPlayer2.setText(mStats.getPlayer2());
        }
    }
}
