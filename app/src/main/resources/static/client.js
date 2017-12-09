var socket;
var keys = [];

function keydown(event){
    var code = event.keyCode;
    if (keys.indexOf(code)<0){
        keys.push(code);
    }
    console.log(keys);
    onKey()
}

function keyup(event){
    keys.splice(keys.indexOf(event.keyCode),1);
    console.log(keys);
    onKey()
}


function connectClient() {
    socket = new WebSocket("ws://be-sky.xyz/client");
    socket.onclose = function (event) {
        console.log('Код: ' + event.code + ' причина: ' + event.reason);
        connectClient();
    }
}


function onKey() {
    var vector = {};
    keys.forEach(function (key) {
        if (key === 87) {
            vector['forward'] = 1;
        } else if (key === 83) {
            vector['forward'] = -1;
        }
        if (key === 39) {               // стрелка вправо
            vector['rotation'] = 1;
        } else if (key === 37) {        // стрелка влево
            vector['rotation'] = -1;
        }
        if (key === 38) {               // стрелка вверх
            vector['trust'] = 1;
        } else if (key === 40) {        // стрелка вниз
            vector['trust'] = -1;
        }
        if (key === 68) {
            vector['side'] = 1;
        } else if (key === 65) {
            vector['side'] = -1;
        }
    });
    socket.send(JSON.stringify(vector));
}