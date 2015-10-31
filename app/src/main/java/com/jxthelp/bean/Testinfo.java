package com.jxthelp.bean;


import android.widget.TextView;
/**
 * Created by Administrator on 2015/10/30.
 */
public class Testinfo {
    private String name,time,teacher,room,number,teacher1,teacher2,teacher3;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getTeacher1() {
        return teacher1;
    }
    public void setTeacher1(String teacher1) {
        this.teacher1 = teacher1;
    }
    public String getTeacher2() {
        return teacher2;
    }
    public void setTeacher2(String teacher2) {
        this.teacher2 = teacher2;
    }
    public String getTeacher3() {
        return teacher3;
    }
    public void setTeacher3(String teacher3) {
        this.teacher3 = teacher3;
    }
    @Override
    public String toString() {
        return "Testinfo{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", teacher='" + teacher + '\'' +
                ", room='" + room + '\'' +
                ", number='" + number + '\'' +
                ", teacher1='" + teacher1 + '\'' +
                ", teacher2='" + teacher2 + '\'' +
                ", teacher3='" + teacher3 + '\'' +
                '}';
    }
}
