//SPDX-License-Identifier: MIT

pragma solidity 0.8.15;

contract BlockChainMessenger {

    uint public changeCounter;

    address public owner;

    string public message;

    constructor() {
        owner = msg.sender;
    }

    function updateaMessage(string memory _message) public {
        if (msg.sender == owner) {
            message = _message;
            changeCounter++;
        }
    }

}