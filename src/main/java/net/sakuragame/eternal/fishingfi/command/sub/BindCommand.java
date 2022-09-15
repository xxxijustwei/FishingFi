package net.sakuragame.eternal.fishingfi.command.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.fishingfi.Main;
import net.sakuragame.eternal.justmessage.api.common.InputBox;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BindCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "bind";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = this.getPlayer();
        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            InputBox box = new InputBox("address_bind", "&6&l绑定钱包", "&7请输入你的钱包公钥地址");
            box.open(player, false);
            return;
        }

        String address = args[0];
        Main.updateAccount(uuid, address);
        player.sendMessage(MegumiUtil.onReplace(" &7已绑定钱包地址: &f" + address));
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
