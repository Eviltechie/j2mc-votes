package to.joe.j2mc.votes;

import java.util.List;

public class DefaultHandler implements ResultHandler<String> {

    @Override
    public void cancelled() {
        // Whatever
    }

    @Override
    public void handleResult(List<PollItem<String>> list) {
        // Do nothing, really. We've already handled it. Other plugins can do what they want here.
    }

    @Override
    public boolean showResult() {
        return true; // Display via this plugin
    }

}
