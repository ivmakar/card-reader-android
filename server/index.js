const express = require('express');
const bodyParser = require('body-parser');
const mime = require('mime');
const fs = require("fs");

const {
    spawn
} = require('child_process');

var exec = require('child_process').exec;

const app = express()
const port = 3000

app.use(bodyParser.json({limit: '50mb', extended: true}))
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}))

const router = express.Router();

router.route('/')
.get((req, res, next) => {
    console.log("GET")

    res.send('{"image":["Ok!"]}')

})
.post((req, res, next) => {
    console.log("image received")
    var decodedImg = req.body.image;
    var imageBuffer = decodedImg;
    var fileName =  "image.jpeg";
    console.log(fileName)
    try{
          fs.writeFileSync("./uploads/" + fileName, imageBuffer, 'base64');
       }
    catch(err){
       console.error(err)
    }

    var dataToSend = "";
    // spawn new child process to call the python script
    // var command = "python3 ./cardscan/scan.py ./uploads/" + fileName;

    // exec(command, function (error, stdout, stderr) {
    //     if (error !== null) {
    //         console.log('exec error: ' + error);
    //         return;
    //     }
    
    //     console.log(`This runs second, after the script has executed: output => ${output}`); // prints out 'beep boop'

    //     res.send(stdout)    
    // });

    const python = spawn('python3', ['./cardscan/scan.py', './uploads/' + fileName]);
    python.stdout.on('data', function (data) {
        // console.log(data.toString());
        dataToSend = data.toString();
    });
    // in close event we are sure that stream from child process is closed
    python.on('close', (code) => {
        console.log(`child process close all stdio with code ${code}`);
        console.log(`data to send:`);
        var data = ""
        dataToSend.split('\n').forEach((value, index, array) => {data += "\"" + value + "\","} )
        dataToSend = '{"image":[' + data.substring(0, data.length - 1) + ']}'
        console.log(dataToSend);
        // send data to browser
        res.send(dataToSend);
    });


    // res.send('{"image":"Excelent!"}')
})

app.use('/scan', router)

app.listen(port, () => console.log(`Example app listening on port ${port}!`))