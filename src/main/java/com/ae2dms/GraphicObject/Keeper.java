package com.ae2dms.GraphicObject;

import com.ae2dms.GameEngine;

public class Keeper extends GraphicObject {
    /**
     * This class set the image of KEEPER to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Keeper(int mode){
        if(mode == 0){
            if(GameEngine.isPower()){
                this.setImage(graphicImage.getPowerful_Keeper());
            }else{
                this.setImage(graphicImage.getKeeper());
            }
        }else{
            if(GameEngine.isPower()){
                this.setImage(graphicImage.getPowerful_Zombie());
            }else{
                this.setImage(graphicImage.getKeeper_Zombie());
            }
        }
    }
}
