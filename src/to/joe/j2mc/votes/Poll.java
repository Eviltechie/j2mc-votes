package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Poll<T> {
    //The question that is currently being asked. Null if no vote is in progress
    private String question = null;
    //A list of choices. String representation and corresponding object
    private List<PollChoice<T>> choices;
    //Hash map of votes. String is name of player and integer is choice
    private LinkedHashMap<String, Integer> votes;
    //Result handler for this vote
    private ResultHandler<T> handler;
    private int time;

    private boolean publicVotes;
    private boolean voteCancelable;

    public Poll(String question, List<PollChoice<T>> choices, ResultHandler<T> handler, int time, boolean publicVotes, boolean cancelable) {
        this.question = question;
        this.choices = choices;
        this.handler = handler;
        this.time = time;
        this.publicVotes = publicVotes;
        this.voteCancelable = cancelable;
    }

    public String getQuestion() {
        return this.question;
    }

    public boolean isValidChoice(int choice) {
        return choice > 0 && choice <= this.choices.size();
    }

    public List<PollChoice<T>> getChoices() {
        return new ArrayList<PollChoice<T>>(choices);
    }

    public boolean isCancellable() {
        return this.voteCancelable;
    }

    public boolean isPublicDisplay() {
        return this.publicVotes;
    }

    public int getTime() {
        return this.time;
    }

    public VoteEntered vote(String name, int id) {
        return this.votes.put(name, id) == null ? VoteEntered.NEW : VoteEntered.CHANGED;
    }

    public void tally() {
        int[] tally = new int[this.choices.size()];
        for (Integer i : this.votes.values()) {
            tally[i--]++;
        }
        int x = 0;
        for (PollChoice<T> choice : this.choices) {
            choice.setResult(tally[x]);
            x++;
        }
    }

    public enum VoteEntered {
        NEW,
        CHANGED
    }

    public boolean showResult() {
        return handler.showResult();
    }

    public void handleResult() {
        this.handler.handleResult(this.choices);
    }
}
