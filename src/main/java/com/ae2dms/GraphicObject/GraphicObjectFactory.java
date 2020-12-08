/**
 * @author Zeyu Xiong
 */

package com.ae2dms.GraphicObject;
import com.ae2dms.GraphicObject.*;
import javafx.scene.image.ImageView;

/**
 * This class is a factory of gameGrid, the aim is to let user not knowing the exact constructor of GraphicObject
 */
public class GraphicObjectFactory extends ImageView {

    public GraphicObjectFactory() {
    }

    public Wall getWall(int mode) {
        return new Wall(mode);
    }
    public Crate getCrate(int mode) {
        return new Crate(mode);
    }
    public Diamond getDiamond(int mode) {
        return new Diamond(mode);
    }
    public Floor getFloor(int mode) {
        return new Floor(mode);
    }
    public Keeper getKeeper(int mode) {
        return new Keeper(mode);
    }
    public Crate_On_Diamond getCrate_On_Diamond(int mode) {
        return new Crate_On_Diamond(mode);
    }
    public Gate getGate(int mode){
        return new Gate(mode);
    }
    public Lock getLock(int mode){
        return new Lock(mode);
    }
    public Power getPower(int mode){
        return new Power(mode);
    }

}


