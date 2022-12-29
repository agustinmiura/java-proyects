const Spacebears = artifacts.require("SpaceBear");

module.exports = function(deployer, network, accounts) {
    console.log(network, accounts);
    //deployer.deploy(token, arg1, arg2 .., {from: accounts[0]})
    deployer.deploy(Spacebears);
}