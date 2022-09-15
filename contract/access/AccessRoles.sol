// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

import "@openzeppelin/contracts/access/AccessControl.sol";

contract AccessRoles is AccessControl {

    bytes32 public constant MINTER = keccak256("MINTER");

    constructor(address _minter) {
        _setupRole(DEFAULT_ADMIN_ROLE, _msgSender());
        _setupRole(MINTER, _minter);
    }

    modifier onlyOnwer() {
        require(hasRole(DEFAULT_ADMIN_ROLE, _msgSender()), "You are not an authorized user.");
        _;
    }

    modifier onlyMinter() {
        require(hasRole(MINTER, _msgSender()), "You are not an authorized user.");
        _;
    }
}