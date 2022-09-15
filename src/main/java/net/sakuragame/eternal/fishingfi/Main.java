package net.sakuragame.eternal.fishingfi;

import lombok.Getter;
import net.sakuragame.eternal.fishingfi.command.MainCommand;
import net.sakuragame.eternal.fishingfi.contract.FishingToken;
import net.sakuragame.eternal.fishingfi.listener.CommonListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    @Getter
    private static Web3j web3j;
    @Getter
    private static FishingToken contract;
    private static Map<UUID, String> account;

    private final static String minterPrivateKey = "0xe57432df862d2c4708c1ff2d1ae3079725b409c419d6a9f4f2d40ed361923b68";
    private final static String publicAddress = "0xDCa72B2392FA69b1ea984a423975b1036cae94d8";

    @Override
    public void onEnable() {
        account = new HashMap<>();

        this.initWeb3j();
        this.initContract();

        Bukkit.getPluginManager().registerEvents(new CommonListener(), this);
        getCommand("ffi").setExecutor(new MainCommand());

        getLogger().info("启动成功");
    }

    private void initWeb3j() {
        web3j = Web3j.build(new HttpService("https://moonbase-alpha.public.blastapi.io"));
        try {
            getLogger().info("Chain ID: " + web3j.netVersion().send().getNetVersion());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initContract() {
        contract = FishingToken.load(
                "0xCc624b18F5A25e348Ed58E9f121597B4D5BEA901",
                web3j,
                Credentials.create(minterPrivateKey),
                new DefaultGasProvider()
        );
    }

    @Override
    public void onDisable() {
        web3j.shutdown();
    }

    public static String getAccount(UUID uuid) {
        return account.getOrDefault(uuid, publicAddress);
    }

    public static void updateAccount(UUID uuid, String address) {
        account.put(uuid, address);
    }

    public static void removeAccount(UUID uuid) {
        account.remove(uuid);
    }
}
