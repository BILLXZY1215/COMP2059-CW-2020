package com.ae2dms.GraphicObject;

public class Crate_On_Diamond extends GraphicObject {
    /**
     * This class set the image of CRATE_ON_DIAMOND to the game object
     * @param mode 1 -> zombie mode; 0 -> plant mode
     */
    public Crate_On_Diamond(int mode){
        super();
        if(mode == 0){
            this.setImage(graphicImage.getCrate_On_Diamond());
        }else{
            this.setImage(graphicImage.getCrate_On_Diamond_Zombie());
        }
    }

}
