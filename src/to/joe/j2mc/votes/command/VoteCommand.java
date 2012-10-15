package to.joe.j2mc.votes.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.votes.J2MC_Votes;
import to.joe.j2mc.votes.exception.VoteAlreadyInProgressException;

public class VoteCommand extends MasterCommand {

    J2MC_Votes plugin;

    public VoteCommand(J2MC_Votes votes) {
        super(votes);
        plugin = votes;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        //TODO See if vote is running. If it is, skip over this, it it's not, see if they have perms and make a new vote
        if (plugin.question == null && sender.hasPermission("j2mc.votes.newvote")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "I can't make a vote unless you tell me what about");
                return;
            }
            Iterator<String> i = Arrays.asList(args).iterator();
            ArrayList<String> combinedArgs = new ArrayList<String>();
            try {
                while (i.hasNext()) {
                    String s = i.next();
                    if (s.matches("\".*?\"")) {
                        combinedArgs.add(s.substring(1, s.length() - 1));
                    } else if (s.contains("\"")) {
                        StringBuilder sb = new StringBuilder();
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
            } catch (NoSuchElementException e) {
                sender.sendMessage(ChatColor.RED + "Invalid input, check your quotes");
                return;
            }
            String question = combinedArgs.remove(0);
            if (combinedArgs.size() == 0) {
                combinedArgs.add("Yes");
                combinedArgs.add("No");
            } else if (combinedArgs.size() == 1) {
                sender.sendMessage(ChatColor.RED + "Can't have only one option");
                return;
            }
            try {
                plugin.newVote(question, new HashSet<String>(combinedArgs), UUID.randomUUID(), 20, true, true);
                return;
            } catch (VoteAlreadyInProgressException e) {
                sender.sendMessage(ChatColor.RED + "A vote is already in progress");
            }
        }
        //TODO See if vote is running. If it is and we are trying to cancel it, then do so, otherwise continue
        if (plugin.question != null && sender.hasPermission("j2mc.votes.cancel") && (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("cancel"))) {
            if (plugin.voteCancelable == false) {
                sender.sendMessage(ChatColor.RED + "This vote cannot be canceled");
                return;
            }
            plugin.getServer().getScheduler().cancelTask(plugin.voteTallyTask);
            plugin.question = null;
            plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "The vote in progress has been canceled");
            return;
        }
        //All this runs if we aren't canceling the vote or making a new one
        if (plugin.question == null) {
            sender.sendMessage(ChatColor.RED + "There is no vote in progress");
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "I need a choice (and only one) to tally your vote");
            return;
        }
        try {
            int i = Integer.parseInt(args[0]);
            i--;
            if (i < 0 || i > plugin.highestVoteAllowed) {
                sender.sendMessage(ChatColor.RED + "That is not a valid choice");
                return;
            }
            if (!plugin.votes.containsKey(sender.getName())) {
                if (plugin.publicVotes) {
                    sender.sendMessage(ChatColor.DARK_AQUA + "Vote recorded. Thank you.");
                    plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + sender.getName() + " chose option " + ++i + ", " + plugin.possibleVotes.get(--i));
                } else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "You chose option " + ++i + ", " + plugin.possibleVotes.get(--i));
                }
            } else {
                if (plugin.publicVotes) {
                    sender.sendMessage(ChatColor.DARK_AQUA + "Vote changed");
                    plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + sender.getName() + " changed their vote to option " + ++i + ", " + plugin.possibleVotes.get(--i));
                } else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "You changed your vote to option " + ++i + ", " + plugin.possibleVotes.get(--i));
                }
            }
            plugin.votes.put(sender.getName(), i);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "That's not a number");
            return;
        }
    }

}
