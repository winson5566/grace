var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/price', function (message) {  //订阅价格
            showPrice(JSON.parse(message.body).content);
        });
        stompClient.subscribe('/topic/margin', function (message) {     //订阅价差
            showMargin(JSON.parse(message.body).content);
        });
    });
}

function showPrice(message) {
    var obj =JSON.parse(message);
    $("#p00000").text(obj.data['00000'].price);
    $("#p00001").text(obj.data['00001'].price);
    $("#p00010").text(obj.data['00010'].price);
    $("#p00011").text(obj.data['00011'].price);
    $("#p01000").text(obj.data['01000'].price);
    $("#p01001").text(obj.data['01001'].price);
    $("#p01010").text(obj.data['01010'].price);
    $("#p01011").text(obj.data['01011'].price);
}

function showMargin(message) {
    var obj =JSON.parse(message);
    $("#margin_btc_00_10").text(obj.data['100100'].margin);
    $("#margin_ltc_00_10").text(obj.data['100101'].margin);
    $("#margin_btc_10_00").text(obj.data['110000'].margin);
    $("#margin_ltc_10_00").text(obj.data['110001'].margin);
    $("#deltaTime_btc_00_10").text(obj.data['100100'].deltaTime);
    $("#deltaTime_ltc_00_10").text(obj.data['100101'].deltaTime);
    $("#deltaTime_btc_10_00").text(obj.data['110000'].deltaTime);
    $("#deltaTime_ltc_10_00").text(obj.data['110001'].deltaTime);
}

window.onload = connect;

