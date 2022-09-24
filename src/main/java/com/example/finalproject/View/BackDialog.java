package com.example.finalproject.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finalproject.R;

/**
 * Customized Back Dialog in Mission Activity
 * @author Mengru.Ji
 */
public class BackDialog extends Dialog {
    /**
     * Context
     */
    private Context context;
    /**
     * Listener
     */
    private BackDialog.BackDialogClickListener clickListener;
    /**
     * Continue Button
     */
    private Button btn_bD_continue;
    private TextView tv_bD_title;
    /**
     * Back Button
     */
    private Button btn_bD_back;
    private TextView tv_bD_description;


    /**
     * Interface Listener of this Dialog
     */
    public interface BackDialogClickListener{

        void goBack();
        void proceed();
    }

    /**
     * Constructor
     * @param context
     */
    public BackDialog(@NonNull Context context) {

        super(context);
        this.context = context;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initView();
    }

    /**
     * initialize View
     */
    private void initView() {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_back_mission,null);
        setContentView(view);

        btn_bD_continue = view.findViewById(R.id.btn_backDialog_continue);
        btn_bD_back = view.findViewById(R.id.btn_backDialog_back);
        tv_bD_title = view.findViewById(R.id.tv_backDialog_title);
        tv_bD_description = view.findViewById(R.id.tv_backDialog_description);

        btn_bD_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.goBack();
            }
        });
        btn_bD_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.proceed();
            }
        });


    }

    /**
     * Set Dialog Listener
     * @param clickListener
     */
    public void setBackDialogClickListner(BackDialog.BackDialogClickListener clickListener){
        this.clickListener = clickListener;
    }
}
