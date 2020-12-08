package com.ae2dms.Observer;

public class LevelStepObserver extends Observer {
    public LevelStepObserver(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
    }

    /**
     * This Method updates the state of steps
     */
    @Override
    public void update() {
        step =  Integer.toString(subject.getStepState());
    }

    /**
     * This Method returns the step
     */
    public String getStep(){
        return step;
    }
}
