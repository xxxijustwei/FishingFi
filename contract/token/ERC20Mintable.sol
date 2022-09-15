// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

abstract contract ERC20Mintable is ERC20 {

    bool private _mintComplete = false;

    event MintCompleted();

    modifier canMint() {
        require(!_mintComplete, "minting is completed");
        _;
    }

    function mint(address _account, uint _amount) public canMint {
        _mint(_account, _amount);
    }

    function completeMint() public canMint {
        _completeMint();
    }

    function _completeMint() internal virtual {
        _mintComplete = true;
        emit MintCompleted();
    }
}