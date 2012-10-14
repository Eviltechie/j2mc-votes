package to.joe.j2mc.votes.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.votes.J2MC_Votes;

public class VoteCommand extends MasterCommand {

    J2MC_Votes plugin;

    public VoteCommand(J2MC_Votes votes) {
        super(votes);
        plugin = votes;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
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
