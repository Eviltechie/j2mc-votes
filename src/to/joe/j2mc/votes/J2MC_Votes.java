package to.joe.j2mc.votes;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.votes.command.VoteCommand;
import to.joe.j2mc.votes.exception.VoteAlreadyInProgressException;
import to.joe.j2mc.votes.runnable.VoteTallyer;

public class J2MC_Votes extends JavaPlugin {

    private static J2MC_Votes instance;
    private static Thread mainThread;

    public static void startNewPoll(Poll<?> poll) throws VoteAlreadyInProgressException {
        if (J2MC_Votes.instance == null) {
            throw new RuntimeException("J2MC_Votes not enabled, somehow");
        }
        if (!Thread.currentThread().equals(J2MC_Votes.mainThread)) {
            throw new RuntimeException("Dipshit called votes from another thread.");
        }
        J2MC_Votes.instance.newPoll(poll);
    }

    private Poll<?> poll;

    private int voteTallyTask;

    public Poll<?> getPoll() {
        return this.poll;
    }

    public boolean hasPoll() {
        return this.poll != null;
    }

    public void newPoll(Poll<?> poll) throws VoteAlreadyInProgressException {
        if (this.poll != null) {
            throw new VoteAlreadyInProgressException();
        }
        this.poll = poll;
        this.getServer().broadcastMessage(ChatColor.DARK_AQUA + "A vote has been started!");
        this.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Question: " + poll.getQuestion());
        this.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Choices are:");

        int num = 1;
        for (final PollItem<?> choice : poll.getChoices()) {
            this.getServer().broadcastMessage(ChatColor.DARK_AQUA.toString() + num + " " + choice.getName());
            num++;
        }

        this.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Vote with /vote <#>");
        this.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting ends in " + poll.getTime() + " seconds");

        this.voteTallyTask = this.getServer().getScheduler().scheduleSyncDelayedTask(this, new VoteTallyer(this), poll.getTime() * 20);
    }

    @Override
    public void onDisable() {
        this.setInstance(null);
    }

    @Override
    public void onEnable() {
        this.getCommand("vote").setExecutor(new VoteCommand(this));
        this.setInstance(this);
    }

    public void reset() {
        this.getServer().getScheduler().cancelTask(this.voteTallyTask);
        this.poll = null;
    }

    private void setInstance(J2MC_Votes instance) {
        J2MC_Votes.instance = instance;
        J2MC_Votes.mainThread = Thread.currentThread();
    }

}
