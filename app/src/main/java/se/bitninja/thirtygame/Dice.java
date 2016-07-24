package se.bitninja.thirtygame;

import android.os.Parcel;
import android.os.Parcelable;

public class Dice implements Parcelable {

    private int number, face, sides;
    private boolean saved, disabled;
    private String ID;

    /**
     * Construct a new Dice object.
     */
    public Dice(int sides) {
        this.setSides(sides);
        this.roll();
        this.setSaved(false);
        this.setDisabled(false);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeInt(face);
        dest.writeInt(sides);
        dest.writeInt(saved ? 1 : 0);
        dest.writeInt(disabled ? 1 : 0);
        dest.writeString(ID);
    }

    // Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };

    // De-parcel object
    public Dice(Parcel in) {
        number = in.readInt();
        face = in.readInt();
        sides = in.readInt();
        saved = in.readInt() != 0;
        ID = in.readString();
        disabled = in.readInt() != 0;
    }


    /**
     * Roll the die
     */
    public void roll() {
        int newSide = (int)Math.floor(Math.random() * this.sides + 1);
        this.setNumber(newSide);
        this.setFace(newSide);
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return this.ID;
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

    public void setSaved(boolean saved) { this.saved = saved; }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSaved() { return saved; }

    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Toggles the save state on the die.
     */
    public void toggleSaved() {
        //ImageButton b = this.getButton();
        if(saved){
            this.setSaved(false);
        } else {
            this.setSaved(true);
        }
    }
}
