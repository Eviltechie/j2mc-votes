package to.joe.j2mc.votes;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.votes.command.VoteCommand;
import to.joe.j2mc.votes.exception.VoteAlreadyInProgressException;
import to.joe.j2mc.votes.runnable.VoteTallyer;

public class J2MC_Votes extends JavaPlugin {

    private static J2MC_Votes instance;
    private static Thread mainThread;

    @Override
    public void onEnable() {
        getCommand("vote").setExecutor(new VoteCommand(this));
        setInstance(this);
    }

    @Override
    public void onDisable() {
        setInstance(null);
    }

    public static void startNewPoll(Poll<?> poll) throws VoteAlreadyInProgressException {
        if (instance == null) {
            throw new RuntimeException("J2MC_Votes not enabled, somehow");
        }
        if (!Thread.currentThread().equals(mainThread)) {
            throw new RuntimeException("Dipshit called votes from another thread.");
        }
        instance.newPoll(poll);
    }

    private void setInstance(J2MC_Votes instance) {
        J2MC_Votes.instance = instance;
        J2MC_Votes.mainThread = Thread.currentThread();
    }

    private Poll<?> poll;

    private int voteTallyTask;

    public void newPoll(Poll<?> poll) throws VoteAlreadyInProgressException {
        if (this.poll != null) {
            throw new VoteAlreadyInProgressException();
        }
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "A vote has been started!");
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Question: " + poll.getQuestion());
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Choices are:");

        int num = 1;
        for (PollChoice<?> choice : poll.getChoices()) {
            getServer().broadcastMessage(ChatColor.DARK_AQUA.toString() + num + " " + choice.getName());
            num++;
        }

        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Vote with /vote <#>");
        getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting ends in " + poll.getTime() + " seconds");

        voteTallyTask = getServer().getScheduler().scheduleSyncDelayedTask(this, new VoteTallyer(this), poll.getTime() * 20);
    }

    public Poll<?> getPoll() {
        return poll;
    }

    public void reset() {
        this.getServer().getScheduler().cancelTask(this.voteTallyTask);
        this.poll = null;
    }

    public boolean hasPoll() {
        return this.poll != null;
    }

}
