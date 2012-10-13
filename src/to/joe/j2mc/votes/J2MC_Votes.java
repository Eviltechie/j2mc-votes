package to.joe.j2mc.votes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.votes.command.CancelVoteCommand;
import to.joe.j2mc.votes.command.NewVoteCommand;
import to.joe.j2mc.votes.command.VoteCommand;
import to.joe.j2mc.votes.event.VoteTallyEvent;

public class J2MC_Votes extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("newvote").setExecutor(new NewVoteCommand(this));
        getCommand("cancelvote").setExecutor(new CancelVoteCommand(this));
    }
    
    //The question that is currently being asked. Null if no vote is in progress
    public String question;
    //A list of choices. String representation and corresponding object
    HashMap<String, ?> choices;
    //An arraylist of the strings in choices
    public ArrayList<String> possibleVotes;
    //Hash map of votes. String is name of player and integer is choice
    public HashMap<String, Integer> votes;
    //Highest number allowed for a vote
    public int highestVoteAllowed;
    
    public boolean publicVotes;
    public boolean voteCancelable;
    public int voteTallyTask;
    
    /**
     * Creates a new vote which will fire a {@link VoteTallyEvent} that has the winning object
     * @param question The question
     * @param answers HashMap of possible answers. The key is the string and the value is the object that will be returned if it wins.
     * @param time How long the vote should run for in seconds
     * @param publicVotes Whether or not to announce who voted for what
     */
    public void newVote(String question, HashMap<String, ?> choices, int time, boolean publicVotes, boolean cancelable) {
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "A vote has been started!");
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Question: " + question);
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Choices are:");
        
        this.choices = choices;
        this.question = question;
        this.publicVotes = publicVotes;
        votes = new HashMap<String, Integer>();
        
        possibleVotes = new ArrayList<String>();
        
        for (String s : choices.keySet()) {
            possibleVotes.add(s);
        }
        
        highestVoteAllowed = possibleVotes.size();
        
        for (int x = 0; x < possibleVotes.size(); x++) {
            getServer().broadcastMessage(ChatColor.DARK_AQUA + "" + (x+1) + " " + possibleVotes.get(x));
        }
        
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Vote with /vote <#>");
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting ends in " + time + " seconds");
        
        voteTallyTask = getServer().getScheduler().scheduleSyncDelayedTask(this, voteTallyer, time*20);
    }
    
    Runnable voteTallyer = new Runnable() {
        
        @Override
        public void run() {
            getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting has finished");
            
            //String is choice string, integer is vote count
            HashMap<String, Integer> results = new HashMap<String, Integer>();
            for (String s : possibleVotes) {
                results.put(s, 0);
            }
            
            for (Integer vote: votes.values()) {
                Integer i = results.get(possibleVotes.get(vote));
                i++;
                results.put(possibleVotes.get(vote), i);
            }
            
            for (String s : results.keySet()) {
                getServer().broadcastMessage(ChatColor.DARK_AQUA + s + " " + results.get(s));
            }
            
            Entry<String, Integer> winner = null;
            
            for (Entry<String, Integer> e : results.entrySet()) {
                if (winner == null || e.getValue() > winner.getValue())
                    winner = e;
            }
            
            getServer().getPluginManager().callEvent(new VoteTallyEvent(choices.get(winner.getKey())));
            
            getServer().broadcastMessage(ChatColor.DARK_AQUA + "The winner is " + winner.getKey() + " with " + winner.getValue() + " votes");
            
            question = null;
        }
    };

    public void onVoteTally(VoteTallyEvent event) {
        Logger l = getServer().getLogger();
        l.info("Just received a vote");
        l.info(event.getObject().toString());
    }
}
