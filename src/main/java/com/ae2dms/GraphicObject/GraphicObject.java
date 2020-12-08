/**
 * @author Zeyu Xiong
 */
package com.ae2dms.GraphicObject;
import javafx.scene.image.ImageView;

class GraphicObject extends ImageView {
    GraphicImage graphicImage;
    /**
     * This constructor generates the Image of GraphicObject
     */
    GraphicObject() {
        graphicImage = GraphicImage.getInstance();
        this.setFitHeight(30);
        this.setFitWidth(30);
    }
}
