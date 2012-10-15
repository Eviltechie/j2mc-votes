package to.joe.j2mc.votes;

public class DefaultHandler implements ResultHandler {

    @Override
    public void handleResult(Object winner) {
        // Do nothing, really. We've already handled it. Other plugins can do what they want here.
    }

    @Override
    public boolean showResult() {
        return true; // Display via this plugin
    }

}
