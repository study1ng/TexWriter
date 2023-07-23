package TexWriter.General.Collections;

import java.util.ArrayList;
import java.util.Arrays;

public class Tuple<T> {
    private ArrayList<T> data;

    public Tuple(T... data) {
        this.data = new ArrayList<T>(Arrays.asList(data));
    }

    public T get(int index) {
        return data.get(index);
    }
}
