package to.joe.j2mc.votes.command;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.votes.DefaultHandler;
import to.joe.j2mc.votes.J2MC_Votes;
import to.joe.j2mc.votes.Poll;
import to.joe.j2mc.votes.PollItem;
import to.joe.j2mc.votes.exception.VoteInProgressNotCancellableException;

public class VoteCommand extends MasterCommand {

    J2MC_Votes plugin;

    public VoteCommand(J2MC_Votes votes) {
        super(votes);
        this.plugin = votes;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        //Make vote
        if (!this.plugin.hasPoll() && sender.hasPermission("j2mc.votes.newvote")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "I can't make a vote unless you tell me what about");
                return;
            }
            final Iterator<String> i = Arrays.asList(args).iterator();
            final ArrayList<String> combinedArgs = new ArrayList<String>();
            try {
                while (i.hasNext()) {
                    String s = i.next();
                    if (s.matches("\".*?\"")) {
                        combinedArgs.add(s.substring(1, s.length() - 1));
                    } else if (s.contains("\"")) {
                        final StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(s).append(" ");
                            s = i.next();
                        } while (!s.contains("\""));
                        sb.append(s);
                        combinedArgs.add(sb.substring(1, sb.length() - 1));
                    } else {
                        combinedArgs.add(s);
                    }
                }
            } catch (final NoSuchElementException e) {
                sender.sendMessage(ChatColor.RED + "Invalid input, check your quotes");
                return;
            }
            final String question = combinedArgs.remove(0);
            if (combinedArgs.size() == 0) {
                combinedArgs.add("Yes");
                combinedArgs.add("No");
            } else if (combinedArgs.size() == 1) {
                sender.sendMessage(ChatColor.RED + "Can't have only one option");
                return;
            }
            final Poll<String> poll = new Poll<String>(question, PollItem.fromCollection(combinedArgs), new DefaultHandler(), 20, true, true);
            try {
                this.plugin.newPoll(poll);
                return;
            } catch (final VoteInProgressNotCancellableException e) {
                sender.sendMessage(ChatColor.RED + "A vote is already in progress");
            }
        }
        //Cancel vote
        if (this.plugin.hasPoll() && sender.hasPermission("j2mc.votes.cancel") && (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("cancel"))) {
            if (!this.plugin.getPoll().isCancellable()) {
                sender.sendMessage(ChatColor.RED + "This vote cannot be canceled");
                return;
            }
            this.plugin.reset();
            this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "The vote in progress has been canceled");
            return;
        }
        //Record Vote
        if (!this.plugin.hasPoll()) {
            sender.sendMessage(ChatColor.RED + "There is no vote in progress");
            return;
        }
        final Poll<?> poll = this.plugin.getPoll();
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "I need a choice (and only one) to tally your vote");
            return;
        }
        try {
            final int vote = Integer.parseInt(args[0]);
            if (!poll.isValidChoice(vote)) {
                sender.sendMessage(ChatColor.RED + "That is not a valid choice");
                return;
            }
            final Poll.VoteEntered result = poll.vote(sender.getName(), vote);
            final String choice = poll.getChoices().get(vote - 1).getName();
            if (result == Poll.VoteEntered.NEW) {
                if (poll.isPublicDisplay()) {
                    sender.sendMessage(ChatColor.DARK_AQUA + "Vote recorded. Thank you.");
                    this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + sender.getName() + " chose option " + vote + ", " + choice);
                } else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "You chose option " + vote + ", " + choice);
                }
            }
            if (result == Poll.VoteEntered.CHANGED) {
                if (poll.isPublicDisplay()) {
                    sender.sendMessage(ChatColor.DARK_AQUA + "Vote changed");
                    this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + sender.getName() + " changed their vote to option " + vote + ", " + choice);
                } else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "You changed your vote to option " + vote + ", " + choice);
                }
            }
        } catch (final NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "That's not a number");
            return;
        }
    }

}
