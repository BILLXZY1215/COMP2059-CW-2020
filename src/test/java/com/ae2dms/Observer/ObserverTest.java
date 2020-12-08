package com.ae2dms.Observer;

import com.ae2dms.GraphicObject.Power;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {
    Subject subject;
    LevelStepObserver levelStepObserver;
    PowerObserver powerObserver;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setPowerState(100);
        subject.setStepState(200);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getStepState() {
        Assertions.assertEquals(200, subject.getStepState());
    }

    @Test
    void setStepState() {
        subject.setStepState(1);
        Assertions.assertEquals(1, subject.getStepState());
    }

    @Test
    void getPowerState() {
        Assertions.assertEquals(100, subject.getPowerState());
    }

    @Test
    void setPowerState() {
        subject.setStepState(2);
        Assertions.assertEquals(2, subject.getStepState());
    }

    @Test
    void attach() {
        // In LevelStepObserver's constructor and PowerObserver's constructor --> call attach()
        levelStepObserver = new LevelStepObserver(subject);
        powerObserver = new PowerObserver(subject);
        Assertions.assertEquals(2,subject.getObservers().size());
    }

    @Test
    void notifyAllObservers() {
        levelStepObserver = new LevelStepObserver(subject);
        powerObserver = new PowerObserver(subject);
        subject.setStepState(900);
        subject.setPowerState(800);
        Assertions.assertEquals("900", levelStepObserver.getStep());
        Assertions.assertEquals(800, powerObserver.getPower());
    }
}