package com.jxthelp.bean;

/**
 * Created by Administrator on 2015/10/29.
 */
public class PersonalInfo {
    private String number;
    private String name;
    private String sex;
    private String identity;
    private String birthday;
    private String zhengzhimianmao;
    private String school;
    private String xueli;
    private String degree;
    private String zhicheng;
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getZhengzhimianmao() {
        return zhengzhimianmao;
    }
    public void setZhengzhimianmao(String zhengzhimianmao) {
        this.zhengzhimianmao = zhengzhimianmao;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getXueli() {
        return xueli;
    }
    public void setXueli(String xueli) {
        this.xueli = xueli;
    }
    public String getDegree() {
        return degree;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    public String getZhicheng() {
        return zhicheng;
    }
    public void setZhicheng(String zhicheng) {
        this.zhicheng = zhicheng;
    }
    @Override
    public String toString() {
        return "PersonalInfo{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", identity='" + identity + '\'' +
                ", birthday='" + birthday + '\'' +
                ", zhengzhimianmao='" + zhengzhimianmao + '\'' +
                ", school='" + school + '\'' +
                ", xueli='" + xueli + '\'' +
                ", degree='" + degree + '\'' +
                ", zhicheng='" + zhicheng + '\'' +
                '}';
    }
}

