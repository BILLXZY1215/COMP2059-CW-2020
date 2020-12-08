package com.ae2dms.GraphicObject;

public class Floor extends GraphicObject {
    /**
     * This class set the image of FLOOR to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Floor(int mode){
        this.setImage(graphicImage.getFloor());
    }
}
