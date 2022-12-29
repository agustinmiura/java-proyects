//SPDX-License-Identifier: MIT

pragma solidity ^0.8.16;

contract SampleUnits {

    uint runUntilTimestamp;
    uint startTimestamp;

    modifier betweenOneAndTwoEth() {
        require(msg.value >= 1 ether && msg.value <= 2 ether);
        _;
    }

    constructor(uint startInDays) {
        startTimestamp = block.timestamp + (startInDays * 1 days);
        runUntilTimestamp = startTimestamp + (7 days);
    }

}