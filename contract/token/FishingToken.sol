// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

import "../access/AccessRoles.sol";
import "./ERC20Mintable.sol";

import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/token/ERC20/extensions/ERC20Burnable.sol";

// deploy network: moonbase alpha
// contract address: 0xCc624b18F5A25e348Ed58E9f121597B4D5BEA901
contract FishingToken is ERC20Mintable, ERC20Burnable, Ownable, AccessRoles {
    constructor(uint256 _initialSupply) 
        ERC20("FishingToken", "FISH")
        AccessRoles(_msgSender())
    {
        _mint(_msgSender(), _initialSupply);
    }

    function _mint(address _account, uint _amount) internal override onlyMinter {
        super._mint(_account, _amount);
    }

    function _completeMint() internal override onlyOnwer {
        super._completeMint();
    }
}