package com.ae2dms.GraphicObject;

public class Lock extends GraphicObject{
    /**
     * This class set the image of Lock to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Lock(int mode){
        this.setImage(graphicImage.getLock());
    }
}
