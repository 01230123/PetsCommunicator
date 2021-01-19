const express = require('express');
const app = express();
const multer = require('multer');
const ejs = require('ejs');
const path = require('path');

app.use(express.json());
app.use(express.urlencoded({extended: false}));

const storage = multer.diskStorage({
    destination: './public/audios',
    filename: function(req, file, cb)
    {
        cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
    }
})

const upload = multer({
    storage: storage
}).single('audio');

const port = 3000
app.listen(port, () => {
    console.log(`Listening on port ${port}...`);
})

app.get('/', (req, res) =>{
    res.send("Hello world!");
})

app.get('/download/:filename', (req, res) =>{
    console.log(req.body);
    console.log(req.params.filename);
    res.sendFile(__dirname + '/public/petSound/' + req.params.filename);
})

app.post('/upload', (req, res) =>
{
    const message = {
        name: 'Im so happy talking to you!',
    };

    upload(req, res, (err) =>
    {
        if (err){
            message.name = "error";
        }
        else{
            console.log(req.file);
        }
    })    

    

    res.status(200).send(JSON.stringify(message))
})


