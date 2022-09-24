package com.example.finalproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.finalproject.Activities.MissionActivity;
import com.example.finalproject.Activities.MissionResultActivity;
import com.example.finalproject.R;
import com.example.finalproject.View.NoScrollGridView;

/**
 * Please ignore this Class
 */
public class AnswerCardFragment extends Fragment {

    LocalBroadcastManager localBroadcastManager;

    public AnswerCardFragment(){

    }
    int count = MissionActivity.randomList.size();
    int[] ids = new int[count];
    int rightCount = MissionActivity.rightCount;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        initData();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());

        View root = inflater.inflate(R.layout.pager_content_answercard,container,false);
        NoScrollGridView gridView = root.findViewById(R.id.gridview);
        TextView tv_submitAndResult = root.findViewById(R.id.tv_submit_result);

        Log.i("QQQ",count+"");

        tv_submitAndResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MissionResultActivity.class);
                intent.putExtra("rightCount_final",rightCount);
                startActivity(intent);

            }
        });

        GridViewAdapter myAdapter = new GridViewAdapter(getActivity());
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("com.finalProject.jumpToPage");
                intent.putExtra("index",position);
                localBroadcastManager.sendBroadcast(intent);
            }
        });

        return root;
    }

    private void initData() {

        for(int i = 0; i < count; i++){
            ids[i] = i+1;
        }
    }

    private class GridViewAdapter extends BaseAdapter{

        private Context mContext;

        public GridViewAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object getItem(int position) {
            return ids[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView =new TextView(mContext);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new GridView.LayoutParams(65,65));
            textView.setPadding(5,5,5,5);

            textView.setText(ids[position]+"");
            textView.setBackgroundResource(R.drawable.icon_circle);
            return textView;
        }
    }
}
