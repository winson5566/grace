function processingPriceData(array, accuracy) {
    for (var i = 0; i < array.length; i++) {
        var channel = array[i].channel;
        for (var j in array[0]) {
            if (j == 'data') {
                if (channel == 'ok_sub_spotcny_btc_depth_20') {
                    var str = '<table id="price_okcoin_btc"  class="table-inline table table-condensed table-hover">\r\n<tr>\r\n';
                } else if (channel == 'ok_sub_spotcny_ltc_depth_20') {
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
                    if((asks.length-k)==1&&channel == 'ok_sub_spotcny_btc_depth_20'){
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
                    if(k==0&&channel == 'ok_sub_spotcny_btc_depth_20'){
                        $("#okcoin-btc-sell-price").text(bid[0]);
                    }
                    str += '<td id="okcoin-btc-buy-amount-' + (k+1) + '"  class="width100 ">' + bid[1] + '</td>\r\n';
                    str += '</tr>\r\n';
                }
                str += '</table>\r\n';
                if (channel == 'ok_sub_spotcny_btc_depth_20') {
                    $("#Okcoin_btc").html(str);
                } else if (channel == 'ok_sub_spotcny_ltc_depth_20') {
                    $("#Okcoin_ltc").html(str);
                }
            }
        }
    }
}