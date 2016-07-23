package se.bitninja.thirtygame;

import android.widget.ImageButton;

public class Dice {

    private int number, face, sides;
    private boolean saved;
    private ImageButton button;

    /**
     * Construct a new Dice object.
     */
    public Dice(int sides) {
        this.setSides(sides);
        this.roll();
        this.setSaved(false);
    }

    /**
     * Roll the die
     */
    public void roll() {
        int newSide = (int)Math.floor(Math.random() * this.sides + 1);
        this.setNumber(newSide);
        this.setFace(newSide);
    }

    /**
     * Set amount of sides on the die
     * @param sides
     */
    public void setSides(int sides) { this.sides = sides; }

    /**
     * Set new face number.
     * @param number The number to represent as a face.
     */
    public void setFace(int number) {
        this.face = number - 1;
    }

    public int getFace() { return this.face; }

    public int getNumber() { return this.number; }

    public void setNumber(int number) { this.number = number; }

    public void setButton(ImageButton button) { this.button = button; }

    public ImageButton getButton() { return this.button; }

    public void setSaved(boolean saved) { this.saved = saved; }

    public boolean isSaved() { return saved; }

    /**
     * Toggles the save state on the die.
     */
    public void toggleSaved() {
        ImageButton b = this.getButton();
        if(saved){
            this.setSaved(false);
            b.setImageDrawable(DiceActivity.getFaceImage(b, DiceActivity.faceColor.WHITE, this.getNumber()));
        } else {
            this.setSaved(true);
            b.setImageDrawable(DiceActivity.getFaceImage(b, DiceActivity.faceColor.GREY, this.getNumber()));
        }
    }
}
