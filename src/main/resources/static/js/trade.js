var okCoinWebSocket = {};
okCoinWebSocket.init = function (uri) {
    this.wsUri = uri;

    okCoinWebSocket.websocket = new WebSocket(okCoinWebSocket.wsUri);

    okCoinWebSocket.websocket.onopen = function (evt) {
        onOpen(evt)
    };
    okCoinWebSocket.websocket.onclose = function (evt) {
        onClose(evt)
    };
    okCoinWebSocket.websocket.onmessage = function (evt) {
        onMessage(evt)
    };
    okCoinWebSocket.websocket.onerror = function (evt) {
        onError(evt)
    };

}
var bitvcWebSocket = {};
bitvcWebSocket.init = function () {
    var sh = setInterval(bitvc_btc, 1000);
    var sh = setInterval(bitvc_ltc, 1000);
}

function onOpen(evt) {
    print("CONNECTED");
    doSend("[{'event':'addChannel','channel':'ok_sub_spotcny_btc_ticker'},{'event':'addChannel','channel':'ok_sub_spotcny_ltc_ticker'}]");
}
function onClose(evt) {
    print("DISCONNECTED");
}

function onMessage(e) {
    var array = JSON.parse(e.data);
    for (var i = 0; i < array.length; i++) {
        for (var j = 0; j < array[i].length; j++) {
            var isTrade = false;
            var isCancelOrder = false;

            if (array[i][j] == 'ok_spotusd_trade' || array[i][j] == 'ok_spotcny_trade') {
                isTrade = true;
            } else if (array[i][j] == 'ok_spotusd_cancel_order'
                || array[i][j] == 'ok_spotcny_cancel_order') {
                isCancelOrder = true;
            }
            var order_id = array[i][j].order_id;
            if (typeof (order_id) != 'undefined') {
                if (isTrade) {
                    //下单成功 业务代码
                    console.log("orderId is  " + order_id);
                } else if (isCancelOrder) {
                    //取消订单成功 业务代码
                    console.log("order  " + order_id + " is now cancled");
                }
            }
        }
    }

    if (array.event == 'pong') {
        okCoinWebSocket.lastHeartBeat = new Date().getTime();
    } else {
        updateOkcoinPrice(array);
    }
}

function onError(evt) {
    print("ERROR: " + evt.data);
}

function doSend(message) {
    print("SENT: " + message);
    okCoinWebSocket.websocket.send(message);
}

function print(message) {
    console.log(new Date().getTime() + ": " + message)
}

function updateOkcoinPrice(array) {
    for (var i = 0; i < array.length; i++) {
        var channel = array[i].channel;
        if (channel == 'ok_sub_spotcny_btc_ticker' || channel == 'ok_sub_spotcny_ltc_ticker') {
            for (var i = 0; i < array.length; i++) {
                var channel = array[i].channel;
                for (var j in array[0]) {
                    if (j == 'data') {
                        var arr = array[i];
                        if (channel == 'ok_sub_spotcny_btc_ticker') {
                            $("#okcoin-btc-buy-price").text(arr.data.buy);
                            $("#okcoin-btc-sell-price").text(arr.data.sell);
                            $("#okcoin-btc-last-price").text(arr.data.last);
                            $("#okcoin-btc-time-price").text(arr.data.timestamp);
                            $("#margin-btc-okcoin-bitvc").text(($("#okcoin-btc-buy-price").text()*1.0 - $("#bitvc-btc-sell-price").text()*1.0).toFixed(2));
                            $("#margin-btc-bitvc-okcoin").text(($("#bitvc-btc-buy-price").text()*1.0 - $("#okcoin-btc-sell-price").text()*1.0).toFixed(2));
                            $("#time-btc-bitvc-okcoin").text(Math.abs(new Date($("#okcoin-btc-time-price").text()*1)-new Date($("#bitvc-btc-time-price").text()*1)));
                            $("#time-btc-okcoin-bitvc").text(Math.abs(new Date($("#okcoin-btc-time-price").text()*1)-new Date($("#bitvc-btc-time-price").text()*1)));
                        } else if (channel == 'ok_sub_spotcny_ltc_ticker') {
                            $("#okcoin-ltc-buy-price").text(arr.data.buy);
                            $("#okcoin-ltc-sell-price").text(arr.data.sell);
                            $("#okcoin-ltc-last-price").text(arr.data.last);
                            $("#okcoin-ltc-time-price").text(arr.data.timestamp);
                            $("#margin-ltc-okcoin-bitvc").text(($("#okcoin-ltc-buy-price").text()*1.0 - $("#bitvc-ltc-sell-price").text()*1.0).toFixed(2));
                            $("#margin-ltc-bitvc-okcoin").text(($("#bitvc-ltc-buy-price").text()*1.0 - $("#okcoin-ltc-sell-price").text()*1.0).toFixed(2));
                            $("#time-ltc-bitvc-okcoin").text(Math.abs(new Date($("#okcoin-ltc-time-price").text()*1)-new Date($("#bitvc-ltc-time-price").text()*1)));
                            $("#time-ltc-okcoin-bitvc").text(Math.abs(new Date($("#okcoin-ltc-time-price").text()*1)-new Date($("#bitvc-ltc-time-price").text()*1)));
                        }
                    }
                }
            }
        }
    }
}


function bitvc_btc() {
    $.ajax({
        url: 'http://api.huobi.com/staticmarket/ticker_btc_json.js',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('http://api.huobi.com/staticmarket/ticker_btc_json.js请求失败');
        },
        success: function (result) {
            var ticker = result.ticker;
            $("#bitvc-btc-buy-price").text(ticker.buy);
            $("#bitvc-btc-sell-price").text(ticker.sell);
            $("#bitvc-btc-last-price").text(ticker.last);
            $("#bitvc-btc-time-price").text(result.time*1000);
            $("#margin-btc-okcoin-bitvc").text(($("#okcoin-btc-buy-price").text()*1.0 - $("#bitvc-btc-sell-price").text()*1.0).toFixed(2));
            $("#margin-btc-bitvc-okcoin").text(($("#bitvc-btc-buy-price").text()*1.0 - $("#okcoin-btc-sell-price").text()*1.0).toFixed(2));
            $("#time-btc-bitvc-okcoin").text(Math.abs(new Date($("#okcoin-btc-time-price").text()*1)-new Date($("#bitvc-btc-time-price").text()*1)));
            $("#time-btc-okcoin-bitvc").text(Math.abs(new Date($("#okcoin-btc-time-price").text()*1)-new Date($("#bitvc-btc-time-price").text()*1)));
        }
    });
}
function bitvc_ltc() {
    $.ajax({
        url: 'http://api.huobi.com/staticmarket/ticker_ltc_json.js',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('http://api.huobi.com/staticmarket/ticker_ltc_json.js请求失败');
        },
        success: function (result) {
            var ticker = result.ticker;
            $("#bitvc-ltc-buy-price").text(ticker.buy);
            $("#bitvc-ltc-sell-price").text(ticker.sell);
            $("#bitvc-ltc-last-price").text(ticker.last);
            $("#bitvc-ltc-time-price").text(result.time*1000);
            $("#margin-ltc-okcoin-bitvc").text(($("#okcoin-ltc-buy-price").text()*1.0 - $("#bitvc-ltc-sell-price").text()*1.0).toFixed(2));
            $("#margin-ltc-bitvc-okcoin").text(($("#bitvc-ltc-buy-price").text()*1.0 - $("#okcoin-ltc-sell-price").text()*1.0).toFixed(2));
            $("#time-ltc-bitvc-okcoin").text(Math.abs(new Date($("#okcoin-ltc-time-price").text()*1)-new Date($("#bitvc-ltc-time-price").text()*1)));
            $("#time-ltc-okcoin-bitvc").text(Math.abs(new Date($("#okcoin-ltc-time-price").text()*1)-new Date($("#bitvc-ltc-time-price").text()*1)));
        }
    });
}
window.onload = okCoinWebSocket.init("wss://real.okcoin.cn:10440/websocket/okcoinapi");
window.onload = bitvcWebSocket.init();

