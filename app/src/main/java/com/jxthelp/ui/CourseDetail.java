package com.jxthelp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jxthelp.R;
import com.jxthelp.swipeback.SwipeBackActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15/10/13.
 * Email : idisfkj@qq.com.
 */
public class CourseDetail extends SwipeBackActivity {
    @InjectView(R.id.courseName)
    TextView courseName;
    @InjectView(R.id.courseRoom)
    TextView courseRoom;
    @InjectView(R.id.courseZhou)
    TextView courseZhou;
    @InjectView(R.id.courseClass)
    TextView courseClass;
    @InjectView(R.id.kc_back)
    ImageButton kc_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail);
        ButterKnife.inject(this);
        Intent intent=getIntent();
        courseName.setText(intent.getStringExtra("courseName"));
        courseRoom.setText(intent.getStringExtra("courseRoom"));
        String courseJS=intent.getStringExtra("courseJS");
        int courseXQ=Integer.parseInt(intent.getStringExtra("courseXQ"));
        String XQ=null;
        switch (courseXQ){
            case 1:XQ="一";break;
            case 2:XQ="二";break;
            case 3:XQ="三";break;
            case 4:XQ="四";break;
            case 5:XQ="五";break;
            case 6:XQ="六";break;
            case 7:XQ="日";break;
        }
        if(Integer.parseInt(intent.getStringExtra("Flag"))==-1) {
            courseZhou.setText("第" + intent.getStringExtra("courseZhou") + "周" + " " + "周" + XQ + courseJS + "-" +
                    String.valueOf(Integer.parseInt(courseJS) + 1) + "节");
        }else if(Integer.parseInt(intent.getStringExtra("Flag"))==1){
            courseZhou.setText("第" + intent.getStringExtra("courseZhou") + "周" + " " +"(单周)"+ "周" + XQ + courseJS + "-" +
                    String.valueOf(Integer.parseInt(courseJS) + 1) + "节");
        }else {
            courseZhou.setText("第" + intent.getStringExtra("courseZhou") + "周" + " " +"(双周)"+ "周" + XQ + courseJS + "-" +
                    String.valueOf(Integer.parseInt(courseJS) + 1) + "节");
        }
        courseClass.setText(intent.getStringExtra("courseClass"));
        kc_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
