import { ethers } from "ethers";
import { TransactionRequest } from "@ethersproject/abstract-provider";
import { keccak256 } from "ethers/lib/utils";
import { toUtf8Bytes } from "ethers/lib/utils";

let provider = new ethers.providers.JsonRpcProvider("https://moonbase-alpha.public.blastapi.io");
let privateKey = '0xe57432df862d2c4708c1ff2d1ae3079725b409c419d6a9f4f2d40ed361923b68';
let wallet = new ethers.Wallet(privateKey, provider);

let contract_address = "0xCc624b18F5A25e348Ed58E9f121597B4D5BEA901";

async function query_block_info() {
    let num = await provider.getBlockNumber();
    console.log(`block number: ${num}`);

    let gas = await provider.getGasPrice();
    console.log(`gas price: ${gas}`);

    let block = await provider.getBlock(num);
    console.log(`block: ${block}`);

    let tx_receipt = await provider.getTransactionReceipt(block.transactions[0]);
    console.log(`tx receipt: ${tx_receipt}`);
}

async function query_balance(account: string) {
    let balance = await provider.getBalance(account);
    const convert = ethers.utils.formatEther(balance);
    console.log(`address ${account} balance: ${convert} Dev`);
}

async function get_token_balance(account: string) {
    let sender = await wallet.getAddress();
    let nonce = await provider.getTransactionCount(sender, "latest");

    let funcSignature = keccak256(toUtf8Bytes("balanceOf(address)"));
    let abi = new ethers.utils.AbiCoder();
    let result = abi.encode(["address"], [account]);
    let data = ethers.utils.hexConcat([ethers.utils.hexDataSlice(funcSignature, 0, 4), result]);
    let require: TransactionRequest = {
        from: sender,
        to: contract_address,
        nonce: nonce,
        data: data
    };

    let response = await wallet.call(require);
    console.log(`response: ${response}`);

    let decode = ethers.utils.formatUnits(abi.decode(["uint256"], response)[0], 18);
    console.log(`token balance: ${decode}`);
}

async function transfer_token(to: string, amount: number) {
    let gas = await provider.getGasPrice();
    let sender = await wallet.getAddress();
    let nonce = await provider.getTransactionCount(sender, "latest");

    let funcSignature = keccak256(toUtf8Bytes("transfer(address,uint256)"));
    let abi = new ethers.utils.AbiCoder();
    let result = abi.encode(["address", "uint256"], [to, ethers.utils.parseUnits(`${amount}`, 18)]);
    let data = ethers.utils.hexConcat([ethers.utils.hexDataSlice(funcSignature, 0, 4), result]);

    let require: TransactionRequest = {
        from: sender,
        to: contract_address,
        nonce: nonce,
        gasLimit: ethers.utils.hexlify(100000),
        gasPrice: gas,
        data: data
    };

    let response = await wallet.sendTransaction(require);
    let receipt = await response.wait();
    console.log("tx hash:" + receipt.transactionHash);
    console.log("receipt: " + receipt);
}

async function main() {
    let address = await wallet.getAddress();
    console.log("...");
    await query_block_info();
    console.log("...");
    await query_balance(address);
    console.log("...");
    await get_token_balance(address);
    console.log("...");
    let to = "0xDCa72B2392FA69b1ea984a423975b1036cae94d8";
    await get_token_balance(to);
    await transfer_token(to, 1000);
    await get_token_balance(to);
}

main().catch((e) => {
    console.error(e);
})