package com.ae2dms.GraphicObject;

public class Gate extends GraphicObject  {
    /**
     * This class set the image of Gate to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Gate(int mode){
        this.setImage(graphicImage.getGate());
    }
}
