package com.ae2dms.GraphicObject;


public class Crate extends GraphicObject{
    /**
     * This class set the image of CRATE to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Crate(int mode){
        if(mode == 0){
            this.setImage(graphicImage.getCrate());
        }else {
            this.setImage(graphicImage.getCrate_Zombie());
        }

    }
}
