const express = require('express');
const app = express();

const cors = require('cors');

/*
app.use(cors({
    methods: ['GET','POST','DELETE','UPDATE','PUT','PATCH'],
    origin: '*'
}));
*/

app.get('/', (req, res) =>{
    console.log(" Request ")
    var object = {success:true}
    res.json(object)
});

app.get('/health', (req, res) =>{
    try {
        console.log(" Request ")
        var object = {success:true}
        res.json(object)
    } catch(e) {
        console.log("Error")
        var object = {success:false}
        res.json(object)  
    }
   
});
var port = 9000

app.listen(9000, () => {
    console.log(`Example app listening at http://localhost:${port}`)
  })

  app.use((error, req, res, next) => {
    console.log("Error Handling Middleware called")
    console.log(" Error : ",error);
    console.log('Path: ', req.path)
    next() // (optional) invoking next middleware
  })