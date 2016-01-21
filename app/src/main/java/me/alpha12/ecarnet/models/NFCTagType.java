package me.alpha12.ecarnet.models;

public class NFCTagType {
    private String label;
    private String mimeType;
    private Integer icon;
    private boolean selected;

    public NFCTagType(String label, String mimeType, Integer icon) {
        this.label      = label;
        this.mimeType   = mimeType;
        this.icon       = icon;
    }

    public Integer getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
