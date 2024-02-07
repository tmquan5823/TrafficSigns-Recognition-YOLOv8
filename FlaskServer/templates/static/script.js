
var socket = io.connect(window.location.protocol + "//" + document.domain + ":" + location.port, { transports: ['websocket'] });


socket.on("connect", function () {
  console.log("Connected...!", socket.connected);
});

var content = document.getElementById("info"); 
var canvas = document.getElementById("canvas");
var context = canvas.getContext("2d");
const video = document.querySelector("#videoElement");

video.width = 400;
video.height = 300;

if (navigator.mediaDevices.getUserMedia) {
  navigator.mediaDevices
    .getUserMedia({
      video: true,
    })
    .then(function (stream) {
      video.srcObject = stream;
      video.play();
    })
    .catch(function (err0r) {});
}

const FPS = 5;
  setInterval(() => {
      width = video.width;
      height = video.height;
      context.drawImage(video, 0, 0, width, height);
      var data = canvas.toDataURL("image/jpeg", 0.5);
      context.clearRect(0, 0, width, height);
      console.log("Lấy ảnh");
      socket.emit("image", data);
  }, 900);

  socket.on("processed_image", function (data) {
    console.log("Tra ảnh");
    photo.setAttribute("src", data.image);
    while (content.firstChild) {
      content.removeChild(content.firstChild);
    }
    if(data.info !== []){
      for(let i=0; i<data.info.length; i++){
        var newDiv = document.createElement('div');
        newDiv.innerHTML = "<h1>" +data.info[i][0]+ ": " +data.info[i][1]+ "</h1>"
        content.appendChild(newDiv);
        var utterance = new SpeechSynthesisUtterance(data.info[i][1]);
        utterance.lang = 'vi-VN';
        window.speechSynthesis.speak(utterance);
      }
    }
    else {
      content.innerHTML = "<h3>Nothing!</h3>"
      window.speechSynthesis.cancel();
    }
  });