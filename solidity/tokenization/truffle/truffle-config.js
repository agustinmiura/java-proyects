const HDWalletProvider = require("@truffle/hdwallet-provider");
const fs = require('fs');
const mnemonicPhrase = fs.readFileSync(".secret").toString().trim();
const infuraProjectID = fs.readFileSync(".infura").toString().trim();

module.exports = {
  networks: {
    ganache: {
      host: "127.0.0.1",
      port: 8545,
      network_id: "*"
    },
    goerli: {
      provider: () =>
          new HDWalletProvider({
          mnemonic: {
              phrase: mnemonicPhrase
          },
          providerOrUrl: "https://goerli.infura.io/v3/" + infuraProjectID
          }),
      network_id: '5',
      }
  },

  mocha: {
  },
  // Configure your compilers
  compilers: {
    solc: {
      version: "0.8.17",      // Fetch exact version from solc-bin (default: truffle's version)
      // docker: true,        // Use "0.5.1" you've installed locally with docker (default: false)
      settings: {          // See the solidity docs for advice about optimization and evmVersion
       optimizer: {
         enabled: false,
         runs: 200
       },
      //  evmVersion: "byzantium"
      }
    }
  },
  plugins: ['truffle-plugin-verify'],
  api_keys: {
    etherscan: fs.readFileSync('.etherscan').toString().trim()
  }

};
