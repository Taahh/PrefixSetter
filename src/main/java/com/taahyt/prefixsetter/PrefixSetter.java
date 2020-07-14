package com.taahyt.prefixsetter;

import com.taahyt.prefixsetter.bridge.LuckPermsBridge;
import com.taahyt.prefixsetter.command.PrefixChangerCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PrefixSetter extends JavaPlugin
{

    private static PrefixSetter plugin;
    public LuckPermsBridge luckPermsBridge;

    @Override
    public void onEnable()
    {
        plugin = this;


        luckPermsBridge = new LuckPermsBridge();

        getCommand("setprefix").setExecutor(new PrefixChangerCommand());
    }


    @Override
    public void onDisable()
    {

    }
    public static PrefixSetter getPlugin() {
        return plugin;
    }
}
