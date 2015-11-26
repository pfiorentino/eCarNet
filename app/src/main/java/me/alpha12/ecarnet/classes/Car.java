package me.alpha12.ecarnet.classes;

/**
 * Created by paul on 02/10/2015.
 */
public class Car {
    public int uuid;
    public String plateNum;
    public Model model;

    public Car(int uuid, String plateNum, Model model) {
        this.uuid = uuid;
        this.plateNum = plateNum;
        this.model = model;
    }
}
