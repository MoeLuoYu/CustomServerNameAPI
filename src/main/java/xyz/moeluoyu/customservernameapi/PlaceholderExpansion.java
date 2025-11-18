package xyz.moeluoyu.customservernameapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        return plugin.getDescription().getVersion();
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
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 先处理基本的颜色代码替换
        String result = text.replace('&', '§');
        
        // 处理渐变效果
        result = processGradientColors(result);
        
        return result;
    }
    
    private String processGradientColors(String text) {
        // 先处理尖括号格式的渐变：<#RRGGBB>文本</#RRGGBB> 或 <#RRGGBB>文本
        String result = processBracketGradient(text, '<', '>');
        
        // 再处理大括号格式的渐变：{#RRGGBB}文本{/#RRGGBB} 或 {#RRGGBB}文本
        result = processBracketGradient(result, '{', '}');
        
        return result;
    }
    
    private String processBracketGradient(String text, char openBracket, char closeBracket) {
        // 构建正则表达式模式
        String startPattern = "\\" + openBracket + "#([A-Fa-f0-9]{6})\\" + closeBracket;
        String endPattern = "\\" + openBracket + "/#([A-Fa-f0-9]{6})\\" + closeBracket;
        
        // 完整的正则表达式 - 匹配开始标签、内容和结束标签
        String gradientRegex = startPattern + "(.*?)" + endPattern;
        Pattern gradientPattern = Pattern.compile(gradientRegex);
        Matcher gradientMatcher = gradientPattern.matcher(text);
        
        // 先处理有结束标签的渐变
        StringBuffer sb = new StringBuffer();
        while (gradientMatcher.find()) {
            String startColor = gradientMatcher.group(1);
            String content = gradientMatcher.group(2);
            String endColor = gradientMatcher.group(3);
            
            // 应用渐变效果
            String gradientText = applyGradient(content, startColor, endColor);
            gradientMatcher.appendReplacement(sb, gradientText);
        }
        gradientMatcher.appendTail(sb);
        
        // 再处理没有结束标签的单色
        String result = sb.toString();
        String singleColorRegex = startPattern + "([^" + openBracket + "]*)";
        Pattern singleColorPattern = Pattern.compile(singleColorRegex);
        Matcher singleColorMatcher = singleColorPattern.matcher(result);
        
        sb = new StringBuffer();
        while (singleColorMatcher.find()) {
            String color = singleColorMatcher.group(1);
            String content = singleColorMatcher.group(2);
            
            // 应用单色效果
            String singleColorText = "§x§" + color.charAt(0) + "§" + color.charAt(1) + 
                                     "§" + color.charAt(2) + "§" + color.charAt(3) + 
                                     "§" + color.charAt(4) + "§" + color.charAt(5) + 
                                     content;
            singleColorMatcher.appendReplacement(sb, singleColorText);
        }
        singleColorMatcher.appendTail(sb);
        
        return sb.toString();
    }
    
    private String applyGradient(String text, String startColor, String endColor) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 将十六进制颜色转换为RGB
        int startR = Integer.parseInt(startColor.substring(0, 2), 16);
        int startG = Integer.parseInt(startColor.substring(2, 4), 16);
        int startB = Integer.parseInt(startColor.substring(4, 6), 16);
        
        int endR = Integer.parseInt(endColor.substring(0, 2), 16);
        int endG = Integer.parseInt(endColor.substring(2, 4), 16);
        int endB = Integer.parseInt(endColor.substring(4, 6), 16);
        
        StringBuilder result = new StringBuilder();
        int length = text.length();
        
        for (int i = 0; i < length; i++) {
            // 计算当前字符的颜色插值
            double ratio = length > 1 ? (double) i / (length - 1) : 0;
            
            int r = (int) Math.round(startR + (endR - startR) * ratio);
            int g = (int) Math.round(startG + (endG - startG) * ratio);
            int b = (int) Math.round(startB + (endB - startB) * ratio);
            
            // 转换为十六进制
            String hexColor = String.format("%02X%02X%02X", r, g, b);
            
            // 添加Minecraft颜色代码格式
            result.append("§x§").append(hexColor.charAt(0)).append("§").append(hexColor.charAt(1))
                  .append("§").append(hexColor.charAt(2)).append("§").append(hexColor.charAt(3))
                  .append("§").append(hexColor.charAt(4)).append("§").append(hexColor.charAt(5))
                  .append(text.charAt(i));
        }
        
        return result.toString();
    }
}
