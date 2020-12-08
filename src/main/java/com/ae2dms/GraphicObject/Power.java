package com.ae2dms.GraphicObject;

public class Power extends GraphicObject{
    /**
     * This class set the image of Lock to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Power(int mode){
        if(mode == 0){
            this.setImage(graphicImage.getPower());
        }else{
            this.setImage(graphicImage.getPower_Zombie());
        }
    }
}
