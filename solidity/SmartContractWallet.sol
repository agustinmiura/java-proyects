//SPDX-License-Identifier: MIT

pragma solidity 0.8.17;

contract Consumer {
    function getBalance() public view returns (uint) {
        return address(this).balance;
    }
    function deposit() public payable{}
}

contract SmartWalletWallet {

    address payable public owner;

    mapping(address => uint) public allowance;
    mapping(address => bool) public isAllowedToSend;

    mapping(address => bool) public guardians;
    address payable nextOwner;
    mapping(address => mapping(address => bool)) nextOwnerGuardianVotedBool;
    uint guardiansResetCount;
    uint public constant confirmationsFromGuardiansForReset = 3;

    constructor() {
        owner = payable(msg.sender);
    }

    function proposeNewOwner(address payable _newOwner) public {
        require(guardians[msg.sender], "You are not the guardian of this wallet.");
        require(nextOwnerGuardianVotedBool[_newOwner][msg.sender] == false, "You already voted");
        if (_newOwner != nextOwner) {
            nextOwner = _newOwner;
            guardiansResetCount = 0;
        }
        guardiansResetCount++;
        if (guardiansResetCount >= confirmationsFromGuardiansForReset) {
            owner = nextOwner;
            nextOwner = payable(address(0));
        }
    }

    function setGuardian(address _guardian, bool _isGuardian) public {
        require(msg.sender == owner, "You are not the owner");
        guardians[_guardian] = _isGuardian;
    } 

    function setAllowance(address _for, uint _amount) public {
        require(msg.sender == owner, "You are not the owner");
        allowance[_for] = _amount;

        if (_amount > 0) {
            isAllowedToSend[_for] = true;
        } else {
            isAllowedToSend[_for] = false;
        }
    }

    function transfer(address payable _to, uint _amount, bytes memory _payload) public returns(bytes memory) {
        require(msg.sender == owner, "You are not the owner , stopping");
        if (msg.sender != owner) {
            require(allowance[msg.sender] >= _amount, "Cannot send more than existing");
            require(isAllowedToSend[msg.sender], "You cannot send ");
        } 
        (bool success, bytes memory returnData) = _to.call{value: _amount}(_payload);
        require(success, "Aborting call not successful");
        return returnData;
    }

    receive() external payable {}

}



