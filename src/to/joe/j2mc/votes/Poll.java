package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll<T> {
    public enum VoteEntered {
        NEW,
        CHANGED
    }

    //The question that is currently being asked. Null if no vote is in progress
    private String question = null;
    //A list of choices. String representation and corresponding object
    private final List<PollItem<T>> choices;
    //Hash map of votes. String is name of player and integer is choice
    private HashMap<String, Integer> votes;
    //Result handler for this vote
    private final ResultHandler<T> handler;

    private final int time;
    private final boolean publicVotes;

    private final boolean voteCancelable;
    private boolean tallied;

    public Poll(String question, List<PollItem<T>> choices, ResultHandler<T> handler, int time, boolean publicVotes, boolean cancelable) {
        this.question = question;
        this.choices = choices;
        this.handler = handler;
        this.time = time;
        this.publicVotes = publicVotes;
        this.voteCancelable = cancelable;
        this.votes = new HashMap<String, Integer>();
    }

    public List<PollItem<T>> getChoices() {
        return new ArrayList<PollItem<T>>(this.choices);
    }

    public String getQuestion() {
        return this.question;
    }

    public int getTime() {
        return this.time;
    }

    public void handleResult() {
        this.handler.handleResult(this.choices);
    }

    public boolean isCancellable() {
        return this.voteCancelable;
    }

    public boolean isPublicDisplay() {
        return this.publicVotes;
    }

    public boolean isTallied() {
        return this.tallied;
    }

    public boolean isValidChoice(int choice) {
        return (choice > 0) && (choice <= this.choices.size());
    }

    public boolean showResult() {
        return this.handler.showResult();
    }

    public void tally() {
        if (this.tallied) {
            throw new RuntimeException("Poll already tallied");
        }
        this.tallied = true;
        final int[] tally = new int[this.choices.size()];
        for (Integer i : this.votes.values()) {
            tally[i--]++;
        }
        int x = 0;
        for (final PollItem<T> choice : this.choices) {
            choice.setResult(tally[x]);
            x++;
        }
    }

    public VoteEntered vote(String name, int id) {
        return this.votes.put(name, id) == null ? VoteEntered.NEW : VoteEntered.CHANGED;
    }
}
