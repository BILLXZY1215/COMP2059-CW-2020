package com.ae2dms.Observer;

public class PowerObserver extends Observer{
    public PowerObserver(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
    }

    /**
     * This Method updates the state of power
     */
    @Override
    public void update() {
        power = subject.getPowerState();
    }

    /**
     * This Method returns the step
     */
    public int getPower(){
        return power;
    }
}
