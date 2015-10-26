package me.alpha12.ecarnet.model;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Operation {

    private int id;
    private String name;
    private String type;
    private String piece;
    private int kilometersCycle;
    private int dateCycle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public int getKilometersCycle() {
        return kilometersCycle;
    }

    public void setKilometersCycle(int kilometersCycle) {
        this.kilometersCycle = kilometersCycle;
    }

    public int getDateCycle() {
        return dateCycle;
    }

    public void setDateCycle(int dateCycle) {
        this.dateCycle = dateCycle;
    }
}
