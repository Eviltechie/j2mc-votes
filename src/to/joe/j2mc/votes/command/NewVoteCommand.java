package to.joe.j2mc.votes.command;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.votes.J2MC_Votes;

public class NewVoteCommand extends MasterCommand {

    J2MC_Votes plugin;

    public NewVoteCommand(J2MC_Votes votes) {
        super(votes);
        plugin = votes;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("Cherry", 64);
        map.put("Apple", 128);
        map.put("Pumpkin", 256);
        plugin.newVote("Which pie is best?", map, 20, true);
    }

}
