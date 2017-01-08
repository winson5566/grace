$("#bn-threshold-logs").click(function () {
    showThreshold();
    $("#bn-threshold-logs").removeClass("btn-default").addClass("btn-primary");
    $("#bn-trade-logs").removeClass("btn-primary").addClass("btn-default");
    $("#bn-trade-success-logs").removeClass("btn-primary").addClass("btn-default");
});

$("#bn-trade-logs").click(function () {
    showTrade();
    $("#bn-trade-logs").removeClass("btn-default").addClass("btn-primary");
    $("#bn-threshold-logs").removeClass("btn-primary").addClass("btn-default");
    $("#bn-trade-success-logs").removeClass("btn-primary").addClass("btn-default");
});

$("#bn-trade-success-logs").click(function () {
    showTradeSuccess();
    $("#bn-trade-success-logs").removeClass("btn-default").addClass("btn-primary");
    $("#bn-threshold-logs").removeClass("btn-primary").addClass("btn-default");
    $("#bn-trade-logs").removeClass("btn-primary").addClass("btn-default");
});

$("#bn-back-trade").click(function () {
    location.href ="/u/trade";
});

function showThreshold() {
    $.ajax({
        url: '/u/getLogByTypeAndAmount',
        data: {
            type:1,amount:200
        },
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
        },
        success: function (result) {
            showThresholdList(result);
        }
    });
}
function showTrade() {
    $.ajax({
        url: '/u/getLogByTypeAndAmount',
        data: {
            type: 2,amount:200
        },
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
        },
        success: function (result) {
            showTradeList(result);
        }
    });
}
function showTradeSuccess() {
    $.ajax({
        url: '/u/getLogByTypeAndAmount',
        data: {
            type: 3,amount:200
        },
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
        },
        success: function (result) {
            showTradeList(result);
        }
    });
}

//显示价差触发日志
function showThresholdList(result) {
    var str = '<tr><th>时间</th><th>币种</th><th>做空平台</th><th>做多平台</th><th>价差</th></tr>';
    for (var i = 0; i < result.length; i++) {
        str = str + '<tr>';
        var one = result[i];
        var createTimestamp = new Date(one.createTimestamp);
        var   month=createTimestamp.getMonth()+1;
        var   date=createTimestamp.getDate();
        var   hour=createTimestamp.getHours();
        var   minute=createTimestamp.getMinutes();
        var   second=createTimestamp.getSeconds();
        var showDatetime =month+"."+date+" "+hour+":"+minute+":"+second;

        var context = one.context;
        var contextJson = JSON.parse(context)
        var margin = contextJson.margin;
        var buyPlatform = contextJson.buyPlatform;
        if (buyPlatform=="00"){
            buyPlatform="Okcoin";
        }else if (buyPlatform=="10"){
            buyPlatform="Bitvc";
        }
        var sellPlatform = contextJson.sellPlatform;
        if (sellPlatform=="00"){
            sellPlatform="Okcoin";
        }else if (sellPlatform=="10"){
            sellPlatform="Bitvc";
        }
        var coin = contextJson.coin;
        if (coin=='0'){
            coin='BTC';
        }else {
            coin='LTC';
        }
        str = str + '<td>';
        str = str + showDatetime + '</td><td>';
        str = str + coin + '</td><td>';
        str = str + buyPlatform + '</td><td>';
        str = str + sellPlatform + '</td><td>';
        str = str + margin + '</td>';
    }
    str = str + '</tr>';
    $("#show-logs").html(str);
}

//显示交易日志
function showTradeList(result) {
    var str = '<tr><th>时间</th><th>日志</th></tr>';
    for (var i = 0; i < result.length; i++) {
        str = str + '<tr>';
        var one = result[i];
        var createTimestamp = one.createTimestamp;
        var context = one.context;
        str = str + '<td>';
        str = str + createTimestamp + '</td><td>';
        str = str + context + '</td>';
    }
    str = str + '<tr>';
    $("#show-logs").html(str);
}

window.onload = showThreshold();
