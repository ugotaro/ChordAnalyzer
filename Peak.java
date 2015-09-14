package com.skworks.harm.Harmony;

import java.util.Comparator;

/**
 * Created by kawanoshogo on 2013/11/23.
 */
public class Peak implements Comparator, Comparable {
    float frequency;
    float amplitude;

    public Peak(float freq, float amp) {
        SetFreq(freq);
        SetAmp(amp);
    }

    public void SetFreq(float value) {
        if (value > 0) {
            frequency = value;
        }
    }

    public void SetAmp(float value) {
        amplitude = value;
    }

    public float GetFreq() {
        return frequency;
    }

    public float GetAmp() {
        return amplitude;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Peak p1 = (Peak) o1;
        Peak p2 = (Peak) o2;

        if (p1.amplitude < p2.amplitude) return -1;
        else if (p1.amplitude > p2.amplitude) return 1;
        else return 0;
    }

    @Override
    public int compareTo(Object o) {
        Peak p = (Peak) o;
        if (this.amplitude < p.amplitude) return -1;
        else if (this.amplitude > p.amplitude) return 1;
        else return 0;
    }
}
