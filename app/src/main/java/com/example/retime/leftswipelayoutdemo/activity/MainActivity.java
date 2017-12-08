package com.example.retime.leftswipelayoutdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retime.leftswipelayoutdemo.R;
import com.example.retime.leftswipelayoutdemo.view.LeftSwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvSwipe = (RecyclerView) findViewById(R.id.rv_left_swipe);
        rvSwipe.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("主内容" + i);
        }
        rvSwipe.setAdapter(new LeftSwipeAdapter(datas, new LeftSwipeAdapter.TouchListener() {
            @Override
            public void onDelete() {
                closeListViewItem(false);
            }
            @Override
            public void onTouchOutside() {
                closeListViewItem(true);
            }
        }));
    }

    /**
     * 关掉删除按钮
     * @param smooth 为true时有关闭过程动画，false时直接关掉
     */
    private void closeListViewItem(boolean smooth) {
        for (int i = 0; i < rvSwipe.getChildCount(); i++) {
            View view = rvSwipe.getChildAt(i);
            if (view instanceof LeftSwipeLayout) {
                LeftSwipeLayout leftSwipeLayout = (LeftSwipeLayout) view;
                if (leftSwipeLayout.getState() == LeftSwipeLayout.STATE_OPEN) {
                    if (smooth) {leftSwipeLayout.smoothClose();}
                    else {leftSwipeLayout.quickClose();}
                }
            }
        }
    }

    static class LeftSwipeAdapter extends RecyclerView.Adapter<LeftSwipeAdapter.Holder> {

        interface TouchListener {
            void onDelete(); //点击了删除按钮
            void onTouchOutside(); //触摸了当前item外面，可在此方法内收起item
        }

        private List<String> datas;
        private TouchListener touchListener;

        LeftSwipeAdapter(List<String> datas, TouchListener touchListener) {
            this.datas = datas;
            this.touchListener = touchListener;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_swipe, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            String data = datas.get(position);
            holder.leftSwipe.quickClose();
            holder.leftSwipe.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (v instanceof LeftSwipeLayout) {
                            if (((LeftSwipeLayout) v).getState() == LeftSwipeLayout.STATE_CLOSED && touchListener != null) {
                                touchListener.onTouchOutside();
                            }
                        }
                    }
                    return false;
                }
            });
            holder.tvMain.setText(data);
            holder.tvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "点击了删除" + position, Toast.LENGTH_SHORT).show();
                    if (touchListener != null) {touchListener.onDelete();}
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            private LeftSwipeLayout leftSwipe;
            private TextView tvMain;
            private TextView tvDelete;

            Holder(View itemView) {
                super(itemView);
                leftSwipe = (LeftSwipeLayout) itemView.findViewById(R.id.left_swipe);
                tvMain = (TextView) itemView.findViewById(R.id.tv_main);
                tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            }
        }
    }
}
