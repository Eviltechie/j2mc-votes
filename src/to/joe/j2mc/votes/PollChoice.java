package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.List;

public class PollChoice<T> {
    public static List<PollChoice<String>> fromList(List<String> list) {
        final List<PollChoice<String>> ret = new ArrayList<PollChoice<String>>();
        for (final String s : list) {
            ret.add(new PollChoice<String>(s, s));
        }
        return ret;
    }

    public String name;
    public T value;

    int result = 0;

    public PollChoice(String name, T value) {
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

    public void setResult(int result) {
        this.result = result;
    }
}
