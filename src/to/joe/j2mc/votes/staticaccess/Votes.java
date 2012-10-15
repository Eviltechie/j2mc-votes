package to.joe.j2mc.votes.staticaccess;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import to.joe.j2mc.votes.J2MC_Votes;
import to.joe.j2mc.votes.ResultHandler;
import to.joe.j2mc.votes.exception.VoteAlreadyInProgressException;
import to.joe.j2mc.votes.exception.VoteNotLoadedException;

public class Votes {

    private static J2MC_Votes instance;

    /**
     * Don't you fucking dare.
     * (This is for internal use only)
     * 
     * @param instance
     */
    public static void setInstance(J2MC_Votes votes) {
        instance = votes;
    }

    /**
     * Creates a new vote which will fire a {@link VoteTallyEvent} that has the winning choice
     * 
     * @param question
     *            The question
     * @param choices
     *            {@link List} of possible answers
     * @param id
     *            A {@link UUID} that the {@link VoteTallyEvent} will contain to easily see if this is your event.
     * @param time
     *            How long the vote should run for in seconds.
     * @param publicVotes
     *            Whether or not to announce who voted for what
     * @param cancelable
     *            Whether or not the vote can be canceled
     * @throws VoteAlreadyInProgressException
     * @throws VoteNotLoadedException
     */
    public void newVote(String question, Set<String> choices, ResultHandler handler, int time, boolean publicVotes, boolean cancelable) throws VoteAlreadyInProgressException, VoteNotLoadedException {
        instance.newVote(question, choices, handler, time, publicVotes, cancelable);
    }

    /**
     * Creates a new vote which will fire a {@link VoteTallyEvent} that has the winning object
     * 
     * @param question
     *            The question
     * @param choices
     *            A {@link HashMap} of possible choices to vote for. The string (key) is the representation of the object (value) which will be used as a choice.
     * @param id
     *            A {@link UUID} that the {@link VoteTallyEvent} will contain to easily see if this is your event.
     * @param time
     *            How long the vote should run for in seconds.
     * @param publicVotes
     *            Whether or not to announce who voted for what
     * @param cancelable
     *            Whether or not the vote can be canceled
     * @throws VoteAlreadyInProgressException
     * @throws VoteNotLoadedException
     */
    public void newVote(String question, HashMap<String, ?> choices, ResultHandler handler, int time, boolean publicVotes, boolean cancelable) throws VoteAlreadyInProgressException, VoteNotLoadedException {
        instance.newVote(question, choices, handler, time, publicVotes, cancelable);
    }
}