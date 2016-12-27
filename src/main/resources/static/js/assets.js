var assets = {};
var setting = {};
assets.init = function () {
    setInterval(okcoinAssets, 1000);
    setInterval(bitvcAssets, 1000);
    setInterval(getUserTradeSetting,1000);
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
            }
            if(result.autoTradeBtc=="1"){
                $("#btc-trade").removeClass('btn-danger').addClass('btn-success').val("1").text("BTC自动交易");
            }else if(result.autoTradeBtc=="0"){
                $("#btc-trade").removeClass('btn-success').addClass('btn-danger').val("0").text("BTC手动交易");
            }
            if(result.autoTradeLtc=="1"){
                $("#ltc-trade").removeClass('btn-danger').addClass('btn-success').val("1").text("LTC自动交易");
            }else if(result.autoTradeLtc=="0"){
                $("#ltc-trade").removeClass('btn-success').addClass('btn-danger').val("0").text("LTC手动交易");
            }

            if(result.autoThresholdBtc=="1"){
                $("#btc-threshold").removeClass('btn-danger').addClass('btn-success').val("1").text("BTC自动阀值");
            }else if(result.autoThresholdBtc=="0"){
                $("#btc-threshold").removeClass('btn-success').addClass('btn-danger').val("0").text("BTC手动阀值");
            }

            if(result.autoThresholdLtc=="1"){
                $("#ltc-threshold").removeClass('btn-danger').addClass('btn-success').val("1").text("LTC自动阀值");
            }else if(result.autoThresholdLtc=="0"){
                $("#ltc-threshold").removeClass('btn-success').addClass('btn-danger').val("0").text("LTC手动阀值");
            }
        }
    });
}

window.onload = assets.init();