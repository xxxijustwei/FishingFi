package net.sakuragame.eternal.fishingfi.command.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.fishingfi.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class BalanceCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "balance";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = this.getPlayer();
        String address = Main.getAccount(player.getUniqueId());

        Function function = new Function(
                "balanceOf",
                Collections.singletonList(new Address(address)),
                Collections.singletonList(new TypeReference<Uint256>() {})
        );

        String encodeFunc = FunctionEncoder.encode(function);
        try {
            EthCall response = Main.getWeb3j().ethCall(
                    Transaction.createEthCallTransaction(
                            address,
                            Main.contractAddress,
                            encodeFunc
                    ),
                    DefaultBlockParameterName.LATEST
            ).sendAsync().get();

            List<Type> result = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
            double balance = new BigDecimal(((Uint256) result.get(0)).getValue()).doubleValue();
            player.sendMessage(MegumiUtil.onReplace(" &7余额: " + balance + " FISH"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
