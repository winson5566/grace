var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {    //客户端连接
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/price', function (message) {  //订阅价格
            showPrice(JSON.parse(message.body).content);
        });
        stompClient.subscribe('/topic/margin', function (message) {  //订阅价差
            showMargin(JSON.parse(message.body).content);
        });
        stompClient.subscribe('/user/queue/assets', function (message) {    //用户资产
            showAssets(JSON.parse(message.body).content);
        });
    });
}

//显示价格
function showPrice(message) {
    var obj = JSON.parse(message);
    $("#okcoin-btc-buy-price").text(obj.data['00000'].price);
    $("#okcoin-btc-sell-price").text(obj.data['00001'].price);
    $("#okcoin-btc-last-price").text(obj.data['00002'].price);
    $("#okcoin-btc-time-price").text(obj.data['00001'].timestamp);

    $("#okcoin-ltc-buy-price").text(obj.data['00010'].price);
    $("#okcoin-ltc-sell-price").text(obj.data['00011'].price);
    $("#okcoin-ltc-last-price").text(obj.data['00012'].price);
    $("#okcoin-ltc-time-price").text(obj.data['00010'].timestamp);

    $("#bitvc-btc-buy-price").text(obj.data['01000'].price);
    $("#bitvc-btc-sell-price").text(obj.data['01001'].price);
    $("#bitvc-btc-last-price").text(obj.data['01002'].price);
    $("#bitvc-btc-time-price").text(obj.data['01000'].timestamp);

    $("#bitvc-ltc-buy-price").text(obj.data['01010'].price);
    $("#bitvc-ltc-sell-price").text(obj.data['01011'].price);
    $("#bitvc-ltc-last-price").text(obj.data['01012'].price);
    $("#bitvc-ltc-time-price").text(obj.data['01010'].timestamp);

}

//显示价差
function showMargin(message) {
    var obj = JSON.parse(message);

    $("#margin-btc-okcoin-bitvc").text(obj.data['100100'].margin);
    $("#margin-ltc-okcoin-bitvc").text(obj.data['100101'].margin);
    $("#margin-btc-bitvc-okcoin").text(obj.data['110000'].margin);
    $("#margin-ltc-bitvc-okcoin").text(obj.data['110001'].margin);
    $("#time-btc-okcoin-bitvc").text(obj.data['100100'].deltaTime);
    $("#time-ltc-okcoin-bitvc").text(obj.data['100101'].deltaTime);
    $("#time-btc-bitvc-okcoin").text(obj.data['110000'].deltaTime);
    $("#time-ltc-bitvc-okcoin").text(obj.data['110001'].deltaTime);
    if (obj.data['100100'].margin > 0) {

    }
}

//显示资产
function showAssets(message) {
    var obj = JSON.parse(message);
    //OkcoinCN
    if (obj.p00 != null) {
        var result =obj.p00;
        if (result.result.info.funds != null) {
            $("#assets-okcoin-btc-free").text(result.result.info.funds.free.btc);
            $("#assets-okcoin-ltc-free").text(result.result.info.funds.free.ltc);
            $("#assets-okcoin-cny-free").text(result.result.info.funds.free.cny);
            if (result.result.info.funds.borrow != null) {
                $("#assets-okcoin-btc-borrow").text(result.result.info.funds.borrow.btc);
                $("#assets-okcoin-ltc-borrow").text(result.result.info.funds.borrow.ltc);
                $("#assets-okcoin-cny-borrow").text(result.result.info.funds.borrow.cny);
            }
            $("#assets-okcoin-btc-freezed").text(result.result.info.funds.freezed.btc);
            $("#assets-okcoin-ltc-freezed").text(result.result.info.funds.freezed.ltc);
            $("#assets-okcoin-cny-freezed").text(result.result.info.funds.freezed.cny);
            $("#assets-okcoin-net-asset").text(result.result.info.funds.asset.net);
            $("#assets-okcoin-total-asset").text(result.result.info.funds.asset.total);
        }
    }
    //BitvcCN
    if(obj.p10 != null){
        var result =obj.p10;
        $("#assets-bitvc-btc-free").text(result.result.available_btc);
        $("#assets-bitvc-ltc-free").text(result.result.available_ltc);
        $("#assets-bitvc-cny-free").text(result.result.available_cny);
        $("#assets-bitvc-cny-free").text(result.result.available_usd);
        $("#assets-bitvc-btc-borrow").text(result.result.borrow_btc);
        $("#assets-bitvc-ltc-borrow").text(result.result.borrow_ltc);
        $("#assets-bitvc-cny-borrow").text(result.result.borrow_cny);
        $("#assets-bitvc-usd-free").text(result.result.borrow_usd);
        $("#assets-bitvc-btc-freezed").text(result.result.frozen_btc);
        $("#assets-bitvc-ltc-freezed").text(result.result.frozen_ltc);
        $("#assets-bitvc-cny-freezed").text(result.result.frozen_cny);
        $("#assets-bitvc-usd-freezed").text(result.result.frozen_usd);
        $("#assets-bitvc-net-asset").text(result.result.net_asset);
        $("#assets-bitvc-total-asset").text(result.result.total);
    }
}

window.onload = connect;

