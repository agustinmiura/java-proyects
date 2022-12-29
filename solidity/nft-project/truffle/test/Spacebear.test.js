const Spacebear = artifacts.require("SpaceBear");
const truffleAssert = require('truffle-assertions');

contract("SpaceBear", (accounts) => {
    it('should credit an NFT to a specific account', async () => {
        const spaceBear = await Spacebear.deployed();
        let txResult = await spaceBear.safeMint(accounts[1],"spacebear_1.json");
        truffleAssert.eventEmitted(txResult, 'Transfer', {from: '0x0000000000000000000000000000000000000000', to: accounts[1], tokenId: web3.utils.toBN("0")});
        assert.equal(await spaceBear.ownerOf(0), accounts[1], "NFT was not credited to the correct account")
    });
});