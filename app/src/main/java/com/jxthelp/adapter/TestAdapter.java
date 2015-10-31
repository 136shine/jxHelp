package com.jxthelp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.sliding.Test;
import com.jxthelp.bean.Testinfo;
import java.util.List;
/**
 * Created by Administrator on 2015/10/30.
 */
public class TestAdapter extends BaseAdapter {
    private List<Testinfo> list= Test.listTest;
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(App.getContext()).inflate(R.layout.test_item,null);
            viewHolder.testName= (TextView) convertView.findViewById(R.id.test_name);
            viewHolder.testTime= (TextView) convertView.findViewById(R.id.test_time);
            viewHolder.testCurrentT= (TextView) convertView.findViewById(R.id.test_current_teacher);
            viewHolder.testRoom= (TextView) convertView.findViewById(R.id.test_room);
            viewHolder.testNumber= (TextView) convertView.findViewById(R.id.test_number);
            viewHolder.testTeacher= (TextView) convertView.findViewById(R.id.test_teacher);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.testName.setText(list.get(position).getName());
        viewHolder.testTime.setText(list.get(position).getTime());
        viewHolder.testCurrentT.setText(list.get(position).getTeacher());
        viewHolder.testRoom.setText(list.get(position).getRoom());
        viewHolder.testNumber.setText(list.get(position).getNumber());
        viewHolder.testTeacher.setText(list.get(position).getTeacher1()
                + list.get(position).getTeacher2()
                + list.get(position).getTeacher3());
        return convertView;
    }
    private class ViewHolder{
        private TextView testName;
        private TextView testTime;
        private TextView testCurrentT;
        private TextView testNumber;
        private TextView testRoom;
        private TextView testTeacher;
    }
}

