## Instructions

* Run the command `truffle develop`
* Run `migrate`

# Deploy in Ganache 

* Run the command : `ganache`
* Run the command : `truffle migrate --network ganache`

* Other commands :

* `const spaceBearInstance = away SpaceBear.deployed()`
* ` const accounts = await web3.eth.getAccounts()`
* `await spaceBearInstance.safeMint(accounts[1], "spacebear_1.json")`