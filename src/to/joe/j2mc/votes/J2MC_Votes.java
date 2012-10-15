package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.votes.command.VoteCommand;
import to.joe.j2mc.votes.exception.VoteAlreadyInProgressException;
import to.joe.j2mc.votes.runnable.VoteTallyer;
import to.joe.j2mc.votes.staticaccess.Votes;

public class J2MC_Votes extends JavaPlugin {

    @Override
    public void onEnable() {
        Votes.setInstance(this);
        getCommand("vote").setExecutor(new VoteCommand(this));
    }

    //The question that is currently being asked. Null if no vote is in progress
    public String question = null;
    //A list of choices. String representation and corresponding object
    public HashMap<String, ?> choices;
    //An arraylist of the strings in choices
    public ArrayList<String> possibleVotes;
    //Hash map of votes. String is name of player and integer is choice
    public HashMap<String, Integer> votes;
    //Highest number allowed for a vote
    public int highestVoteAllowed;

    public UUID id;
    public boolean publicVotes;
    public boolean voteCancelable;
    public int voteTallyTask;

    public void newVote(String question, Set<String> choices, UUID id, int time, boolean publicVotes, boolean cancelable) throws VoteAlreadyInProgressException {
        HashMap<String, String> options = new HashMap<String, String>();
        for (String s : choices) {
            options.put(s, s);
        }
        newVote(question, options, id, time, publicVotes, cancelable);
    }

    public void newVote(String question, HashMap<String, ?> choices, UUID id, int time, boolean publicVotes, boolean cancelable) throws VoteAlreadyInProgressException {
        if (this.question != null) {
            throw new VoteAlreadyInProgressException();
        }
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "A vote has been started!");
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Question: " + question);
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Choices are:");

        this.choices = choices;
        this.question = question;
        this.publicVotes = publicVotes;
        this.id = id;
        voteCancelable = cancelable;
        votes = new HashMap<String, Integer>();

        possibleVotes = new ArrayList<String>();

        for (String s : choices.keySet()) {
            possibleVotes.add(s);
        }

        highestVoteAllowed = possibleVotes.size() - 1;

        for (int x = 0; x < possibleVotes.size(); x++) {
            getServer().broadcastMessage(ChatColor.DARK_AQUA + "" + (x + 1) + " " + possibleVotes.get(x));
        }

        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Vote with /vote <#>");
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting ends in " + time + " seconds");

        voteTallyTask = getServer().getScheduler().scheduleSyncDelayedTask(this, new VoteTallyer(this), time * 20);
    }
}
