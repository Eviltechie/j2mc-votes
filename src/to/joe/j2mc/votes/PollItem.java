package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.List;

public class PollItem<T> {
    public static List<PollItem<String>> fromList(List<String> list) {
        final List<PollItem<String>> ret = new ArrayList<PollItem<String>>();
        for (final String s : list) {
            ret.add(new PollItem<String>(s, s));
        }
        return ret;
    }

    private final String name;
    private final T value;
    private boolean tallied;

    int result = 0;

    public PollItem(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public int getResult() {
        return this.result;
    }

    public T getValue() {
        return this.value;
    }

    public boolean isTallied() {
        return this.tallied;
    }

    public void setResult(int result) {
        this.result = result;
        this.tallied = true;
    }
}
