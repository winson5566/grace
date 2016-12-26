$("#bn-threshold-btc-okcoin-bitvc").click(function(){
    var margin = $("#input-threshold-btc-okcoin-bitvc").val();
    if(margin!=null){
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data:{
                buyPlatform:'00',sellPlatform:'10',coin:'0',margin:margin
            },
            type: 'GET',
            dataType: 'json',
            timeout: 1000,
            error: function () {
                console.log('/u/updateUserTradeSetting请求失败');
            },
            success: function (result) {
                updateThreshold(result);
            }
        });
    }
});

$("#bn-threshold-btc-bitvc-okcoin").click(function(){
    var margin = $("#input-threshold-btc-bitvc-okcoin").val();
    if(margin!=null){
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data:{
                buyPlatform:'10',sellPlatform:'00',coin:'0',margin:margin
            },
            type: 'GET',
            dataType: 'json',
            timeout: 1000,
            error: function () {
                console.log('/u/updateUserTradeSetting请求失败');
            },
            success: function (result) {
                updateThreshold(result);
            }
        });
    }
});

$("#bn-threshold-ltc-okcoin-bitvc").click(function(){
    var margin = $("#input-threshold-ltc-okcoin-bitvc").val();
    if(margin!=null){
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data:{
                buyPlatform:'00',sellPlatform:'10',coin:'1',margin:margin
            },
            type: 'GET',
            dataType: 'json',
            timeout: 1000,
            error: function () {
                console.log('/u/updateUserTradeSetting请求失败');
            },
            success: function (result) {
                updateThreshold(result);
            }
        });
    }
});

$("#bn-threshold-ltc-bitvc-okcoin").click(function(){
    var margin = $("#input-threshold-ltc-bitvc-okcoin").val();
    if(margin!=null){
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data:{
                buyPlatform:'10',sellPlatform:'00',coin:'1',margin:margin
            },
            type: 'GET',
            dataType: 'json',
            timeout: 1000,
            error: function () {
                console.log('/u/updateUserTradeSetting请求失败');
            },
            success: function (result) {
                updateThreshold(result);
            }
        });
    }
});

function updateThreshold(result){
    if (result.s00100!=null)
        $("#threshold-btc-okcoin-bitvc").text(result.s00100);
    if (result.s10000!=null)
        $("#threshold-btc-bitvc-okcoin").text(result.s10000);
    if (result.s00101!=null)
        $("#threshold-ltc-okcoin-bitvc").text(result.s00101);
    if (result.s10001!=null)
        $("#threshold-ltc-bitvc-okcoin").text(result.s10001);
}

$("#all-trade").click(function(){
    var allTrade = $("#all-trade").val();
    var btcTrade = $("#btc-trade").val();
    var ltcTrade = $("#ltc-trade").val();
        $.ajax({
            url: '/u/updateUserTradeSettingAutoTrade',
            data:{
                autoTrade:allRun,autoTradeBtc:btcRun,autoTradeLtc:ltcRun
            },
            type: 'GET',
            dataType: 'json',
            timeout: 1000,
            error: function () {
                console.log('/u/updateUserTradeSettingAutoTrade');
            },
            success: function (result) {
                console.log(result);
            }
        });
});