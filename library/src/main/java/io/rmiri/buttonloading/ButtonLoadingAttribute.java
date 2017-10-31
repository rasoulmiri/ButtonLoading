package io.rmiri.buttonloading;

/**
 * Created by Rasoul Miri on 8/20/17.
 */

public class ButtonLoadingAttribute {


    private boolean isEnable;

    private int idParent;

    private String font;

    private String text;

    private int textColor;

    private int backgroundColor;
    private int circleColor;
    private int circleColorSecond;

    private int textSize;

    private int stateShow;
    static final int STATE_NORMAL = 1;
    static final int STATE_ANIMATION_START = 2;
    static final int STATE_PROGRESS = 3;
    static final int STATE_ANIMATION_FINISH = 4;


    //==============================================================================================
    /* getter and setter */

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getCircleColorSecond() {
        return circleColorSecond;
    }

    public void setCircleColorSecond(int circleColorSecond) {
        this.circleColorSecond = circleColorSecond;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getStateShow() {
        return stateShow;
    }

    public void setStateShow(int stateShow) {
        this.stateShow = stateShow;
    }
}
