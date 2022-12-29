(async() => {
    try {
        let accounts = await web3.eth.getAccounts();
        console.log(" I see the accounts ",accounts);
        let balance = await web3.eth.getBalance(accounts[0])
        let balanceInEth = web3.utils.fromWei(balance, "eth")
        console.log("I see the balance : ",balance)
        console.log(balanceInEth)
    } catch(err) {
        console.log(" Error message : ",err.message);
    }
})()