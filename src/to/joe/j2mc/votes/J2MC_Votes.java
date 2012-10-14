package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.votes.command.CancelVoteCommand;
import to.joe.j2mc.votes.command.NewVoteCommand;
import to.joe.j2mc.votes.command.VoteCommand;
import to.joe.j2mc.votes.event.VoteTallyEvent;
import to.joe.j2mc.votes.exception.VoteAlreadyInProgressException;
import to.joe.j2mc.votes.runnable.VoteTallyer;

public class J2MC_Votes extends JavaPlugin {
    
    @Override
    public void onEnable() {
        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("newvote").setExecutor(new NewVoteCommand(this));
        getCommand("cancelvote").setExecutor(new CancelVoteCommand(this));
    }
    
    //The question that is currently being asked. Null if no vote is in progress
    public String question;
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
    
    /**
     * Creates a new vote which will fire a {@link VoteTallyEvent} that has the winning choice
     * @param question The question
     * @param choices List of possible answers
     * @param time How long the vote should run for in seconds
     * @param publicVotes Whether or not to announce who voted for what
     * @param cancelable Whether or not the vote can be canceled
     */
    public void newVote(String question, List<String> choices, UUID id, int time, boolean publicVotes, boolean cancelable) throws VoteAlreadyInProgressException {
        if (question != null) {
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
    }
    
    /**
     * Creates a new vote which will fire a {@link VoteTallyEvent} that has the winning object
     * @param question The question
     * @param answers HashMap of possible answers. The string (key) is the representation of the object (value) which will be used as a choice
     * @param time How long the vote should run for in seconds
     * @param publicVotes Whether or not to announce who voted for what
     * @param cancelable Whether or not the vote can be canceled
     */
    public void newVote(String question, HashMap<String, ?> choices, UUID id, int time, boolean publicVotes, boolean cancelable) throws VoteAlreadyInProgressException {
        if (question != null) {
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
        
        highestVoteAllowed = possibleVotes.size()-1;
        
        for (int x = 0; x < possibleVotes.size(); x++) {
            getServer().broadcastMessage(ChatColor.DARK_AQUA + "" + (x+1) + " " + possibleVotes.get(x));
        }
        
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Vote with /vote <#>");
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting ends in " + time + " seconds");
        
        voteTallyTask = getServer().getScheduler().scheduleSyncDelayedTask(this, new VoteTallyer(this), time*20);
    }
}
