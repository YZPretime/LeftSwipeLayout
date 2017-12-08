package com.example.retime.leftswipelayoutdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retime.leftswipelayoutdemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvSwipe = (RecyclerView) findViewById(R.id.rv_left_swipe);
        rvSwipe.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSwipe.setAdapter(new LeftSwipeAdapter());
    }

    class LeftSwipeAdapter extends RecyclerView.Adapter<LeftSwipeAdapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_swipe, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            holder.tvMain.setText("主内容 " + position);
            holder.tvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "点击了删除" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        class Holder extends RecyclerView.ViewHolder {

            private TextView tvMain;
            private TextView tvDelete;

            public Holder(View itemView) {
                super(itemView);
                tvMain = (TextView) itemView.findViewById(R.id.tv_main);
                tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            }
        }
    }
}
