package to.joe.j2mc.votes;

import java.util.List;

public interface ResultHandler<T> {
    public void handleResult(List<PollItem<T>> list);

    public boolean showResult();

    public void cancelled();
}
