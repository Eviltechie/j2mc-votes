package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PollItem<T> {
    public static List<PollItem<String>> fromCollection(Collection<String> collection) {
        final List<PollItem<String>> ret = new ArrayList<PollItem<String>>();
        for (final String s : collection) {
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
