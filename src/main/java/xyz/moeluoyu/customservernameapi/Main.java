package xyz.moeluoyu.customservernameapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends JavaPlugin implements TabCompleter {

    private PlaceholderExpansion placeholderExpansion;
    // 定义当前插件配置文件的版本号
    static final String CURRENT_CONFIG_VERSION = "3.0";
    // 颜色代码正则表达式
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("([&§])[0-9a-fk-orA-FK-OR]");

    @Override
    public void onEnable() {
        // 插件自检
        autoCheck();
        loadPlaceholderExpansion();
        reloadConfig();

        // 注册命令
        getCommand("customservernameapi").setExecutor(this);
        getCommand("customservernameapi").setTabCompleter(this);
    }

    public void autoCheck() {
        // 检查配置文件是否存在
        if (!new File(getDataFolder(), "config.yml").exists()) {
            getLogger().info("配置文件不存在，已加载默认配置文件");
            // 若配置文件不存在，保存默认配置文件
            saveDefaultConfig();
            loadMsg();
        } else {
            // 若配置文件存在，检查配置文件版本
            checkConfigVersion();
        }
    }

    public void checkConfigVersion() {
        // 检查配置文件版本号
        String configVersion = getConfig().getString("version");
        if (!Objects.equals(configVersion, CURRENT_CONFIG_VERSION)) {
            getLogger().warning("检测到旧版本配置文件，正在更新...");
            // 更新配置文件中的 version 值
            getConfig().set("version", CURRENT_CONFIG_VERSION);
            saveConfig();
            // 删除处理方式不妥当，后续版本更新会导致配置文件变为默认
            // 删除旧的配置文件
            // File configFile = new File(getDataFolder(), "config.yml");
            // if (configFile.delete()) {
            // 重新保存默认配置文件
            // saveDefaultConfig();
            //     getLogger().info("配置文件已更新到最新版本");
            //     reloadConfig();
            // } else {
            //     getLogger().warning("无法删除旧配置文件，请手动删除后重试");
            // }
        }
    }

    @Override
    public void reloadConfig() {
        // 重载配置文件时的处理
        super.reloadConfig();
        if (placeholderExpansion != null) {
            placeholderExpansion.unregister();
            loadPlaceholderExpansion();
        }
    }

    public void loadPlaceholderExpansion() {
        // 注册 PlaceholderExpansion
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderExpansion = new PlaceholderExpansion(this);
            placeholderExpansion.register();
        } else {
            getLogger().warning("PlaceholderAPI 未找到，本插件主要功能将无法使用。");
        }
    }

    public void loadMsg() {
        // 加载插件时输出信息
        getLogger().info("CustomServerNameAPI 已启用");
        getLogger().info("定制插件找落雨，买插件上速德优，速德优（北京）网络科技有限公司出品，落雨QQ：1498640871");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomServerNameAPI 已禁用");
        // 插件关闭时的处理
        if (!getConfig().getBoolean("persist")) {
            if (placeholderExpansion != null) {
                placeholderExpansion.unregister();
            }
        }
    }

    public String getServerName() {
        return getConfig().getString("server-name.name", "Default Server Name");
    }

    public String getServerAlias() {
        return getConfig().getString("server-name.alias", "Alias");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("customservernameapi")) {
            // 对一级命令进行权限检查
            // 对于 plugin.yml 中已定义权限节点，无需额外检查
            // if (!sender.hasPermission("customservernameapi.admin")) {
            //     sender.sendMessage("你没有权限使用此命令。");
            //     return true;
            // }
            if (args.length == 0) {
                // 输入一级命令 /customservernameapi 时输出默认文本
                showHelpMessage(sender);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    sender.sendMessage("§7[§4!§7] §a配置文件已重新加载。");
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    sender.sendMessage("§7[§4!§7] §eupdate 可用子命令：name, alias, autogeneratealias");
                    return true;
                } else if (args[0].equalsIgnoreCase("togglepersist")) {
                    boolean currentPersist = getConfig().getBoolean("persist", false);
                    getConfig().set("persist", !currentPersist);
                    saveConfig();
                    // 重新加载配置以更新持久化开关
                    reloadConfig();
                    sender.sendMessage("§7[§4!§7] §e持久拓展已" + (currentPersist ? "§c关闭" : "§a开启"));
                    return true;
                } else if (args[0].equalsIgnoreCase("info")) {
                    // 查询配置文件中的各个设置项状态
                    sender.sendMessage("§6==========配置文件设置项状态==========");
                    sender.sendMessage("§e配置文件版本: §a" + getConfig().getString("version", "未知"));
                    sender.sendMessage("§e服务器名称: §a" + placeholderExpansion.translateColorCodes(getServerName()));
                    sender.sendMessage("§e服务器别名: §a" + placeholderExpansion.translateColorCodes(getServerAlias()));
                    sender.sendMessage("§e自动生成别名功能: §a" + (getConfig().getBoolean("autogeneratealias", true) ? "开启" : "关闭"));
                    sender.sendMessage("§e持久化开关: §a" + (getConfig().getBoolean("persist", false) ? "开启" : "关闭"));
                    return true;
                } else if (args[0].equalsIgnoreCase("help")) {
                    showHelpMessage(sender);
                    return true;
                } else {
                    sender.sendMessage("§7[§4!§7] §c无效的子命令");
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("update")) {
                    if (args[1].equalsIgnoreCase("name")) {
                        sender.sendMessage("§a用法: /customservernameapi update name <新的服务器名称>");
                        return true;
                    } else if (args[1].equalsIgnoreCase("alias")) {
                        sender.sendMessage("§a用法: /customservernameapi update alias <新的服务器别名>");
                        return true;
                    } else if (args[1].equalsIgnoreCase("autogeneratealias")) {
                        boolean currentAutoGenerate = getConfig().getBoolean("autogeneratealias", true);
                        getConfig().set("autogeneratealias", !currentAutoGenerate);
                        saveConfig();
                        sender.sendMessage("§7[§4!§7] §e自动生成别名功能已" + (currentAutoGenerate ? "§c关闭" : "§a开启" + "\n §c注意：如您设置的内容为中文或其他非ASCII字符，则自动生成别名功能将失效"));
                        return true;
                    } else {
                        sender.sendMessage("§7[§4!§7] §c无效的子命令");
                        return true;
                    }
                } else {
                    sender.sendMessage("§7[§4!§7] §c无效的子命令");
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("update")) {
                    if (args[1].equalsIgnoreCase("name")) {
                        String newServerName = args[2];
                        getConfig().set("server-name.name", newServerName);
                        // 检查是否自动生成别名
                        String newAlias = null;
                        if (getConfig().getBoolean("autogeneratealias", true)) {
                            newAlias = generateAlias(newServerName);
                            if (newAlias != null) {
                                getConfig().set("server-name.alias", newAlias);
                            }
                        }
                        saveConfig();
                        // 重新加载配置以更新占位符
                        reloadConfig();
                        sender.sendMessage("§7[§4!§7] §a服务器名称已更新为: " + placeholderExpansion.translateColorCodes(newServerName));
                        if (getConfig().getBoolean("autogeneratealias", true) && newAlias != null && !newAlias.equals("&") && !newAlias.equals("§")) {
                            sender.sendMessage("§7[§4!§7] §a服务器别名已自动更新为: " + placeholderExpansion.translateColorCodes(newAlias));
                        } else if (getConfig().getBoolean("autogeneratealias", true) && !generateAliasStatus(newServerName)) {
                            sender.sendMessage("§7[§4!§7] §c您设置的内容为中文或其他非ASCII字符，自动生成别名功能已失效");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("alias")) {
                        String newServerAlias = args[2];
                        getConfig().set("server-name.alias", newServerAlias);
                        saveConfig();
                        // 重新加载配置以更新占位符
                        reloadConfig();
                        sender.sendMessage("§7[§4!§7] §a服务器别名已更新为: " + newServerAlias);
                        return true;
                    } else {
                        sender.sendMessage("§7[§4!§7] §c无效的子命令");
                        return true;
                    }
                } else {
                    sender.sendMessage("§7[§4!§7] §c无效的子命令");
                    return true;
                }
            } else {
                sender.sendMessage("§7[§4!§7] §c无效的子命令");
                return true;
            }
        }
        return false;
    }

    private String generateAlias(String name) {
        // 查找所有颜色代码
        Matcher matcher = COLOR_CODE_PATTERN.matcher(name);
        int lastColorCodeIndex = -1;
        while (matcher.find()) {
            lastColorCodeIndex = matcher.end();
        }
        // 如果有颜色代码，取最后一个颜色代码后的第一个字符
        if (lastColorCodeIndex != -1 && lastColorCodeIndex < name.length()) {
            String firstCharAfterLastColorCode = name.substring(lastColorCodeIndex, lastColorCodeIndex + 1);
            if (Pattern.matches("^[a-zA-Z0-9]", firstCharAfterLastColorCode)) {
                return firstCharAfterLastColorCode.toUpperCase();
            }
        }
        // 没有颜色代码或者颜色代码后没有合适字符，使用原逻辑
        if (Pattern.matches("^[a-zA-Z0-9].*", name)) {
            return name.substring(0, 1).toUpperCase();
        }
        return null;
    }

    private boolean generateAliasStatus(String name) {
        // 返回中文或其他非ASCII字符状态
        return generateAlias(name) != null;
    }

    private void showHelpMessage(CommandSender sender) {
        sender.sendMessage("§6==========CustomServerNameAPI==========");
        sender.sendMessage("§a以下均可用命令缩写：/csna");
        sender.sendMessage("§e/customservernameapi reload §7- §a重新加载配置文件");
        sender.sendMessage("§e/customservernameapi update name §7- §a更新服务器名称");
        sender.sendMessage("§e/customservernameapi update alias §7- §a更新服务器别名");
        sender.sendMessage("§e/customservernameapi update autogeneratealias §7- §a自动生成别名开关");
        sender.sendMessage("§e/customservernameapi togglepersist §7- §a开关变量持久化功能");
        sender.sendMessage("§e/customservernameapi info §7- §a查询配置文件中的各个设置项状态");
        sender.sendMessage("§e/customservernameapi help §7- §a显示此帮助信息");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // 命令补全
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("customservernameapi")) {
            if (args.length == 1) {
                if ("reload".startsWith(args[0].toLowerCase())) {
                    completions.add("reload");
                }
                if ("update".startsWith(args[0].toLowerCase())) {
                    completions.add("update");
                }
                if ("togglepersist".startsWith(args[0].toLowerCase())) {
                    completions.add("togglepersist");
                }
                if ("info".startsWith(args[0].toLowerCase())) {
                    completions.add("info");
                }
                if ("help".startsWith(args[0].toLowerCase())) {
                    completions.add("help");
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("update")) {
                if ("name".startsWith(args[1].toLowerCase())) {
                    completions.add("name");
                }
                if ("alias".startsWith(args[1].toLowerCase())) {
                    completions.add("alias");
                }
                if ("autogeneratealias".startsWith(args[1].toLowerCase())) {
                    completions.add("autogeneratealias");
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("update") && (args[1].equalsIgnoreCase("name"))) {
                completions.add("<新的名称>");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("update") && (args[1].equalsIgnoreCase("alias"))) {
                completions.add("<新的别名>");
            }
        }
        return completions;
    }
}