// SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";

contract CofeeToken is ERC20, AccessControl {
    bytes32 public constant MINTER_ROLE = keccak256("MINTER_ROLE");

    event CofeePurchased(address indexed receiver, address indexed buyer);

    constructor() ERC20("CofeeToken", "CFE") {
        _mint(msg.sender, 1000 * 10 ** decimals());
        _grantRole(DEFAULT_ADMIN_ROLE, msg.sender);
        _grantRole(MINTER_ROLE, msg.sender);
    }

    function mint(address to, uint256 amount) public onlyRole(MINTER_ROLE) {
        _mint(to, amount);
    }

    function buyOneCofee() public {
        _burn(_msgSender(), 1);
        emit CofeePurchased(_msgSender());
    }

    function buyOneCofeeFrom(address account) public {
        _spendAllowance(account, _msgSender(), 1);
        _burn(account, 1);
        emit CofeePurchased(_msgSender(), account);
    }
}