package me.alpha12.ecarnet.database;

import android.provider.BaseColumns;

/**
 * Created by paul on 24/01/16.
 */
public abstract class DBObject {
    private int id;
    protected boolean selected;

    public abstract void persist(boolean update);
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
