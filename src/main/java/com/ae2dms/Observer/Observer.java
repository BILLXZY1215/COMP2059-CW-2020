package com.ae2dms.Observer;

public abstract class Observer {
    public String step;
    public int power;
    protected Subject subject;
    public abstract void update();
}