var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //console.log('Connected: ' + frame);
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
    $("#okcoin-btc-buy-price").text(obj.data['00000'].price);
    $("#okcoin-btc-sell-price").text(obj.data['00001'].price);
    $("#okcoin-btc-last-price").text((obj.data['00001'].price+obj.data['00000'].price)/2);
    $("#okcoin-btc-time-price").text(obj.data['00001'].timestamp);

    $("#okcoin-ltc-buy-price").text(obj.data['00010'].price);
    $("#okcoin-ltc-sell-price").text(obj.data['00011'].price);
    $("#okcoin-ltc-time-price").text(obj.data['00010'].timestamp);

    $("#bitvc-btc-buy-price").text(obj.data['01000'].price);
    $("#bitvc-btc-sell-price").text(obj.data['01001'].price);
    $("#bitvc-btc-time-price").text(obj.data['01000'].timestamp);

    $("#bitvc-ltc-buy-price").text(obj.data['01010'].price);
    $("#bitvc-ltc-sell-price").text(obj.data['01011'].price);
    $("#bitvc-ltc-time-price").text(obj.data['01010'].timestamp);

}

function showMargin(message) {
    var obj =JSON.parse(message);
    $("#margin-btc-okcoin-bitvc").text(obj.data['100100'].margin);
    $("#margin-ltc-okcoin-bitvc").text(obj.data['100101'].margin);
    $("#margin-btc-bitvc-okcoin").text(obj.data['110000'].margin);
    $("#margin-ltc-bitvc-okcoin").text(obj.data['110001'].margin);
    $("#time-btc-okcoin-bitvc").text(obj.data['100100'].deltaTime);
    $("#time-ltc-okcoin-bitvc").text(obj.data['100101'].deltaTime);
    $("#time-btc-bitvc-okcoin").text(obj.data['110000'].deltaTime);
    $("#time-ltc-bitvc-okcoin").text(obj.data['110001'].deltaTime);
}

window.onload = connect;

