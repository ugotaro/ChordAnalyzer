package com.skworks.harm.Harmony;

/**
 * Created by kawanoshogo on 2013/11/22.
 */

public class Chord {
    public enum RootNote {
        None,
        C,
        CSharp,
        D,
        DSharp,
        E,
        F,
        FSharp,
        G,
        GSharp,
        A,
        ASharp,
        B
    }

    public enum Third {
        None,
        Major,
        Minor,
        Sus4
    }

    public enum Fifth {
        None,
        Perfect,
        Diminished,
        Augmented,
        Flatted
    }

    public enum Sixth {
        None,
        Major,
        Minor
    }

    public enum Seventh {
        None,
        Major,
        Minor
    }

    public enum Tension {
        None,
        Major,
        Minor,
        AddMajor,
        AddMinor,
        Perfect,
        Diminished,
        Augmented
    }

    private RootNote rootNote; //基音の音
    private boolean isFlatted; //基音はフラット表記か
    Third thirdNote = Third.None;
    Fifth fifthNote = Fifth.None;
    Sixth sixthNote = Sixth.None;
    Seventh seventhNote = Seventh.None;
    Tension ninthNote = Tension.None;
    Tension eleventhNote = Tension.None;
    Tension thirteenthNote = Tension.None;

    public Chord() {
    }

    public Chord(RootNote root, boolean flatted, Third third, Fifth fifth) {
        rootNote = root;
        isFlatted = flatted;
        thirdNote = third;
        fifthNote = fifth;
    }

    public void SetRoot(RootNote root) {
        rootNote = root;
    }

    public RootNote GetRoot() {
        return rootNote;
    }

    public void SetThird(Third third) {
        thirdNote = third;
    }

    public Third GetThird() {
        return thirdNote;
    }

    public void SetFifth(Fifth fifth) {
        fifthNote = fifth;
    }

    public Fifth GetFifth() {
        return fifthNote;
    }

    public void SetSixth(Sixth sixth) {
        sixthNote = sixth;
    }

    public Sixth GetSixth() {
        return sixthNote;
    }

    public void SetSeventh(Seventh seventh) {
        seventhNote = seventh;
    }

    public Seventh GetSeventh() {
        return seventhNote;
    }

    public void SetNinth(Tension ninth) {
        ninthNote = ninth;
    }

    public Tension GetNinth() {
        return ninthNote;
    }

    public void SetEleventh(Tension eleventh) {
        eleventhNote = eleventh;
    }

    public Tension GetEleventh() {
        return eleventhNote;
    }

    public void SetThirteenth(Tension thirteenth) {
        thirteenthNote = thirteenth;
    }

    public Tension GetThirteenth() {
        return thirteenthNote;
    }

    public boolean DrawSeven() {
        if (seventhNote == Seventh.None) return false;

        if (seventhNote != Seventh.Minor) return true;

        if (thirteenthNote == Tension.Major &&
                ninthNote == Tension.Major &&
                eleventhNote == Tension.Perfect) return false;

        if (ninthNote == Tension.Major) {
            if (eleventhNote == Tension.None && thirteenthNote == Tension.None) return false;
            else if (eleventhNote == Tension.None && thirteenthNote == Tension.Major) return false;
            else if (eleventhNote == Tension.Perfect && thirteenthNote == Tension.None)
                return false;
        }

        if (eleventhNote == Tension.Perfect) {
            if (ninthNote == Tension.None && thirteenthNote == Tension.None) return false;
            else if (ninthNote == Tension.None && thirteenthNote == Tension.Major) return false;
            else if (ninthNote == Tension.Major && thirteenthNote == Tension.None) return false;
        }

        if (thirteenthNote == Tension.Major) {
            if (ninthNote == Tension.None && eleventhNote == Tension.None) return false;
            else if (ninthNote == Tension.None && eleventhNote == Tension.Perfect) return false;
            else if (ninthNote == Tension.Major && thirteenthNote == Tension.None) return false;
        }

        return true;
    }


}
