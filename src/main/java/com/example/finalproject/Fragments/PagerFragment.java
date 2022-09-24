package com.example.finalproject.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.finalproject.Data.Questions;
import com.example.finalproject.R;

import java.io.Serializable;
import java.util.List;

/**
 * Fragment for View Pager in Question Book Activity
 */
public class PagerFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_PAGE="ARG_PAGE";
    private int mPage;
    private TextView tv_curIndex;
    private TextView tv_totalIndex;
    private TextView tv_que;
    private TextView tv_a;
    private TextView tv_b;
    private TextView tv_c;
    private TextView tv_d;
    private List<Questions> wrongList;
    private List<Questions> collectList;
    private int curWrong;
    private int curCollect;
    private Button btn_share;


    public static PagerFragment newInstance(int page, List<Questions> wrongList,List<Questions> collectList){
        Bundle bundle=new Bundle();
        bundle.putInt(ARG_PAGE,page);
        bundle.putSerializable("wrongList",(Serializable)wrongList);
        bundle.putSerializable("collectList",(Serializable)collectList);
        PagerFragment pageFragment=new PagerFragment();
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mPage = bundle.getInt(ARG_PAGE);
        wrongList = (List<Questions>)bundle.getSerializable("wrongList");
        collectList = (List<Questions>)bundle.getSerializable("collectList");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_page,container,false);
        tv_curIndex = view.findViewById(R.id.tv_statistics_curIndex);
        tv_totalIndex = view.findViewById(R.id.tv_statistics_total);
        tv_que = view.findViewById(R.id.tv_statistics_question);

        tv_a = view.findViewById(R.id.statistics_A);
        tv_b = view.findViewById(R.id.statistics_B);
        tv_c = view.findViewById(R.id.statistics_C);
        tv_d = view.findViewById(R.id.statistics_D);


        btn_share = view.findViewById(R.id.btn_statistics_share);
        Button btn_pre = view.findViewById(R.id.btn_statistics_pre);
        Button btn_suc = view.findViewById(R.id.btn_statistics_suc);

        btn_pre.setOnClickListener(this);
        btn_suc.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        //MySQLite mySQLite = new MySQLite()

        curCollect=0;
        curWrong=0;

        /*for(int i = 0; i < wrongList.size(); i++){
            Log.i("FFF","WrongList"+i+": "+wrongList.get(i).toString());
        }

        for(int i = 0; i < collectList.size(); i++){
            Log.i("FFF","CollectList"+i+": "+collectList.get(i).toString());
        }*/

        if(mPage == 1){
            if(wrongList.isEmpty()){
                TextView tv_titleA = view.findViewById(R.id.statistics_titleA);
                TextView tv_titleB = view.findViewById(R.id.statistics_titleB);
                TextView tv_titleC = view.findViewById(R.id.statistics_titleC);
                TextView tv_titleD = view.findViewById(R.id.statistics_titleD);
                tv_titleA.setVisibility(View.INVISIBLE);
                tv_titleB.setVisibility(View.INVISIBLE);
                tv_titleC.setVisibility(View.INVISIBLE);
                tv_titleD.setVisibility(View.INVISIBLE);
                tv_curIndex.setVisibility(View.INVISIBLE);
                tv_totalIndex.setVisibility(View.INVISIBLE);
                tv_a.setVisibility(View.INVISIBLE);
                tv_b.setVisibility(View.INVISIBLE);
                tv_c.setVisibility(View.INVISIBLE);
                tv_d.setVisibility(View.INVISIBLE);
                btn_pre.setVisibility(View.INVISIBLE);
                btn_suc.setVisibility(View.INVISIBLE);
                btn_share.setEnabled(false);

                tv_que.setText("No Wrong Questions!");
            }else {
                Questions questions = wrongList.get(curWrong);
                tv_curIndex.setText("1");
                tv_totalIndex.setText("/" + wrongList.size());
                tv_que.setText(questions.getQDescription());
                tv_a.setText(questions.getOptionA());
                tv_b.setText(questions.getOptionB());
                tv_c.setText(questions.getOptionC());
                tv_d.setText(questions.getOptionD());
                markOption(questions);
            }

        }else{
            if(collectList.isEmpty()){
                TextView tv_titleA = view.findViewById(R.id.statistics_titleA);
                TextView tv_titleB = view.findViewById(R.id.statistics_titleB);
                TextView tv_titleC = view.findViewById(R.id.statistics_titleC);
                TextView tv_titleD = view.findViewById(R.id.statistics_titleD);
                tv_titleA.setVisibility(View.INVISIBLE);
                tv_titleB.setVisibility(View.INVISIBLE);
                tv_titleC.setVisibility(View.INVISIBLE);
                tv_titleD.setVisibility(View.INVISIBLE);
                tv_curIndex.setVisibility(View.INVISIBLE);
                tv_totalIndex.setVisibility(View.INVISIBLE);
                tv_a.setVisibility(View.INVISIBLE);
                tv_b.setVisibility(View.INVISIBLE);
                tv_c.setVisibility(View.INVISIBLE);
                tv_d.setVisibility(View.INVISIBLE);
                btn_pre.setVisibility(View.INVISIBLE);
                btn_suc.setVisibility(View.INVISIBLE);
                btn_share.setEnabled(false);

                tv_que.setText("No Favorites!");
            }else {
                Questions questions = collectList.get(curCollect);
                tv_curIndex.setText("1");
                tv_totalIndex.setText("/" + collectList.size());
                tv_que.setText(questions.getQDescription());
                tv_a.setText(questions.getOptionA());
                tv_b.setText(questions.getOptionB());
                tv_c.setText(questions.getOptionC());
                tv_d.setText(questions.getOptionD());
                markOption(questions);
            }
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_statistics_pre:

                Log.i("FFF","Pre:Page:"+mPage+" curWrong:"+curWrong+" curCollect:"+curCollect);
                if(mPage == 1){

                    //List<Questions> tempList = wrongList;
                    if(curWrong > 0){

                            clearMark();
                            curWrong--;
                            Questions questions = wrongList.get(curWrong);
                            tv_que.setText(questions.getQDescription());
                            tv_a.setText(questions.getOptionA());
                            tv_b.setText(questions.getOptionB());
                            tv_c.setText(questions.getOptionC());
                            tv_d.setText(questions.getOptionD());

                            tv_curIndex.setText((curWrong+1)+"");

                            markOption(questions);

                    }else {
                            new AlertDialog.Builder(getActivity()).setTitle(" ! ! ").setMessage("It is already the first question!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                    }
                }else{

                    //List<Questions> tempList = collectList;
                    if(curCollect > 0){

                        clearMark();
                        curCollect--;
                        Questions questions = collectList.get(curCollect);
                        tv_que.setText(questions.getQDescription());
                        tv_a.setText(questions.getOptionA());
                        tv_b.setText(questions.getOptionB());
                        tv_c.setText(questions.getOptionC());
                        tv_d.setText(questions.getOptionD());

                        tv_curIndex.setText((curCollect+1)+"");

                        markOption(questions);

                    }else {
                        new AlertDialog.Builder(getActivity()).setTitle(" ! ! ").setMessage("It is already the first question!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                }

                break;

            case R.id.btn_statistics_suc:

                Log.i("FFF","SUC:Page:"+mPage+" curWrong:"+curWrong+" curCollect:"+curCollect);

                if(mPage == 1){

                    //List<Questions> tempList = wrongList;
                    if(curWrong < wrongList.size() - 1){

                        clearMark();
                        curWrong++;

                        Questions questions = wrongList.get(curWrong);
                        tv_que.setText(questions.getQDescription());
                        tv_a.setText(questions.getOptionA());
                        tv_b.setText(questions.getOptionB());
                        tv_c.setText(questions.getOptionC());
                        tv_d.setText(questions.getOptionD());

                        tv_curIndex.setText((curWrong+1)+"");

                        markOption(questions);

                    }else {
                        new AlertDialog.Builder(getActivity()).setTitle(" ! ! ").setMessage("It is already the last question!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                }else{

                    //List<Questions> tempList = collectList;
                    if(curCollect < collectList.size() -1){

                        clearMark();
                        curCollect++;
                        Questions questions = collectList.get(curCollect);
                        tv_que.setText(questions.getQDescription());
                        tv_a.setText(questions.getOptionA());
                        tv_b.setText(questions.getOptionB());
                        tv_c.setText(questions.getOptionC());
                        tv_d.setText(questions.getOptionD());

                        tv_curIndex.setText((curCollect+1)+"");

                        markOption(questions);

                    }else {
                        new AlertDialog.Builder(getActivity()).setTitle(" ! ! ").setMessage("It is already the last question!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                }

                break;
            case R.id.btn_statistics_share:
                if(mPage == 1){
                    shareText(wrongList, curWrong);
                }else{
                    shareText(collectList,curCollect);
                }

        }

    }

    private void shareText(List<Questions> list,int index) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, list.get(index).toString()+"\nDo you know the answer?");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    private void clearMark() {
        tv_a.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_b.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_c.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_d.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    //mark the right Answer and users selected answer
    private void markOption(Questions q) {

        int answer = q.getAnswer();

        switch(answer){
            case 0:
                tv_a.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            case 1:
                tv_b.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            case 2:
                tv_c.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            case 3:
                tv_d.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            default:
                break;
        }
    }
}

