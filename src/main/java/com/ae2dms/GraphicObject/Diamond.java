package com.ae2dms.GraphicObject;

import com.ae2dms.GameEngine;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Diamond extends GraphicObject {
    /**
     * This class set the image of DIAMOND to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Diamond(int mode){
        if(mode == 0){
            this.setImage(graphicImage.getDiamond());
        }else{
            this.setImage(graphicImage.getDiamond_Zombie());
        }
        if (GameEngine.isDebugActive()) {
            FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
            ft.setFromValue(1.0);
            ft.setToValue(0.2);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        }
    }
}
