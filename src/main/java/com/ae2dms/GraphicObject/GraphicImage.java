/**
 * @author Zeyu Xiong
 */
package com.ae2dms.GraphicObject;
import javafx.scene.image.Image;

import java.util.Objects;

public class GraphicImage {
    // Design Pattern: Singleton
    private static GraphicImage graphicImage = new GraphicImage();

    private GraphicImage() {
    }

    public static GraphicImage getInstance() {
        return graphicImage;
    }

    private final Image Wall = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Wall.png")));
    private final Image Crate = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Crate.gif")));
    private final Image Crate_Zombie = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Nut.png")));
    private final Image Diamond = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Diamond.png")));
    private final Image Diamond_Zombie = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Hat.png")));
    private final Image Keeper = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Keeper.gif")));
    private final Image Keeper_Zombie = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Zombie.gif")));
    private final Image Floor = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Floor.png")));
    private final Image Crate_On_Diamond = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Crate_On_Diamond.png")));
    private final Image Crate_On_Diamond_Zombie = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Crate_On_Diamond_Zombie.png")));
    private final Image Gate = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Gate.gif")));
    private final Image Power = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Power.gif")));
    private final Image Power_Zombie = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Power_Zombie.png")));
    private final Image Lock = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Lock.png")));
    private final Image Powerful_Keeper = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Powerful_Keeper.gif")));
    private final Image Powerful_Zombie = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/Powerful_Zombie.gif")));

    /**
     * @return Image of Wall
     */
    public Image getWall(){
        return Wall;
    }

    /**
     * @return Image of Crate
     */
    public Image getCrate(){
        return Crate;
    }

    /**
     * @return Image of Crate (Zombie Mode)
     */
    public Image getCrate_Zombie(){
        return Crate_Zombie;
    }

    /**
     * @return Image of Diamond
     */
    public Image getDiamond(){
        return Diamond;
    }


    /**
     * @return Image of Diamond (Zombie Mode)
     */
    public Image getDiamond_Zombie() {
        return Diamond_Zombie;
    }


    /**
     * @return Image of Keeper
     */
    public Image getKeeper() {
        return Keeper;
    }


    /**
     * @return Image of Keeper (Zombie Mode)
     */
    public Image getKeeper_Zombie() {
        return Keeper_Zombie;
    }


    /**
     * @return Image of Floor
     */
    public Image getFloor() {
        return Floor;
    }

    /**
     * @return Image of Crate_On_Diamond
     */
    public Image getCrate_On_Diamond() {
        return Crate_On_Diamond;
    }


    /**
     * @return Image of Crate_On_Diamond (Zombie Mode)
     */
    public Image getCrate_On_Diamond_Zombie() {
        return Crate_On_Diamond_Zombie;
    }

    /**
     * @return Image of Gate
     */
    public Image getGate() {
        return Gate;
    }

    /**
     * @return Image of Locked Gate
     */
    public Image getLock() {
        return Lock;
    }

    /**
     * @return Image of Power_Plant
     */
    public Image getPower() {
        return Power;
    }

    /**
     * @return Image of Power_Zombie
     */
    public Image getPower_Zombie() {
        return Power_Zombie;
    }

    /**
     * @return Image of Powerful_Keeper
     */
    public Image getPowerful_Keeper() {
        return Powerful_Keeper;
    }

    /**
     * @return Image of Powerful_Zombie
     */
    public Image getPowerful_Zombie() {
        return Powerful_Zombie;
    }
}