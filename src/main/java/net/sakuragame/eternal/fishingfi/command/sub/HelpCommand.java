package net.sakuragame.eternal.fishingfi.command.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage("/ffi bind - 绑定钱包");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
