package net.sakuragame.eternal.fishingfi.listener;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.fishing.api.event.FishCaughtEvent;
import net.sakuragame.eternal.fishingfi.Main;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.justmessage.api.event.input.InputBoxConfirmEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonListener implements Listener {

    private final int cycle = 6;
    private final Map<UUID, Integer> count = new HashMap<>();

    @EventHandler
    public void onBind(InputBoxConfirmEvent e) {
        Player player = e.getPlayer();
        if (!e.getKey().equals("address_bind")) return;

        String address = e.getContent();
        Main.updateAccount(player.getUniqueId(), address);
        player.sendMessage(MegumiUtil.onReplace(" &7已绑定钱包地址: &f" + address));
        player.closeInventory();
    }

    @EventHandler
    public void onCaught(FishCaughtEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        int current = count.merge(uuid, 1, Integer::sum);
        if (current < cycle) {
            MessageAPI.sendInformMessage(player, " &7奖励进度: &f" + current + "/" + cycle);
            return;
        }

        this.count.remove(uuid);
        this.mintReward(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Main.removeAccount(player.getUniqueId());
    }

    private void mintReward(Player player) {
        UUID uuid = player.getUniqueId();
        MessageAPI.sendInformMessage(player, " &a开发发放钓鱼奖励...");

        Main.getContract().mint(
                Main.getAccount(uuid),
                BigInteger.valueOf(1_000_000_000_000_000_000L)
        ).sendAsync()
                .thenAccept(receipt -> {
                    MessageAPI.sendInformMessage(player, "&8[&e+&8] &31 FISH Token");
                    player.sendMessage(MegumiUtil.onReplace(
                            " &7奖励已发放，Tx hash: &f" + receipt.getTransactionHash()
                    ));
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }
}
