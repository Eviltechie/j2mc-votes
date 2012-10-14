package to.joe.j2mc.votes.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.votes.J2MC_Votes;
import to.joe.j2mc.votes.exception.VoteAlreadyInProgressException;

public class NewVoteCommand extends MasterCommand {

    J2MC_Votes plugin;

    public NewVoteCommand(J2MC_Votes votes) {
        super(votes);
        plugin = votes;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        /*HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("Cherry", 64);
        map.put("Apple", 128);
        map.put("Pumpkin", 256);*/
        List<String> map = new ArrayList<String>();
        map.add("Cherry");
        map.add("Apple");
        map.add("Pumpkin");
        try {
            plugin.newVote("Which pie is best?", map, UUID.randomUUID(), 20, true, true);
        } catch (VoteAlreadyInProgressException e) {
            sender.sendMessage(ChatColor.RED + "A vote is already in progress");
        }
    }

}
