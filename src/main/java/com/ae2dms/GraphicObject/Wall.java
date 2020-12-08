package com.ae2dms.GraphicObject;


public class Wall extends GraphicObject {
    /**
     * This class set the image of WALL to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Wall(int mode){
        this.setImage(graphicImage.getWall());
    }
}
