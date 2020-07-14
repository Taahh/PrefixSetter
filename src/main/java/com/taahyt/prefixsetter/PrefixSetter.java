package com.taahyt.prefixsetter;

import com.taahyt.prefixsetter.bridge.LuckPermsBridge;
import com.taahyt.prefixsetter.command.PrefixChangerCommand;
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

        String author = getDescription().getAuthors().get(0);
        String version = getDescription().getVersion();

        getLogger().info(String.format("Enabled version %s by %s", version, author));

    }

    public static PrefixSetter getPlugin() {
        return plugin;
    }
}
