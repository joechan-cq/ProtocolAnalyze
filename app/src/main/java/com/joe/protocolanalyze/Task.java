package com.joe.protocolanalyze;

/**
 * Description
 * Created by chenqiao on 2015/8/28.
 */
public class Task {

    private int num1;

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    private int num2;

    public Task() {
    }

    public Task(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }
}
