package com.jxthelp.bean;

/**
 * Created by idisfkj on 15/10/8.
 * Email : idisfkj@qq.com.
 */
public class CourseInfo {
    //星期
    public int XinQi;
    //开始周
    public int Start;
    //结束周
    public int End;
    //节数
    public int ClassNumber;
    //判断单，双
    public int flag;
    //课程名
    public String courseName;
    //课程教室
    public String courseRoom;
    //课程班级
    public String courseClass;

    public int getXinQi() {
        return XinQi;
    }

    public void setXinQi(int xinQi) {
        XinQi = xinQi;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseRoom() {
        return courseRoom;
    }

    public void setCourseRoom(String courseRoom) {
        this.courseRoom = courseRoom;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }


}
