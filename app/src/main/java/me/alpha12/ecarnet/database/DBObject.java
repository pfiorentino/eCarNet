package me.alpha12.ecarnet.database;

import android.provider.BaseColumns;

/**
 * Created by paul on 24/01/16.
 */
public abstract class DBObject {
    private int id;
    protected boolean selected;

    public abstract boolean persist(boolean update);
    public boolean persist() {
        return persist(false);
    }
    public void update() {
        persist(true);
    }

    public abstract boolean delete();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
