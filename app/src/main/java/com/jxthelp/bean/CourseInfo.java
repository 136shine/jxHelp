package com.jxthelp.bean;

/**
 * Created by idisfkj on 15/10/8.
 * Email : idisfkj@qq.com.
 */
public class CourseInfo {
    //星期
    public int XinQi;
    //课程详情
    public String CourseInfo;
    //开始周
    public int Start;
    //结束周
    public int End;
    //节数
    public int ClassNumber;
    //判断单，双
    public int flag;

    public int getXinQi() {
        return XinQi;
    }

    public void setXinQi(int xinQi) {
        XinQi = xinQi;
    }

    public String getCourseInfo() {
        return CourseInfo;
    }

    public void setCourseInfo(String courseInfo) {
        CourseInfo = courseInfo;
    }

    public int getStart() {
        return Start;
    }

    public void setStart(int start) {
        Start = start;
    }

    public int getEnd() {
        return End;
    }

    public void setEnd(int end) {
        End = end;
    }

    public int getClassNumber() {
        return ClassNumber;
    }

    public void setClassNumber(int classNumber) {
        ClassNumber = classNumber;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
