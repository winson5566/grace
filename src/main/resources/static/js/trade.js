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
var huobiWebSocket = {};
huobiWebSocket.init=function () {
    var sh=setInterval(huobi_btc,1000);
    var sh=setInterval(huobi_ltc,1000);
}

function onOpen(evt) {
    print("CONNECTED");
    doSend("[{'event':'addChannel','channel':'ok_sub_spotcny_btc_depth_60'},{'event':'addChannel','channel':'ok_sub_spotcny_ltc_depth_60'}]");
}
function onClose(evt) {
    print("DISCONNECTED");
}

function onMessage(e) {
    //console.log(new Date().getTime() + ": " + e.data)
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
        createTable(array);
    }
}

function onError(evt) {
    print('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend(message) {
    print("SENT: " + message);
    okCoinWebSocket.websocket.send(message);
}

function print(message) {
    console.log(new Date().getTime() + ": " + message)
}

function createTable(array) {
    for (var i = 0; i < array.length; i++) {
        var channel = array[i].channel;
        //价格
        if (channel == 'ok_sub_spotcny_btc_depth_60' || channel == 'ok_sub_spotcny_ltc_depth_60') {
            processingPriceData(array);
        }
    }
}

function processingPriceData(array, accuracy) {
    for (var i = 0; i < array.length; i++) {
        var channel = array[i].channel;
        for (var j in array[0]) {
            if (j == 'data') {
                if (channel == 'ok_sub_spotcny_btc_depth_60') {
                    var str = '<table id="price_okcoin_btc"  class="table-inline table table-condensed table-hover">\r\n<tr>\r\n';
                } else if (channel == 'ok_sub_spotcny_ltc_depth_60') {
                    var str = '<table id="price_okcoin_ltc" class="table-inline table table-condensed table-hover">\r\n<tr>\r\n';
                }
                str += '<th class="width150 font_white">' + '价格' + '</th>\r\n';
                str += '<th class="width100 font_white">' + '数量' + '</th>\r\n';
                str += '</tr>\r\n';
                var arr = array[i];
                var asks = arr.data.asks;
                var bids = arr.data.bids;
                // 卖
                for (var k = asks.length - 5; k < asks.length; k++) {
                    str += '<tr id="okcoin-btc-sell" class="font_red">\r\n';
                    var ask = asks[k];
                    str += '<td id="okcoin-btc-sell-price-' + (asks.length-k) + '"  class="width150 ">' + ask[0] + '</td>\r\n';
                    if((asks.length-k)==1&&channel == 'ok_sub_spotcny_btc_depth_60'){
                        $("#okcoin-btc-buy-price").text(ask[0]);
                    }
                    str += '<td id="okcoin-btc-sell-amount-' + (asks.length-k) + '"  class="width100 ">' + ask[1] + '</td>\r\n';
                    str += '</tr>\r\n';
                }
                //买
                for (var k = 0; k < 5; k++) {
                    str += '<tr id="okcoin-btc-buy"  class="font_green">\r\n';
                    var bid = bids[k];
                    str += '<td id="okcoin-btc-buy-price-' + (k+1) + '"  class="width150 ">' + bid[0] + '</td>\r\n';
                    if(k==0&&channel == 'ok_sub_spotcny_btc_depth_60'){
                        $("#okcoin-btc-sell-price").text(bid[0]);
                    }
                    str += '<td id="okcoin-btc-buy-amount-' + (k+1) + '"  class="width100 ">' + bid[1] + '</td>\r\n';
                    str += '</tr>\r\n';
                }
                str += '</table>\r\n';
                if (channel == 'ok_sub_spotcny_btc_depth_60') {
                    $("#Okcoin_btc").html(str);
                } else if (channel == 'ok_sub_spotcny_ltc_depth_60') {
                    $("#Okcoin_ltc").html(str);
                }
            }
        }
    }
}


function huobi_btc() {
    $.ajax({
        url: 'http://api.huobi.com/staticmarket/depth_btc_json.js',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('http://api.huobi.com/staticmarket/depth_btc_json.js请求异常');
        },
        success: function (result) {
            var asks = result.asks;
            var bids = result.bids;
            var str = '<table id="price_huobi_btc"  class="table-inline table table-condensed table-hover">\r\n<tr>\r\n';
            str += '<th class="width150 font_white">' + '价格' + '</th>\r\n';
            str += '<th class="width100 font_white">' + '数量' + '</th>\r\n';
            str += '</tr>\r\n';
            // 卖
            asks.reverse();
            for (var k = asks.length - 5; k < asks.length; k++) {
                str += '<tr id="huobi-btc-sell" class="font_red">\r\n';
                var ask = asks[k];
                str += '<td id="huobi-btc-sell-price-' + (asks.length-k) + '"  class="width150 ">' + ask[0] + '</td>\r\n';
                str += '<td id="huobi-btc-sell-amount-' + (asks.length-k) + '"  class="width100 ">' + ask[1] + '</td>\r\n';
                str += '</tr>\r\n';
            }
            //买
            for (var k = 0; k < 5; k++) {
                str += '<tr id="huobi-btc-buy"  class="font_green">\r\n';
                var bid = bids[k];
                str += '<td id="huobi-btc-buy-price-' + (k+1) + '"  class="width150 ">' + bid[0] + '</td>\r\n';
                str += '<td id="huobi-btc-buy-amount-' + (k+1) + '"  class="width100 ">' + bid[1] + '</td>\r\n';
                str += '</tr>\r\n';
            }
            str += '</table>\r\n';
            $("#Huobi_btc").html(str);
        }
    });
}
function huobi_ltc() {
    $.ajax({
        url: 'http://api.huobi.com/staticmarket/depth_ltc_json.js',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('http://api.huobi.com/staticmarket/depth_ltc_json.js请求异常');
        },
        success: function (result) {
            var asks = result.asks;
            var bids = result.bids;
            var str = '<table id="price_huobi_btc"  class="table-inline table table-condensed table-hover">\r\n<tr id="tr0">\r\n';
            str += '<th>' + '价格' + '</th>\r\n';
            str += '<th>' + '数量' + '</th>\r\n';
            str += '</tr>\r\n';
            // 卖
            asks.reverse();
            for (var k = asks.length - 5; k < asks.length; k++) {
                str += '<tr id="ask' + (k + 1) + '" class="font_red">\r\n';
                var ask = asks[k];
                str += '<td>' + ask[0] + '</td>\r\n';
                str += '<td>' + ask[1] + '</td>\r\n';
                str += '</tr>\r\n';
            }
            //买
            for (var k = 0; k < 5; k++) {
                str += '<tr id="bid' + (k + 1) + '"  class="font_green">\r\n';
                var bid = bids[k];
                str += '<td>' + bid[0] + '</td>\r\n';
                str += '<td>' + bid[1] + '</td>\r\n';
                str += '</tr>\r\n';
            }
            str += '</table>\r\n';
            $("#Huobi_ltc").html(str);
        }
    });
}
window.onload = okCoinWebSocket.init("wss://real.okcoin.cn:10440/websocket/okcoinapi");
window.onload = huobiWebSocket.init();