package com.taahyt.prefixsetter.command;

import com.taahyt.prefixsetter.PrefixSetter;
import net.luckperms.api.model.group.Group;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrefixChangerCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.GOLD + "Only in-game players may use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1)
        {
            player.sendMessage(ChatColor.GOLD + "Correct usage: " + ChatColor.WHITE + "/setprefix <list | clear | groupname>");
            player.sendMessage(ChatColor.GOLD + "Examples:\n" +
                    "§6/setprefix <groupname>\n" +
                    "§6/setprefix clear\n" +
                    "§6/setprefix list");
            return true;
        }

        if (args[0].equalsIgnoreCase("clear"))
        {
            PrefixSetter.getPlugin().luckPermsBridge.clearPersonalPrefix(player);
            player.sendMessage(ChatColor.GOLD + "Your personal prefix has been cleared.");
            return true;
        }
        if (args[0].equalsIgnoreCase("list"))
        {
            player.sendMessage(ChatColor.GOLD + "Your current prefix: " + PrefixSetter.getPlugin().luckPermsBridge.getCurrentPrefix(player));
            player.sendMessage(ChatColor.GOLD + "Here's a list of groups you have access to: \n" + ChatColor.WHITE + PrefixSetter.getPlugin().luckPermsBridge.listGroupsandPrefixes(player));
            return true;
        }

        String group = args[0];

        if (PrefixSetter.getPlugin().luckPermsBridge.getGroup(group) == null)
        {
            player.sendMessage(ChatColor.RED + "This group does not exist.");
            return true;
        }

        if (!PrefixSetter.getPlugin().luckPermsBridge.hasGroup(player, group))
        {
            player.sendMessage(ChatColor.RED + "You are not apart of this group.");
            player.sendMessage(ChatColor.GOLD + "Your current prefix: " + PrefixSetter.getPlugin().luckPermsBridge.getCurrentPrefix(player));
            player.sendMessage(ChatColor.GOLD + "Here's a list of groups you have access to: \n" + ChatColor.WHITE + PrefixSetter.getPlugin().luckPermsBridge.listGroupsandPrefixes(player));
            return true;
        }

        Group g = PrefixSetter.getPlugin().luckPermsBridge.getGroup(group);

        PrefixSetter.getPlugin().luckPermsBridge.changePrefix(player, group);
        player.sendMessage(ChatColor.GOLD + "You have changed your prefix to " + group + "'s");
        player.sendMessage(ChatColor.GOLD + "You will now appear as: " + ChatColor.RESET + colorize(g.getCachedData().getMetaData().getPrefix() + " " + player.getName()));
        return true;
    }

    public String colorize(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
