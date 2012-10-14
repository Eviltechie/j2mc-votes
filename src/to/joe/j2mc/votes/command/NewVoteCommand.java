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

public class NewVoteCommand extends MasterCommand {

    J2MC_Votes plugin;

    public NewVoteCommand(J2MC_Votes votes) {
        super(votes);
        plugin = votes;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
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
        } catch (VoteAlreadyInProgressException e) {
            sender.sendMessage(ChatColor.RED + "A vote is already in progress");
        }
    }

}
