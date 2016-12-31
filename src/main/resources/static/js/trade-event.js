/**
 * Created by winson on 2016/12/31.
 */
$("#bn-threshold-btc-okcoin-bitvc").click(function () {
    var margin = $("#input-threshold-btc-okcoin-bitvc").val();
    if (margin != null&&margin != "") {
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data: {
                buyPlatform: '00', sellPlatform: '10', coin: '0', margin: margin
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

$("#bn-threshold-btc-bitvc-okcoin").click(function () {
    var margin = $("#input-threshold-btc-bitvc-okcoin").val();
    if (margin != null&&margin != "") {
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data: {
                buyPlatform: '10', sellPlatform: '00', coin: '0', margin: margin
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

$("#bn-threshold-ltc-okcoin-bitvc").click(function () {
    var margin = $("#input-threshold-ltc-okcoin-bitvc").val();
    if (margin != null&&margin != "") {
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data: {
                buyPlatform: '00', sellPlatform: '10', coin: '1', margin: margin
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

$("#bn-threshold-ltc-bitvc-okcoin").click(function () {
    var margin = $("#input-threshold-ltc-bitvc-okcoin").val();
    if (margin != null&&margin != "") {
        $.ajax({
            url: '/u/updateUserTradeSetting',
            data: {
                buyPlatform: '10', sellPlatform: '00', coin: '1', margin: margin
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


function updateThreshold(result) {
    if (result.s00100 != null)
        $("#threshold-btc-okcoin-bitvc").text(result.s00100);
    if (result.s10000 != null)
        $("#threshold-btc-bitvc-okcoin").text(result.s10000);
    if (result.s00101 != null)
        $("#threshold-ltc-okcoin-bitvc").text(result.s00101);
    if (result.s10001 != null)
        $("#threshold-ltc-bitvc-okcoin").text(result.s10001);
}


$("#btc-trade").click(function () {
    var btcTrade = $("#btc-trade").val();
    var ltcTrade = $("#ltc-trade").val();
    var btcThreshold = $("#btc-threshold").val();
    var ltcThreshold = $("#ltc-threshold").val();
    if (btcTrade == "0") {
        btcTrade = "1";
    } else if (btcTrade == "1") {
        btcTrade = "0";
    }
    $.ajax({
        url: '/u/updateUserTradeSettingAuto',
        data: {
            autoTradeBtc: btcTrade,
            autoTradeLtc: ltcTrade,
            autoThresholdBtc: btcThreshold,
            autoThresholdLtc: ltcThreshold
        },
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/updateUserTradeSettingAuto');
        },
        success: function (result) {
            updateAutoTrade(result);
        }
    });
});

$("#ltc-trade").click(function () {
    var btcTrade = $("#btc-trade").val();
    var ltcTrade = $("#ltc-trade").val();
    var btcThreshold = $("#btc-threshold").val();
    var ltcThreshold = $("#ltc-threshold").val();
    if (ltcTrade == "0") {
        ltcTrade = "1";
    } else if (ltcTrade == "1") {
        ltcTrade = "0";
    }
    $.ajax({
        url: '/u/updateUserTradeSettingAuto',
        data: {
            autoTradeBtc: btcTrade,
            autoTradeLtc: ltcTrade,
            autoThresholdBtc: btcThreshold,
            autoThresholdLtc: ltcThreshold
        },
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/updateUserTradeSettingAuto');
        },
        success: function (result) {
            updateAutoTrade(result);
        }
    });
});


$("#btc-threshold").click(function () {
    var btcTrade = $("#btc-trade").val();
    var ltcTrade = $("#ltc-trade").val();
    var btcThreshold = $("#btc-threshold").val();
    var ltcThreshold = $("#ltc-threshold").val();
    if (btcThreshold == "0") {
        btcThreshold = "1";
    } else if (btcThreshold == "1") {
        btcThreshold = "0";
    }
    $.ajax({
        url: '/u/updateUserTradeSettingAuto',
        data: {
            autoTradeBtc: btcTrade,
            autoTradeLtc: ltcTrade,
            autoThresholdBtc: btcThreshold,
            autoThresholdLtc: ltcThreshold
        },
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/updateUserTradeSettingAuto');
        },
        success: function (result) {
            updateAutoTrade(result);
        }
    });
});

$("#ltc-threshold").click(function () {
    var btcTrade = $("#btc-trade").val();
    var ltcTrade = $("#ltc-trade").val();
    var btcThreshold = $("#btc-threshold").val();
    var ltcThreshold = $("#ltc-threshold").val();
    if (ltcThreshold == "0") {
        ltcThreshold = "1";
    } else if (ltcThreshold == "1") {
        ltcThreshold = "0";
    }
    $.ajax({
        url: '/u/updateUserTradeSettingAuto',
        data: {
            autoTradeBtc: btcTrade,
            autoTradeLtc: ltcTrade,
            autoThresholdBtc: btcThreshold,
            autoThresholdLtc: ltcThreshold
        },
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        error: function () {
            console.log('/u/updateUserTradeSettingAuto');
        },
        success: function (result) {
            updateAutoTrade(result);
        }
    });
});


function updateAutoTrade(result) {
    if (result.autoTradeBtc == '0') {
        $("#btc-trade").removeClass('btn-success').addClass('btn-danger').val("0");
        $("#btc-trade").text("手动交易");
    } else if (result.autoTradeBtc == '1') {
        $("#btc-trade").removeClass('btn-danger').addClass('btn-success').val("1");
        $("#btc-trade").text("自动交易");
    }

    if (result.autoTradeLtc == '0') {
        $("#ltc-trade").removeClass('btn-success').addClass('btn-danger').val("0");
        $("#ltc-trade").text("手动交易");
    } else if (result.autoTradeLtc == '1') {
        $("#ltc-trade").removeClass('btn-danger').addClass('btn-success').val("1");
        $("#ltc-trade").text("自动交易");
    }

    if (result.autoThresholdBtc == '0') {
        $("#btc-threshold").removeClass('btn-success').addClass('btn-danger').val("0");
        $("#btc-threshold").text("手动阀值");
    } else if (result.autoThresholdBtc == '1') {
        $("#btc-threshold").removeClass('btn-danger').addClass('btn-success').val("1");
        $("#btc-threshold").text("自动阀值");
    }

    if (result.autoThresholdLtc == '0') {
        $("#ltc-threshold").removeClass('btn-success').addClass('btn-danger').val("0");
        $("#ltc-threshold").text("手动阀值");
    } else if (result.autoThresholdLtc == '1') {
        $("#ltc-threshold").removeClass('btn-danger').addClass('btn-success').val("1");
        $("#ltc-threshold").text("自动阀值");
    }
}


$("#bn-each-amount-btc").click(function () {
    var eachAmountBtc = $("#input-each-amount-btc").val();
    if (eachAmountBtc != null && eachAmountBtc != "") {
        $.ajax({
            url: '/u/updateUserTradeSettingEachAmount',
            data: {
                eachAmountBtc: eachAmountBtc, eachAmountLtc: null
            },
            type: 'GET',
            dataType: 'json',
            timeout: 1000,
            error: function () {
                console.log('/u/updateUserTradeSettingEachAmount');
            },
            success: function (result) {
                updateEachAmount(result);
            }
        });
    }
});

$("#bn-each-amount-ltc").click(function () {
    var eachAmountLtc = $("#input-each-amount-ltc").val();
    if (eachAmountLtc != null && eachAmountLtc != "") {
        $.ajax({
            url: '/u/updateUserTradeSettingEachAmount',
            data: {
                eachAmountBtc: null, eachAmountLtc: eachAmountLtc
            },
            type: 'GET',
            dataType: 'json',
            timeout: 1000,
            error: function () {
                console.log('/u/updateUserTradeSettingEachAmount');
            },
            success: function (result) {
                updateEachAmount(result);
            }
        });
    }
});

function updateEachAmount(result) {
    if (result.eachAmountBtc != null && result.eachAmountBtc != "")
        $("#each-amount-btc").text(result.eachAmountBtc);
    if (result.eachAmountLtc != null && result.eachAmountLtc != "")
        $("#each-amount-ltc").text(result.eachAmountLtc);
}

$("#bn-threshold-log").click(function () {
    $("#threshold-log").removeClass("not-display").addClass("is-display");
    $("#trade-log").removeClass("is-display").addClass("not-display");
    $("#trade-success-log").removeClass("is-display").addClass("not-display");
    $("#bn-threshold-log").removeClass("btn-default").addClass("btn-primary");
    $("#bn-trade-log").removeClass("btn-primary").addClass("btn-default");
    $("#bn-trade-success-log").removeClass("btn-primary").addClass("btn-default");
});

$("#bn-trade-log").click(function () {
    $("#trade-log").removeClass("not-display").addClass("is-display");
    $("#threshold-log").removeClass("is-display").addClass("not-display");
    $("#trade-success-log").removeClass("is-display").addClass("not-display");
    $("#bn-trade-log").removeClass("btn-default").addClass("btn-primary");
    $("#bn-threshold-log").removeClass("btn-primary").addClass("btn-default");
    $("#bn-trade-success-log").removeClass("btn-primary").addClass("btn-default");
});

$("#bn-trade-success-log").click(function () {
    $("#trade-success-log").removeClass("not-display").addClass("is-display");
    $("#threshold-log").removeClass("is-display").addClass("not-display");
    $("#trade-log").removeClass("is-display").addClass("not-display");
    $("#bn-trade-success-log").removeClass("btn-default").addClass("btn-primary");
    $("#bn-threshold-log").removeClass("btn-primary").addClass("btn-default");
    $("#bn-trade-log").removeClass("btn-primary").addClass("btn-default");
});



