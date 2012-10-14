package to.joe.j2mc.votes.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.votes.J2MC_Votes;

public class CancelVoteCommand extends MasterCommand {

    J2MC_Votes plugin;

    public CancelVoteCommand(J2MC_Votes votes) {
        super(votes);
        plugin = votes;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (plugin.question == null) {
            sender.sendMessage(ChatColor.RED + "There is no vote in progress");
            return;
        }
        if (plugin.voteCancelable == false) {
            sender.sendMessage(ChatColor.RED + "This vote cannot be canceled");
            return;
        }
        plugin.getServer().getScheduler().cancelTask(plugin.voteTallyTask);
        plugin.question = null;
        plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "The vote in progress has been canceled");
    }

}
