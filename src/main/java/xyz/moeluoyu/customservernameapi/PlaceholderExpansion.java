package xyz.moeluoyu.customservernameapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static xyz.moeluoyu.customservernameapi.Main.CURRENT_CONFIG_VERSION;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final JavaPlugin plugin;

    public PlaceholderExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "customservername";
    }

    @Override
    public String getAuthor() {
        return "MoeLuoYu";
    }

    @Override
    public String getVersion() {
        return CURRENT_CONFIG_VERSION;
    }

    @Override
    public boolean persist() {
        return plugin.getConfig().getBoolean("persist", true);
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.isEmpty()) {
            String serverName = ((Main) plugin).getServerName();
            // 处理颜色代码，将 & 替换为 §
            return translateColorCodes(serverName);
        } else if (identifier.equals("alias")) {
            String serverAlias = ((Main) plugin).getServerAlias();
            // 处理颜色代码，将 & 替换为 §
            return translateColorCodes(serverAlias);
        }

        return null;
    }

    public String translateColorCodes(String text) {
        return text.replace('&', '§');
    }
}