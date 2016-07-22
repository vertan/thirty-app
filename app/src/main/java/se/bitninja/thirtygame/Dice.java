package se.bitninja.thirtygame;

import android.widget.ImageButton;

public class Dice {

    // TODO Move graphical representation out of this class?
    int[] whiteFaces = {
            R.drawable.white1,
            R.drawable.white2,
            R.drawable.white3,
            R.drawable.white4,
            R.drawable.white5,
            R.drawable.white6
    };
    int[] greyFaces = {
            R.drawable.grey1,
            R.drawable.grey2,
            R.drawable.grey3,
            R.drawable.grey4,
            R.drawable.grey5,
            R.drawable.grey6
    };
    int[] redFaces = {
            R.drawable.red1,
            R.drawable.red2,
            R.drawable.red3,
            R.drawable.red4,
            R.drawable.red5,
            R.drawable.red6
    };

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
        if(saved){
            this.setSaved(false);
            this.getButton().setImageResource(whiteFaces[this.getFace()]);
        } else {
            this.setSaved(true);
            this.getButton().setImageResource(greyFaces[this.getFace()]);
        }
    }
}
