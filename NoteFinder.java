package com.skworks.harm.Harmony;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kawanoshogo on 2013/11/22.
 */
public class NoteFinder {
    public String MidiNoteNumberToNote(int note) {
        int octave = note / 12 - 1;
        int noteInOct = note % 12;
        switch (noteInOct) {
            case 0:
                return String.format("%dC", octave);
            case 1:
                return String.format("%dC#", octave);
            case 2:
                return String.format("%dD", octave);
            case 3:
                return String.format("%dD#", octave);
            case 4:
                return String.format("%dE", octave);
            case 5:
                return String.format("%dF", octave);
            case 6:
                return String.format("%dF#", octave);
            case 7:
                return String.format("%dG", octave);
            case 8:
                return String.format("%dG#", octave);
            case 9:
                return String.format("%dA", octave);
            case 10:
                return String.format("%dA#", octave);
            case 11:
                return String.format("%dB", octave);
        }
        return "N";
    }

    public Chord.RootNote MidiNoteNumberToRootNote(int note) {
        int noteInOct = note % 12;
        Chord.RootNote[] values = Chord.RootNote.values();
        return values[noteInOct + 1];
    }

    public Chord[] FindHarmony(int[] notes) {
        int noteNumber = notes.length;
        int codeNumber = 5;
        Chord[] answer = new Chord[codeNumber]; //It answers five chords.

        if (noteNumber < 1) {
            codeNumber = 1;
            answer[0] = new Chord();
            answer[0].SetRoot(Chord.RootNote.None);
         } else if (noteNumber == 1) {
            //基音を探す Find a root
            //音が一個ならそれ If the note number is one, the root is it.
            System.out.printf("Root:%d¥n", notes[0]);
            answer[0] = new Chord();
            answer[0].SetRoot(MidiNoteNumberToRootNote(notes[0]));
        } else if (noteNumber == 2) {
            codeNumber = 2;
            answer[0] = new Chord();
            answer[1] = new Chord();
            //音が二個なら低い方を基音、ただし四度なら逆転する
            //If the note number is two, lower is root. However, the interval is 4th, root is upper one.
            int lower = notes[0];
            int higher = notes[1];

            if (lower > higher) {
                int temp = lower;
                lower = higher;
                higher = temp;
            }

            int interval = (higher - lower) % 12;
            System.out.printf("Root:%d¥n", lower);
            answer[0].SetRoot(MidiNoteNumberToRootNote(lower));

            switch (interval) {
                case 0:
                    break;
                case 1:
                    answer[0].SetNinth(Chord.Tension.Minor);
                    break;
                case 2:
                    answer[0].SetNinth(Chord.Tension.Major);
                    break;
                case 3:
                    answer[0].SetThird(Chord.Third.Minor);
                    break;
                case 4:
                    answer[0].SetThird(Chord.Third.Major);
                    break;
                case 5:
                    answer[0].SetRoot(MidiNoteNumberToRootNote(higher));
                    break;
                case 6:
                    answer[0].SetFifth(Chord.Fifth.Diminished);
                    break;
                case 7:
                    break;
                case 8:
                    answer[0].SetRoot(MidiNoteNumberToRootNote(higher));
                    answer[0].SetThird(Chord.Third.Major);
                    break;
                case 9:
                    answer[0].SetRoot(MidiNoteNumberToRootNote(higher));
                    answer[0].SetThird(Chord.Third.Minor);
                    break;
                case 10:
                    answer[0].SetThird(Chord.Third.Major);
                    answer[0].SetSeventh(Chord.Seventh.Minor);
                    break;
                case 11:
                    answer[0].SetThird(Chord.Third.Major);
                    answer[0].SetSeventh(Chord.Seventh.Major);
                    break;
            }

            interval = (lower - higher) % 12;
            System.out.printf("Root:%d¥n", lower);
            answer[1].SetRoot(MidiNoteNumberToRootNote(lower));

            switch (interval) {
                case 0:
                    break;
                case 1:
                    answer[1].SetNinth(Chord.Tension.Minor);
                    break;
                case 2:
                    answer[1].SetNinth(Chord.Tension.Major);
                    break;
                case 3:
                    answer[1].SetThird(Chord.Third.Minor);
                    break;
                case 4:
                    answer[1].SetThird(Chord.Third.Major);
                    break;
                case 5:
                    answer[1].SetRoot(MidiNoteNumberToRootNote(higher));
                    break;
                case 6:
                    answer[1].SetFifth(Chord.Fifth.Diminished);
                    break;
                case 7:
                    break;
                case 8:
                    answer[1].SetRoot(MidiNoteNumberToRootNote(higher));
                    answer[1].SetThird(Chord.Third.Major);
                    break;
                case 9:
                    answer[1].SetRoot(MidiNoteNumberToRootNote(higher));
                    answer[1].SetThird(Chord.Third.Minor);
                    break;
                case 10:
                    answer[1].SetThird(Chord.Third.Major);
                    answer[1].SetSeventh(Chord.Seventh.Minor);
                    break;
                case 11:
                    answer[1].SetThird(Chord.Third.Major);
                    answer[1].SetSeventh(Chord.Seventh.Major);
                    break;
            }
        } else {
            int[] intervalMatrix = new int[noteNumber * noteNumber];
            int[] octaveMatrix = new int[noteNumber * noteNumber];

            for (int m = 0; m < noteNumber; m++) {
                for (int n = 0; n < noteNumber; n++) {
                    int temp = (notes[m] - notes[n]) % 12;
                    if (temp < 0)
                        temp += 12;
                    intervalMatrix[m * noteNumber + n] = temp;
                    octaveMatrix[m * noteNumber + n] = (notes[m] - notes[n]) / 12;
                }
            }

            int[] evaluation = new int[noteNumber];

            int lowest = 1000;
            int lowestPos = 0;
            for (int m = 0; m < noteNumber; m++) {
                if (notes[m] < lowest) {
                    lowest = notes[m];
                    lowestPos = m;
                }
            }
            evaluation[lowestPos] = 3; 
            //低い音にはボーナス
            // give bonus to lower notes
            //3度、５度、７度の一番多いものがコードの基音
            // The note should have the third, fifth, seventh most. 

            for (int m = 0; m < noteNumber; m++) {
                boolean useThird = false;
                boolean useSeventh = false;
                for (int n = 0; n < noteNumber; n++) {
                    if (octaveMatrix[n * noteNumber + m] < 2) {
                        switch (intervalMatrix[n * noteNumber + m]) {
                            case 0: //ユニゾン・オクターブ Unison&Octave
                                evaluation[m] += 4;
                                break;
                            case 3: //短3度 minor third
                                if (useThird == false) {
                                    evaluation[m] += 4;
                                    useThird = true;
                                }
                                break;
                            case 4: //長3度 major third
                                if (useThird == false) {
                                    evaluation[m] += 4;
                                    useThird = true;
                                }
                                break;
                            case 5: //Sus4
                                if (noteNumber == 3)
                                    evaluation[m] += 4;
                                break;
                            case 6: //Dim
                                if (noteNumber <= 4)
                                    evaluation[m] += 3;
                                break;
                            case 7: //Perfect 5
                                evaluation[m] += 4;
                                if (noteNumber == 3)
                                    evaluation[m] += 1;
                                break;
                            case 8: // Aug
                                evaluation[m] += 1;
                                break;
                            case 10:// m7
                                if (useSeventh == false) {
                                    evaluation[m] += 4;
                                    useSeventh = true;
                                }
                                break;
                            case 11:// M7
                                if (useSeventh == false) {
                                    evaluation[m] += 4;
                                    useSeventh = true;
                                }
                                break;
                        }
                    }
                }
            }


            //基音をソートし、０で無いものをカウントする
            //Sort roots, and count over 0 score.
            int rootNote = 0;
            int rootPos = 0;
            int temp = 0;
            int len = evaluation.length;
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
            for(int n = 0;n < len;n++){
                map.put(n, evaluation[n]);
            }
            java.util.List<HashMap.Entry<Integer, Integer>> entries =
                    new ArrayList<HashMap.Entry<Integer, Integer>>(map.entrySet());

            Collections.sort(entries, new Comparator<HashMap.Entry<Integer,Integer>>(){
                @Override
                public int compare(java.util.Map.Entry<Integer, Integer> o1, java.util.Map.Entry<Integer, Integer> o2){
                    return (o2.getValue()).compareTo(o1.getValue());
                }
            });

            int m = 0;
            for(Map.Entry<Integer, Integer> entry: entries){
                int numberOfMThird = 0;
                int numberOfmThird = 0;
                int numberOfPFifth = 0;
                int numberOfdFifth = 0;
                int numberOfaFifth = 0;
                int numberOfMSeventh = 0;
                int numberOfmSeventh = 0;
                rootNote = notes[entry.getKey()];
                rootPos = entry.getKey();
                if(evaluation[rootPos] == 0 || m == codeNumber) continue;
                answer[m] = new Chord();
                answer[m].SetRoot(MidiNoteNumberToRootNote(rootNote));
                answer[m].SetThird(Chord.Third.None);

                for (int n = 0; n < noteNumber; n++) {
                    if (octaveMatrix[n * noteNumber + rootPos] < 2) {
                        switch (intervalMatrix[n * noteNumber + rootPos]) {
                            case 3:
                                numberOfmThird++;
                                break;
                            case 4:
                                numberOfMThird++;
                                break;
                            case 6:
                                numberOfdFifth++;
                                break;
                            case 7:
                                numberOfPFifth++;
                                break;
                            case 8:
                                numberOfaFifth++;
                                break;
                            case 10:
                                numberOfmSeventh++;
                                break;
                            case 11:
                                numberOfMSeventh++;
                                break;
                        }
                    }
                }
                //基音に対する三度を決定
                //Determine the thirds
                for (int n = 0; n < noteNumber; n++) {
                    if (n != rootPos) {
                        switch (intervalMatrix[n * noteNumber + rootPos]) {
                            case 1:
                                answer[m].SetNinth(Chord.Tension.Minor);
                                break;
                            case 2:
                                answer[m].SetNinth(Chord.Tension.Major);
                                break;
                            case 3:
                                if (numberOfmThird > numberOfMThird)
                                    answer[m].SetThird(Chord.Third.Minor);
                                break;
                            case 4:
                                if (numberOfMThird > numberOfmThird)
                                    answer[m].SetThird(Chord.Third.Major);
                                break;
                            case 5:
                                if (answer[m].GetThird() == Chord.Third.None && noteNumber == 3)
                                    answer[m].SetThird(Chord.Third.Sus4);
                                else
                                    answer[m].SetEleventh(Chord.Tension.Perfect);
                                break;
                            case 6:
                                if (numberOfdFifth > numberOfPFifth)
                                    answer[m].SetFifth(Chord.Fifth.Diminished);
                                else
                                    answer[m].SetEleventh(Chord.Tension.Augmented);
                                break;
                            case 7:
                                if (numberOfPFifth > numberOfaFifth && numberOfPFifth > numberOfdFifth)
                                    answer[m].SetFifth(Chord.Fifth.Perfect);
                                break;
                            case 8:
                                if (numberOfaFifth > numberOfPFifth)
                                    answer[m].SetFifth(Chord.Fifth.Augmented);
                                else
                                    answer[m].SetThirteenth(Chord.Tension.Minor);
                                break;
                            case 9:
                                if (answer[m].GetSeventh() == Chord.Seventh.None)
                                    answer[m].SetSixth(Chord.Sixth.Major);
                                else
                                    answer[m].SetThirteenth(Chord.Tension.Major);
                                break;
                            case 10:
                                if (numberOfmSeventh > numberOfMSeventh)
                                    answer[m].SetSeventh(Chord.Seventh.Minor);
                                break;
                            case 11:
                                if (numberOfMSeventh > numberOfmSeventh)
                                    answer[m].SetSeventh(Chord.Seventh.Major);
                                break;
                        }
                    }
                }

                //例外処理を書く
                //Give a special rules
                if (answer[m].GetFifth() == Chord.Fifth.Diminished) {

                    //Cm7(b5)
                    if (answer[m].GetThird() == Chord.Third.Minor && answer[m].GetSeventh() == Chord.Seventh.Minor) {
                        answer[m].SetFifth(Chord.Fifth.Flatted);
                    }
                    //Cdim7
                    if (answer[m].GetThird() == Chord.Third.Minor &&
                            (answer[m].GetSixth() == Chord.Sixth.Major || answer[m].GetThirteenth() == Chord.Tension.Major)) {
                        answer[m].SetSixth(Chord.Sixth.None);
                        answer[m].SetThirteenth(Chord.Tension.None);
                        answer[m].SetSeventh(Chord.Seventh.Minor);
                    }
                    //C(b5)7
                    if (answer[m].GetThird() == Chord.Third.Major) {
                        answer[m].SetFifth(Chord.Fifth.Flatted);
                    }
                }

                if (answer[m].GetSeventh() == Chord.Seventh.None) {
                    //C6 ただし、７が無い場合
                    //C6, if the chord does not have the seventh
                    if (answer[m].GetThirteenth() == Chord.Tension.Major) {
                        answer[m].SetThirteenth(Chord.Tension.None);
                        answer[m].SetSixth(Chord.Sixth.Major);
                    }

                    if (answer[m].GetEleventh() == Chord.Tension.None && answer[m].GetThirteenth() == Chord.Tension.None) {
                        //Cadd9
                        if (answer[m].GetNinth() == Chord.Tension.Major) {
                            answer[m].SetNinth(Chord.Tension.AddMajor);
                        }
                        //Caddb9
                        if (answer[m].GetNinth() == Chord.Tension.Minor) {
                            answer[m].SetNinth(Chord.Tension.AddMinor);
                        }
                    }
                } else {
                    //C13 ７がある場合
                    //C13 if the chord has the seventh.
                    if (answer[m].GetSixth() == Chord.Sixth.Major) {
                        answer[m].SetThirteenth(Chord.Tension.Major);
                    } else if (answer[m].GetSixth() == Chord.Sixth.Minor) {
                        answer[m].SetThirteenth(Chord.Tension.Minor);
                    }

                }
                m++;
            }
            codeNumber = m;
        }

        Chord[] resultArray = new Chord[codeNumber];
        for(int n = 0;n < codeNumber;n++){
            resultArray[n] = answer[n];
        }
        return resultArray;
    }

    private float[] smoothTempData;

    public boolean Smoothing(float[] data, int smoothLength) {
        if (data == null || data.length == 0) return false;
        if (smoothTempData == null || smoothTempData.length != data.length) {
            smoothTempData = new float[data.length];
        }

        for (int n = 0; n < data.length; n++) {
            float temp = 0;
            for (int m = -smoothLength / 2; m < smoothLength / 2; m++) {
                if (n + m < 0) {
                    temp += data[0];
                } else if (n + m >= data.length) {
                    temp += data[data.length - 1];
                } else {
                    temp += data[n + m];
                }
            }
            smoothTempData[n] = temp / (float) smoothLength;
        }

        for (int n = 0; n < data.length; n++) {
            data[n] = smoothTempData[n];
        }
        return true;
    }

    private float[] diffTempData;

    public boolean Differentiate(float[] data, int method) {
        if (data == null || data.length == 0) return false;

        if (diffTempData == null || diffTempData.length != data.length) {
            diffTempData = new float[data.length];
        }

        switch (method) {
            case 0: //CentralDifference
            {
                for (int i = 1; i < data.length - 1; i++) {
                    diffTempData[i] = data[i + 1] - data[i - 1];
                }
                diffTempData[0] = diffTempData[1];
                diffTempData[data.length - 1] = diffTempData[data.length - 1];
            }
            break;
            case 1: //ThreePointStencil
            {
                for (int i = 1; i < data.length - 1; i++) {
                    diffTempData[i] = 0.5f * data[i + 1] - 0.5f * data[i - 1];
                }
                diffTempData[0] = diffTempData[1];
                diffTempData[data.length - 1] = diffTempData[data.length - 2];
            }
            break;
            case 2: //FivePointStencil
            {
                for (int i = 2; i < data.length - 2; i++) {
                    diffTempData[i] = (
                            -data[i + 2]
                                    + 8f * data[i + 1]
                                    - 8f * data[i - 1]
                                    + data[i - 2]
                    ) / 12f;
                }
                diffTempData[0] = diffTempData[2];
                diffTempData[1] = diffTempData[2];
                diffTempData[data.length - 2] = diffTempData[data.length - 3];
                diffTempData[data.length - 1] = diffTempData[data.length - 3];
            }
            break;
            case 3: //SevenPointStencil
            {
                for (int i = 3; i < data.length - 3; i++) {
                    diffTempData[i] = (
                            data[i + 3]
                                    - 9f * data[i + 2]
                                    + 45f * data[i + 1]
                                    - 45f * data[i - 1]
                                    + 9f * data[i - 2]
                                    - data[i - 3]
                    ) / 60f;
                }
                diffTempData[0] = diffTempData[3];
                diffTempData[1] = diffTempData[3];
                diffTempData[2] = diffTempData[3];
                diffTempData[data.length - 3] = diffTempData[data.length - 4];
                diffTempData[data.length - 2] = diffTempData[data.length - 4];
                diffTempData[data.length - 1] = diffTempData[data.length - 4];
            }
            break;
            default:
                return false;
        }

        for (int n = 0; n < data.length; n++) {
            data[n] = diffTempData[n];
        }

        return true;
    }

    private float AmpThreshold = 5;
    private float SlopeThreshold = 0.01f;
    private float InputThreshold = 1e9f;

    public int[] NoteFind(float[] freq, float[] data) {
        final int SmoothWidth = 5;
        //const int PeakGroup = 20;
        //float PeakGroupArrayY[PeakGroup];
        //float PeakGroupArrayX[PeakGroup];
        float[] Spectrum = data.clone();
        ArrayList<Peak> Peaks = new ArrayList<Peak>();

        //スペクトルのノーマライズ
        float fMin = Float.MAX_VALUE;
        float fMax = -Float.MIN_VALUE;
        for (int n = 0; n < data.length; n++) {
            if (Spectrum[n] < fMin)
                fMin = Spectrum[n];
            if (Spectrum[n] > fMax)
                fMax = Spectrum[n];
        }
        if (fMax < InputThreshold) return new int[0];

        for (int n = 0; n < data.length; n++) {
            Spectrum[n] = 100f * (Spectrum[n] - fMin) / (fMax - fMin);
        }
        //const int Order = 3;
        //float FitResult[Order];
        //メモリ確保
        float[] DifSpectrum = null;
        //スムージング

        boolean ret = Smoothing(Spectrum, SmoothWidth);
        if (ret == false)
            return new int[0];

        //微分
        DifSpectrum = Spectrum.clone();
        ret = Differentiate(DifSpectrum, 2); //FivePointStencil
        if (ret == false)
            return new int[0];

        //微分した結果をスムージング
        ret = Smoothing(DifSpectrum, SmoothWidth);
        if (ret == false)
            return new int[0];
        //int PeakHalfWidth = (int)(PeakGroup / 2 + 1);


        //微分した結果が + -> - に変化する位置を検出
        //int GroupIndex;
        for (int i = SmoothWidth; i < data.length - SmoothWidth; i++) {

            //検出した変化量がある一定以上ならDif[n] - Dif[n+1] > Thresholdピークフィットを実行
            if (DifSpectrum[i] - DifSpectrum[i + 1] > SlopeThreshold * Spectrum[i]
                    && DifSpectrum[i] >= 0
                    && DifSpectrum[i + 1] <= 0) {

                if (Spectrum[i] > AmpThreshold) {
                    //ある程度の範囲で２次のフィッティング
                    //範囲を抽出
                /*for(int k = 0; k < PeakGroup; k++){
                    GroupIndex = i + k - PeakHalfWidth + 1;
                    if(GroupIndex < 0) GroupIndex = 0;
                    else if(GroupIndex > length - 1) GroupIndex = length - 1;
                    PeakGroupArrayY[k] = Spectrum[GroupIndex];
                    PeakGroupArrayX[k] = X[GroupIndex];
                }*/
                    //２次フィッティング
                    //[PolynomialFitting PolyFit:PeakGroupArrayX:PeakGroupArrayY:PeakGroup:FitResult:Order];
                    //ピークを追加
                    //id peak = [[Peak alloc] initWithPeak:-0.5 * FitResult[1] / FitResult[2]:FitResult[0] - FitResult[1] * FitResult[1] / 4.0 / FitResult[2]];
                    //ピークの倍音をサーチ
                    boolean NoHarmonics = true;
                    for (int m = 0; m < Peaks.size(); m++) {

                        if (freq[i] % Peaks.get(m).GetFreq() < 0.2) {
                            NoHarmonics = false;
                            //NSLog(@"Kabutta!%f , %f", X[i], [[Peaks objectAtIndex:m] GetFreq]);
                            break;
                        }
                    }

                    if (NoHarmonics) {
                        Peak peak = new Peak(freq[i], data[i]);
                        Peaks.add(peak);
                    }

                }
            }
        }
        int numPeak = 0;
        Collections.sort(Peaks);
        ArrayList<Integer> notes = new ArrayList<Integer>();
        int counts = Peaks.size();
        for (int i = 0; i < counts && i < 10; i++) {

            int d = MidiTuningStandardFreq2Note(Peaks.get(counts - i - 1).GetFreq());
            if (d <= 96 && d >= 0) {
                notes.add(new Integer(d));
                numPeak++;
            }
        }

        int[] returnArray = new int[notes.size()];
        for (int n = 0; n < returnArray.length; n++) {
            returnArray[n] = notes.get(n).intValue();
        }
        return returnArray;
    }

    public int MidiTuningStandardFreq2Note(float freq) {

        if (freq <= 0)
            return -1;
        double value = 69 + (12.0 * Math.log(freq / 440.0) / Math.log(2.0));
        int d = (int) Math.round(value);
        return d;
    }

    public float MidiTuningStandardNote2Freq(int note) {

        double freq = 440.0 * Math.pow(2.0, ((float) note - 69.0) / 12.0);
        return (float) freq;
    }
}
