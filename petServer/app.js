const express = require('express');
const app = express();
const multer = require('multer');
const ejs = require('ejs');
const path = require('path');
const { readdirSync} = require('fs');
const fs = require('fs');
const Meyda = require('meyda');
const { dir } = require('console');
const AV = require('av');
require('flac.js');


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
    console.log("here");
    let dirs = getDirectories("./public/petSound/");

    const soundList = dirs.map((dir) => {
        return getFileNames(dir);
    })

    let message = {
        dogSounds: soundList,
    };

    console.log(message);

    res.status(200).send(JSON.stringify(message))
});

const dogMsg = [
    // "I love you",
    // "I want to play with you!",
    // "Get away from me!!",
    // "You're in danger", 
    // "I'm not felling well",
    // "Go pet me",
    "Let's play outdoor\nヽ（≧□≦）ノ",
    "I'm very hungry\n   o(TヘTo)",
    "I can fly\no(*￣︶￣*)o",
    "What time is it?\no(〃＾▽＾〃)o",
    "I saw a giant bird!\nΣ(っ °Д °;)っ",
    "I'm thirsty\n（〃｀ 3′〃）",
    "I’m hungry\n＞︿＜.",
    "I need a girl friend!\n(┬┬﹏┬┬)",
    "Come with me\n( •̀ ω •́ )✧",
    "Follow me!\no(*°▽°*)o",
    "I want to play ball\n(/≧▽≦)/",
    "Glad to meet you\n(´▽`ʃ♡ƪ)",
    "I'm sleepy\n(✿◡‿◡)",
    "I’m only a child\n(❁´◡`❁)",
    "I smell something…\n(⊙ˍ⊙)",
    "It's so hot!\n(╬▔皿▔)╯"
];


app.post('/upload', (req, res) =>
{
    const randomMsg = dogMsg[Math.floor(Math.random() * dogMsg.length)];

    const message = {
        name: randomMsg,
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




let wav = require('node-wav');


let buffer = fs.readFileSync('100hz.wav');
let result = wav.decode(buffer);

const BUFFER_SIZE = 16384;

Meyda.bufferSize = BUFFER_SIZE;


let datas = result.channelData; // array of Float32Array
 

//https://stackoverflow.com/questions/32439437/retrieve-an-evenly-distributed-number-of-elements-from-an-array
function distributedCopy(items, n) {
    var elements = [items[0]];
    var totalItems = items.length - 2;
    var interval = Math.floor(totalItems/(n - 2));
    for (var i = 1; i < n - 1; i++) {
        elements.push(items[i * interval]);
    }
    elements.push(items[items.length - 1]);
    return elements;
}


//console.log(result.channelData.length);
//console.log(Meyda.extract('rms', extracted));
//console.log(Meyda.extract('perceptualSpread', extracted)); //Amplitude range


const { getAudioDurationInSeconds } = require('get-audio-duration');
 

// From a local path...
// getAudioDurationInSeconds('door.wav').then((duration) => {
//   console.log(duration);
// });


const getAmplitudeSpectrum = (signal) =>
{
    let data = distributedCopy(signal, BUFFER_SIZE);
    console.log(Meyda.extract('spectralCentroid', data));
    return Meyda.extract('amplitudeSpectrum', data);
} 





const meanFrequency = (array) =>
{
    let sum = 0;
    let sumFre = 0;
    let freMax = 0;
    let maxAmp = 0;
    for (let i = 0; i < array.length; i++){
        let a = array[i];
        if(a > maxAmp)
        {
            freMax = i;
            maxAmp = a;
        }
        sum += a*i;
        sumFre += a;
    };

    console.log(freMax, maxAmp);    
}

let average = (array) => array.reduce((a, b) => a + b) / array.length;

function findIndicesOfMax(inp, count) {
    var outp = [];
    for (var i = 0; i < inp.length; i++) {
        outp.push(i); // add index to output array
        if (outp.length > count) {
            outp.sort(function(a, b) { return inp[b] - inp[a]; }); // descending sort the output array
            outp.pop(); // remove the last index (index of smallest element in output array)
        }
    } 
    return average(outp);
}

let spec = getAmplitudeSpectrum(datas[0]);
console.log(findIndicesOfMax(spec, 10));

console.log(datas[0].length);
meanFrequency(spec);




