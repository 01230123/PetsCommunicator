const express = require('express');
const app = express();
const multer = require('multer');
const ejs = require('ejs');
const path = require('path');
const { readdirSync, fs } = require('fs');
const { dir } = require('console');

const getDirectories = source =>
  readdirSync(source, { withFileTypes: true })
    .filter(dirent => dirent.isDirectory())
    .map(dirent => dirent.name)


app.use(express.json());
app.use(express.urlencoded({extended: false}));

const storage = multer.diskStorage({
    destination: './public/audios',
    filename: function(req, file, cb)
    {
        cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
    }
})


let dirs = getDirectories("./public/petSound/");

const getFileNames = (dir) =>{
    let names = readdirSync('./public/petSound/' + dir + "/");
    return names.map((n) =>{
        return dir + "/" + n;
    })
}



const upload = multer({
    storage: storage
}).single('audio');

const port = 3000
app.listen(port, () => {
    console.log(`Listening on port ${port}...`);
})


app.get('/download/:filename', (req, res) =>{
    console.log(req.body);
    console.log(req.params.filename);
    res.sendFile(__dirname + '/public/petSound/' + req.params.filename);
})


app.get('/', (req, res) =>{
    const soundList = dirs.map((dir) => {
        return getFileNames(dir);
    })

    let message = {
        dogSounds: soundList,
    };

    console.log(message);

    res.status(200).send(JSON.stringify(message))
});

app.get('/getSounds', (req, res) =>{
    const soundList = dirs.map((dir) => {
        return getFileNames(dir);
    })

    soundList = ["34", "35", "dgg"];


    const message = {
        dogSounds: soundList,
    };

    console.log(message);

    // if (soundList.length <= 0)
    // {
    //     res.status(500);
    //     return;
    // }

    console.log("here");
    res.status(200).send(JSON.stringify(message))
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


