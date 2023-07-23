package TexWriter.General.Collections;

public class Pair<T> extends Tuple<T> {
    @SuppressWarnings("varargs")
    public Pair(T first, T second) {
        super(first, second);
    }

    public T first() {
        return get(0);
    }

    public T second() {
        return get(1);
    }
}
