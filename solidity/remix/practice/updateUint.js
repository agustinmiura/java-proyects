(async() => {
    const address = ""
    const abiArray = []

    const contractInstance = new web3.eth.Contract(abiArray, address);
    console.log(await contractInstance.methods.myUint().call())
    
    let accounts = await web3.eth.getAccounts();
    let trxResult = await contractInstance.methods.setMyUint(345).send({from: accounts[0]})

    console.log(await contractInstance.methods.myUint().call())
    console.log(" I see the trxResult : ",trxResult);
})()