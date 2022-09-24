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
 * Customized Timer Dialog in Mission Activity
 */
public class TimerDialog extends Dialog {

    /**
     * context
     */
    private Context context;
    /**
     * Listener
     */
    private ClickListener clickListener;
    /**
     * Continue Button
     */
    private Button btn_tD_continue;
    private TextView tv_tD_title;

    /**
     * Interface Listener
     */
    public interface ClickListener{

        void proceed();
    }

    /**
     * Constructor
     * @param context
     */
    public TimerDialog(@NonNull Context context) {
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
        View view = inflater.inflate(R.layout.dialog_countdown_timer_mission,null);
        setContentView(view);

        btn_tD_continue = view.findViewById(R.id.btn_timer_dialog_continue);
        tv_tD_title = view.findViewById(R.id.tv_timer_dialog_title);

        btn_tD_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.proceed();
            }
        });


    }

    /**
     * Set Click Listener
     * @param clickListener
     */
    public void setClickListner(ClickListener clickListener){
        this.clickListener = clickListener;
    }



}
