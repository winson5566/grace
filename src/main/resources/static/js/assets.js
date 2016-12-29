var assets = {};
var setting = {};
assets.init = function () {
    okcoinAssets();
    bitvcAssets();
    getUserTradeSettingInit();
    getThresholdLog();
    getTradeLog();
    getTradeSuccessLog();
}
assets.timer = function () {
    setInterval(okcoinAssets, 1000);
    setInterval(bitvcAssets, 1000);
    setInterval(getUserTradeSetting,1000);
    setInterval(getThresholdLog, 2000);
    setInterval(getTradeLog, 2000);
    setInterval(getTradeSuccessLog, 2000);
}

function okcoinAssets() {
    $.ajax({
        url: '/u/getOkcoinAssets',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/getOkcoinAssets请求异常');
        },
        success: function (result) {
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
    });
}

function bitvcAssets() {
    $.ajax({
        url: '/u/getBitvcAssets',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/getBitvcAssets请求异常');
        },
        success: function (result) {
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
    });
}


function getUserTradeSetting() {
    $.ajax({
        url: '/u/getUserTradeSetting',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/getUserTradeSetting');
        },
        success: function (result) {
            if (result != null && result.marginJson != null) {
                var marginJson = JSON.parse(result.marginJson);
                if (marginJson.s00100 != null)
                    $("#threshold-btc-okcoin-bitvc").text(marginJson.s00100);
                if (marginJson.s10000 != null)
                    $("#threshold-btc-bitvc-okcoin").text(marginJson.s10000);
                if (marginJson.s00101 != null)
                    $("#threshold-ltc-okcoin-bitvc").text(marginJson.s00101);
                if (marginJson.s10001 != null)
                    $("#threshold-ltc-bitvc-okcoin").text(marginJson.s10001);
                if(result.eachAmountBtc!=null)
                    $("#each-amount-btc").text(result.eachAmountBtc);
                if(result.eachAmountLtc!=null)
                    $("#each-amount-ltc").text(result.eachAmountLtc);
            }
            if(result.autoTradeBtc=="1"){
                $("#btc-trade").removeClass('btn-danger').addClass('btn-success').val("1").text("自动交易");
            }else if(result.autoTradeBtc=="0"){
                $("#btc-trade").removeClass('btn-success').addClass('btn-danger').val("0").text("手动交易");
            }
            if(result.autoTradeLtc=="1"){
                $("#ltc-trade").removeClass('btn-danger').addClass('btn-success').val("1").text("自动交易");
            }else if(result.autoTradeLtc=="0"){
                $("#ltc-trade").removeClass('btn-success').addClass('btn-danger').val("0").text("手动交易");
            }

            if(result.autoThresholdBtc=="1"){
                $("#btc-threshold").removeClass('btn-danger').addClass('btn-success').val("1").text("自动阀值");
            }else if(result.autoThresholdBtc=="0"){
                $("#btc-threshold").removeClass('btn-success').addClass('btn-danger').val("0").text("手动阀值");
            }

            if(result.autoThresholdLtc=="1"){
                $("#ltc-threshold").removeClass('btn-danger').addClass('btn-success').val("1").text("自动阀值");
            }else if(result.autoThresholdLtc=="0"){
                $("#ltc-threshold").removeClass('btn-success').addClass('btn-danger').val("0").text("手动阀值");
            }
        }
    });
}

function getUserTradeSettingInit() {
    $.ajax({
        url: '/u/getUserTradeSetting',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/getUserTradeSetting');
        },
        success: function (result) {
            if (result != null && result.marginJson != null) {
                var marginJson = JSON.parse(result.marginJson);
                if (marginJson.s00100 != null)
                    $("#threshold-btc-okcoin-bitvc").text(marginJson.s00100);
                    $("#input-threshold-btc-okcoin-bitvc").val(marginJson.s00100);
                if (marginJson.s10000 != null)
                    $("#threshold-btc-bitvc-okcoin").text(marginJson.s10000);
                    $("#input-threshold-btc-bitvc-okcoin").val(marginJson.s10000);
                if (marginJson.s00101 != null)
                    $("#threshold-ltc-okcoin-bitvc").text(marginJson.s00101);
                    $("#input-threshold-ltc-okcoin-bitvc").val(marginJson.s00101);
                if (marginJson.s10001 != null)
                    $("#threshold-ltc-bitvc-okcoin").text(marginJson.s10001);
                    $("#input-threshold-ltc-bitvc-okcoin").val(marginJson.s10001);
                if(result.eachAmountBtc!=null)
                    $("#each-amount-btc").text(result.eachAmountBtc);
                     $("#input-each-amount-btc").val(result.eachAmountBtc)
                if(result.eachAmountLtc!=null)
                    $("#each-amount-ltc").text(result.eachAmountLtc);
                     $("#input-each-amount-ltc").val(result.eachAmountLtc)
            }
            if(result.autoTradeBtc=="1"){
                $("#btc-trade").removeClass('btn-danger').addClass('btn-success').val("1").text("自动交易");
            }else if(result.autoTradeBtc=="0"){
                $("#btc-trade").removeClass('btn-success').addClass('btn-danger').val("0").text("手动交易");
            }
            if(result.autoTradeLtc=="1"){
                $("#ltc-trade").removeClass('btn-danger').addClass('btn-success').val("1").text("自动交易");
            }else if(result.autoTradeLtc=="0"){
                $("#ltc-trade").removeClass('btn-success').addClass('btn-danger').val("0").text("手动交易");
            }

            if(result.autoThresholdBtc=="1"){
                $("#btc-threshold").removeClass('btn-danger').addClass('btn-success').val("1").text("自动阀值");
            }else if(result.autoThresholdBtc=="0"){
                $("#btc-threshold").removeClass('btn-success').addClass('btn-danger').val("0").text("手动阀值");
            }

            if(result.autoThresholdLtc=="1"){
                $("#ltc-threshold").removeClass('btn-danger').addClass('btn-success').val("1").text("自动阀值");
            }else if(result.autoThresholdLtc=="0"){
                $("#ltc-threshold").removeClass('btn-success').addClass('btn-danger').val("0").text("手动阀值");
            }
        }
    });
}

function getThresholdLog() {
    $.ajax({
        url: '/u/getThresholdLog',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/getThresholdLog');
        },
        success: function (result) {
            var str='<tr><th>时间</th><th>币种</th><th>做空平台</th><th>做多平台</th><th>价差</th></tr>';
            for(var i=0; i<result.length; i++){
                str=str+'<tr>';
                var one=result[i];
                var createTimestamp = one.createTimestamp;
                var context = one.context;
                var contextJson = JSON.parse(context)
                var margin = contextJson.margin;
                var buyPlatform = contextJson.buyPlatform;
                var sellPlatform = contextJson.sellPlatform;
                var coin = contextJson.coin;
                str=str+'<td>';
                str=str+createTimestamp+'</td><td>';
                str=str+coin+'</td><td>';
                str=str+buyPlatform+'</td><td>';
                str=str+sellPlatform+'</td><td>';
                str=str+margin+'</td>';
                $("#threshold-log").html(str);
            }
        }
    });
}

function getTradeLog() {
    $.ajax({
        url: '/u/getTradeLog',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/getTradeLog');
        },
        success: function (result) {
            var str='<tr><th>时间</th><th>日志</th></tr>';
            for(var i=0; i<result.length; i++) {
                str = str + '<tr>';
                var one = result[i];
                var createTimestamp = one.createTimestamp;
                var context = one.context;
                str = str + '<td>';
                str = str + createTimestamp + '</td><td>';
                str = str + context + '</td>';
                $("#trade-log").html(str);
            }
        }
    });
}

function getTradeSuccessLog() {
    $.ajax({
        url: '/u/getTradeSuccessLog',
        data: {},
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/getTradeSuccessLog');
        },
        success: function (result) {
            var str='<tr><th>时间</th><th>日志</th></tr>';
            for(var i=0; i<result.length; i++) {
                str = str + '<tr>';
                var one = result[i];
                var createTimestamp = one.createTimestamp;
                var context = one.context;
                str = str + '<td>';
                str = str + createTimestamp + '</td><td>';
                str = str + context + '</td>';
                $("#trade-success-log").html(str);
            }
        }
    });
}


window.onload = assets.init();
window.onload = assets.timer();