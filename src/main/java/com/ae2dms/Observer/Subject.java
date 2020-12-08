package com.ae2dms.Observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private List<Observer> observers = new ArrayList<Observer>();
    private int stepState;
    private int powerState;

    /**
     * @return the state of step
     */
    public int getStepState() {
        return stepState;
    }

    /**
     * @param stepState Set the state of step
     */
    public void setStepState(int stepState) {
        this.stepState = stepState;
        notifyAllObservers();
    }

    /**
     * @return the state of power
     */
    public int getPowerState() {
        return powerState;
    }

    /**
     * @param powerState Set the state of power
     */
    public void setPowerState(int powerState) {
        this.powerState = powerState;
        notifyAllObservers();
    }

    /**
     * @param observer add observer attached to subject
     */
    public void attach(Observer observer){
        observers.add(observer);
    }

    /**
     * This method notifies all attached observers to update
     */
    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * This method gets the list of observer
     */
    public List<Observer> getObservers() {
        return observers;
    }


}