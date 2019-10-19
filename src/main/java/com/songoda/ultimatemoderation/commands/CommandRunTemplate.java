package com.songoda.ultimatemoderation.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.ultimatemoderation.UltimateModeration;
import com.songoda.ultimatemoderation.punish.template.Template;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandRunTemplate extends AbstractCommand {

    private UltimateModeration instance;

    public CommandRunTemplate(UltimateModeration instance) {
        super(CommandType.CONSOLE_OK, "RunTemplate");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length < 2)
            return ReturnType.SYNTAX_ERROR;

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        if (player == null) {
            instance.getLocale().newMessage("That player does not exist.").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        StringBuilder templateBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            String line = args[i];
            templateBuilder.append(line).append(" ");
        }
        String templateStr = templateBuilder.toString().trim();

        Template template = instance.getTemplateManager().getTemplate(templateStr);

        if (template == null) {
            sender.sendMessage("That template does not exist...");
            return ReturnType.FAILURE;
        }

        template.execute(sender, player);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
            return players;
        } else if (args.length == 2) {
            List<String> lines = new ArrayList<>();
            for (Template template : instance.getTemplateManager().getTemplates().values()) {
                lines.add(template.getTemplateName());
            }
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "um.template";
    }

    @Override
    public String getSyntax() {
        return "/RunTemplate <player> <template>";
    }

    @Override
    public String getDescription() {
        return "Allows you to use templates on players.";
    }
}