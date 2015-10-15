package com.jxthelp.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.bean.CourseInfo;
import com.jxthelp.bean.XueQi;
import com.jxthelp.ui.CourseDetail;
import com.jxthelp.ui.LoginActivity;
import com.jxthelp.ui.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15-9-17 12:19.
 * Email: idisfkj@qq.com
 */
public class FragmentKC extends Fragment {

    @InjectView(R.id.spinner)
    Spinner spinner;
    @InjectView(R.id.empty)
    TextView empty;
    @InjectView(R.id.mon)
    TextView mon;
    @InjectView(R.id.tue)
    TextView tue;
    @InjectView(R.id.wen)
    TextView wen;
    @InjectView(R.id.thu)
    TextView thu;
    @InjectView(R.id.fri)
    TextView fri;
    @InjectView(R.id.sta)
    TextView sta;
    @InjectView(R.id.sun)
    TextView sun;
    @InjectView(R.id.course_content)
    RelativeLayout courseContent;
    @InjectView(R.id.course_kb)
    RelativeLayout courseKb;
    @InjectView(R.id.extra_content)
    RelativeLayout extraContent;
    @InjectView(R.id.ts_tv)
    TextView ts;


    private int averWidth;
    private int averHeight;
    private int width;
    private int height;
    private int zhouShu;
    private String[] zhou = new String[25];
    private XueQi xueQi = LoginActivity.xueQi;
    private Context mContext = App.getContext();

    private List<CourseInfo> list;

    public FragmentKC() {
        list = LoginActivity.listCourse;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_kc, null);
        ButterKnife.inject(this, view);

        width = MainActivity.width;
        height = MainActivity.height;
        averWidth = width / 5;
        averHeight = height / 10;

        empty.setWidth(averWidth * 1 / 2);
        mon.setWidth(averWidth);
        tue.setWidth(averWidth);
        wen.setWidth(averWidth);
        thu.setWidth(averWidth);
        fri.setWidth(averWidth);
        sta.setWidth(averWidth);
        sun.setWidth(averWidth);

        zhouShu = getCurrentZhou();

        Zhou();
        setExcell();
        if ((zhouShu) % 2 == 1) {
            empty.setText("单");
        } else {
            empty.setText("双");
        }
        empty.setTextSize(18);
        empty.setGravity(Gravity.CENTER);
        empty.setTextColor(getResources().getColor(R.color.text_xiqi));

        spinner.setSelection(zhouShu - 1, true);
//        getData(week-startWeek);


        return view;
    }

    /**
     * 画8*12表格
     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setExcell() {
        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 8; j++) {
                TextView textView = new TextView(mContext);
                textView.setId((i - 1) * 8 + j);
                if (j < 8) {
                    textView.setBackground(getResources().getDrawable(R.drawable.course_bg));
                } else {
                    //最后一列右边无边界线条
                    textView.setBackground(getResources().getDrawable(R.drawable.course_last));
                }
                RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(averWidth, averHeight);
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(mContext, R.style.CoursetText);
                if (j == 1) {
                    textView.setText(String.valueOf(i));
                    textView.setTextColor(getResources().getColor(R.color.text_jieshu));
                    rl.width = averWidth * 1 / 2;
                    if (i == 1) {
                        rl.addRule(RelativeLayout.BELOW, empty.getId());
                    } else {
                        rl.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                    }
                } else {
                    rl.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
                    rl.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
                    textView.setText("");
                }
                textView.setLayoutParams(rl);
                courseContent.addView(textView);
            }
        }
    }

    public void getData(int position) {
        if ((position + 1) % 2 == 1) {
            empty.setText("单");
        } else {
            empty.setText("双");
        }
        for (int i = 0; i < list.size(); i++) {
            final int t = i;
            //还需上的课
            if (list.get(i).getEnd() >= (position + 1)) {
                Button tv = new Button(getActivity());
                RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(averWidth * 31 / 32, (averHeight - 2) * 2);
                //设置左，上边距
                rl.topMargin = 2 + ((list.get(i).getClassNumber() - 1) * averHeight);
                rl.leftMargin = 2 + (averWidth * 1 / 2) + (list.get(i).getXinQi() - 1) * averWidth;

                tv.setTextSize(12);
                tv.setTextColor(Color.WHITE);
                tv.getBackground().setAlpha(222);
                tv.setLayoutParams(rl);
                if (list.get(i).getStart() <= (position + 1)) {
                    int[] backGround = {R.drawable.course_blue_bg, R.drawable.course_green_bg, R.drawable.course_pink_bg, R.drawable.course_purple_bg, R.drawable.course_red_bg, R.drawable.course_yellow_bg};

                    if (list.get(i).getFlag() == (position + 1) % 2 || list.get(i).getFlag() == -1) {
                        tv.setText(list.get(i).getCourseName() + "  " + list.get(i).getCourseRoom());
                        tv.setBackgroundResource(backGround[i % 6]);
//                    courseKb.setGravity(Gravity.CENTER);
                    } else {
                        tv.setText("[非本周]" + "  " + list.get(i).getCourseName() + "  " + list.get(i).getCourseRoom());
                        tv.setBackgroundResource(R.drawable.course_no_bg);
                    }

                } else{
                    tv.setText("[非本周]" + "  " + list.get(i).getCourseName() + "  " + list.get(i).getCourseRoom());
                    tv.setBackgroundResource(R.drawable.course_no_bg);
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.getContext(), CourseDetail.class);
                        intent.putExtra("courseName", list.get(t).getCourseName());
                        intent.putExtra("courseRoom", list.get(t).getCourseRoom());
                        intent.putExtra("courseZhou", list.get(t).getStart() + "-" + list.get(t).getEnd());
                        intent.putExtra("courseClass", list.get(t).getCourseClass());
                        intent.putExtra("courseJS", String.valueOf(list.get(t).getClassNumber()));
                        intent.putExtra("courseXQ", String.valueOf(list.get(t).getXinQi()));
                        intent.putExtra("Flag", String.valueOf(list.get(t).getFlag()));
                        startActivity(intent);
                    }
                });
                courseKb.addView(tv);
            }
        }
    }

    /**
     * Spinner
     */
    public void Zhou() {
        for (int i = 0; i < 25; i++) {
            zhou[i] = (i + 1) + "周";
        }
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return zhou.length;
            }

            @Override
            public Object getItem(int position) {
                return zhou[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item, null);
                    viewHolder.sTv = (TextView) convertView.findViewById(R.id.spinner_tv);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.sTv.setText(zhou[position]);
                return convertView;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseKb.removeAllViews();
                getData(i);
                if (zhouShu != (i + 1)) {
                    ts.setVisibility(View.VISIBLE);
                } else {
                    ts.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public class ViewHolder {
        private TextView sTv;

    }

    /**
     * 获取当前周
     *
     * @return 当前周数
     */
    public int getCurrentZhou() {
        int Zs = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        try {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置已星期一为一周的开始，默认为sunday
            calendar1.setFirstDayOfWeek(Calendar.MONDAY);
            System.out.println(year + "-" + (month + 1) + "-" + day);
            //month+1 可能是API默认原因月份要＋1
            calendar.setTime(sdf.parse(year + "-" + (month + 1) + "-" + day));
            //判断学期
            if (Integer.parseInt(xueQi.getXq()) == 1) {
                //上半学期开学时间
                calendar1.setTime(sdf.parse(xueQi.getXnUp() + "-9-6"));
            } else {
                //待确定下半学期开学时间
                calendar1.setTime(sdf.parse(xueQi.getXnDown() + "-9-6"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int startWeek = calendar1.get(Calendar.WEEK_OF_YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        //是否是跨年
        if (week < startWeek) {
            for (int i = 0; i < 8; i++) {
                try {
                    calendar.setTime(sdf.parse(xueQi.getXnUp() + "-12-" + (31 - i)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int total = calendar.get(Calendar.WEEK_OF_YEAR);
                //获得一年的总周数
                if (total > 1) {
                    i = 8;
                    //获取目前的周数
                    Zs = total - startWeek + week;
                }
            }
        } else {
            //获取目前的周数
            Zs = week - startWeek;
        }
        return Zs;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
