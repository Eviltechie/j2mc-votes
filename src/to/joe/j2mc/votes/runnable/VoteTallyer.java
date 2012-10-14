package to.joe.j2mc.votes.runnable;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import to.joe.j2mc.votes.J2MC_Votes;
import to.joe.j2mc.votes.event.VoteTallyEvent;

public class VoteTallyer implements Runnable {
    
    J2MC_Votes plugin;
    
    public VoteTallyer(J2MC_Votes votes) {
        plugin = votes;
    }

    @Override
    public void run() {
        plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting has finished");
        HashMap<String, Integer> results = new HashMap<String, Integer>();
        for (String s : plugin.possibleVotes) {
            results.put(s, 0);
        }
        for (Integer vote: plugin.votes.values()) {
            Integer i = results.get(plugin.possibleVotes.get(vote)) + 1;
            results.put(plugin.possibleVotes.get(vote), i);
        }
        for (String s : results.keySet()) {
            plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + s + " " + results.get(s));
        }
        Entry<String, Integer> winner = null;
        for (Entry<String, Integer> e : results.entrySet()) {
            if (winner == null || e.getValue() > winner.getValue())
                winner = e;
        }
        plugin.getServer().getPluginManager().callEvent(new VoteTallyEvent(plugin.id, plugin.choices.get(winner.getKey())));
        plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "The winner is " + winner.getKey() + " with " + winner.getValue() + " votes");
        plugin.question = null;
    }

}
