package com.taahyt.prefixsetter.bridge;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.query.QueryOptions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LuckPermsBridge
{

    private LuckPerms luckperms;

    public LuckPermsBridge()
    {
        this.luckperms = LuckPermsProvider.get();
    }

    public void changePrefix(Player player, String group)
    {
        getLuckPerms().getUserManager().modifyUser(player.getUniqueId(), (User user) -> {
            String prefix = getGroup(group).getCachedData().getMetaData().getPrefix();

            user.data().clear(NodeType.PREFIX::matches);

            int priority = user.getCachedData().getMetaData(QueryOptions.nonContextual()).getPrefixes()
                    .keySet().stream().mapToInt(i-> i + 10).max().orElse(10);
            Node node = PrefixNode.builder(prefix, priority).build();

            user.data().add(node);
        });
    }

    public boolean hasGroup(Player player, String group)
    {
        return getGroups(player).contains(group);
    }

    public Set<String> getGroups(Player player)
    {
        User user = getLuckPerms().getUserManager().getUser(player.getUniqueId());
        assert user != null;
        Set<String> groups = user.getNodes().stream()
            .filter(NodeType.INHERITANCE::matches)
            .map(NodeType.INHERITANCE::cast)
            .map(InheritanceNode::getGroupName)
            .collect(Collectors.toSet());
        for (String name : groups)
        {
            if (name == null)
            {
                continue;
            }
            Group group = getGroup(name);
            Set<String> moreGroups = group.getNodes().stream()
            .filter(NodeType.INHERITANCE::matches)
            .map(NodeType.INHERITANCE::cast)
            .map(InheritanceNode::getGroupName)
            .collect(Collectors.toSet());
            groups.addAll(moreGroups);
            for (String name2 : moreGroups) //this is honestly an endless cycle isnt it lol
            {
                if (name == null)
                {
                    continue;
                }
                Group moreGroup = getGroup(name2);
                Set<String> evenMoreGroups = moreGroup.getNodes().stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .map(InheritanceNode::getGroupName)
                .collect(Collectors.toSet());
                groups.addAll(evenMoreGroups);
            }
        }
        return groups;
    }

    public String getCurrentPrefix(Player player)
    {
        String prefix = getLuckPerms().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
        return (prefix == null ? "N/A" : ChatColor.translateAlternateColorCodes('&', prefix));
    }

    public String listGroupsandPrefixes(Player player)
    {
        List<String> groupsAndPrefix = new ArrayList<>();

        for (String s : getGroups(player))
        {
            System.out.println(s);
            String groupPrefix = getGroup(s).getCachedData().getMetaData().getPrefix();
            if (groupPrefix == null)
            {
                continue;
            }

            System.out.println(groupPrefix);

            assert groupPrefix != null;
            groupsAndPrefix.add(s + " - " + ChatColor.translateAlternateColorCodes('&', groupPrefix + player.getName()));
        }
        String list = StringUtils.join(groupsAndPrefix, "\n");
        return (list.isEmpty() ? "N/A" : list);
    }

    public void clearPersonalPrefix(Player player)
    {
        User user = getLuckPerms().getUserManager().getUser(player.getUniqueId());
        assert user != null;
        user.data().clear(NodeType.PREFIX::matches);
    }

    public Group getGroup(String group)
    {
        return getLuckPerms().getGroupManager().getGroup(group);
    }


    public LuckPerms getLuckPerms() {
        return luckperms;
    }
}
