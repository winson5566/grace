var assets = {};
assets.init = function () {
    setInterval(okcoinAssets,1000);
    setInterval(bitvcAssets, 1000);
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

window.onload = assets.init();