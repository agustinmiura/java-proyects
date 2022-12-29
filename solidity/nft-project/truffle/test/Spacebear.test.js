const Spacebear = artifacts.require("SpaceBear");

contract("SpaceBear", (accounts) => {
    it('should credit an NFT to a specific account', async () => {
        const spaceBear = await Spacebear.deployed();
        let txResult = await spaceBear.safeMint(accounts[1],"spacebear_1.json");
        assert.equal(txResult.logs[0].event, "Transfer", "Transfer event was not emitted")
        assert.equal(txResult.logs[0].args.from, '0x0000000000000000000000000000000000000000', "Token was not transferred from the zero address");
        assert.equal(txResult.logs[0].args.to, accounts[1], "Receiver wrong address");
        assert.equal(txResult.logs[0].args.tokenId.toString(), "0", "Wrong Token ID minted");
        assert.equal(await spaceBear.ownerOf(0), accounts[1], "NFT was not credited to the correct account")
    });
});