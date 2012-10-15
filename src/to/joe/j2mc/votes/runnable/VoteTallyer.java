package to.joe.j2mc.votes.runnable;

import java.util.List;

import org.bukkit.ChatColor;

import to.joe.j2mc.votes.J2MC_Votes;
import to.joe.j2mc.votes.Poll;
import to.joe.j2mc.votes.PollChoice;

public class VoteTallyer implements Runnable {

    J2MC_Votes plugin;

    public VoteTallyer(J2MC_Votes votes) {
        this.plugin = votes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        final Poll<?> poll = this.plugin.getPoll();
        poll.tally();
        if (poll.showResult()) {
            //TODO: TIE HANDLING
            final List<?> choices = poll.getChoices();
            this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Voting has finished");
            PollChoice<?> winner = null;
            for (final PollChoice<?> choice : (List<PollChoice<?>>) choices) {
                this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + choice.getName() + " " + choice.getResult());
                if ((winner == null) || (choice.getResult() > winner.getResult())) {
                    winner = choice;
                }
            }
            this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "The winner is " + winner.getName() + " with " + winner.getResult() + " votes");
        }
        poll.handleResult();
        this.plugin.reset();
    }

}
