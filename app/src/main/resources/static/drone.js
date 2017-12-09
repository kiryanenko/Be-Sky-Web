var socket;

function connectDrone(){
    console.log('connectDrone');
    socket = new WebSocket("ws://be-sky.xyz/drone");
    socket.onmessage = function(event) {
        printVector(JSON.parse(event.data));
    };
}

function printVector(vector) {
    var res = '';
    if (vector['forward'] === 1) {
        res = 'Вперед; '
    } else if (vector['forward'] === -1) {
        res = 'Назад; '
    }
    if (vector['trust'] === 1) {
        res += 'Вверх; '
    } else if (vector['trust'] === -1) {
        res += 'Вниз; '
    }
    if (vector['rotation'] === 1) {
        res += 'Поворот вправо; '
    } else if (vector['rotation'] === -1) {
        res += 'Поворот влево; '
    }
    if (vector['side'] === 1) {
        res += 'Вправо в бок; '
    } else if (vector['side'] === -1) {
        res += 'Влево в бок; '
    }
    console.log(vector);
    console.log(res);
    var field = document.getElementById('drone-vector');
    field.innerHTML = res
}