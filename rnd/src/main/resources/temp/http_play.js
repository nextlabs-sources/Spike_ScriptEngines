var request = require('request')

exports.test = function (url, callback) {
    request(url, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            // console.log(body) // Show the HTML for the Google homepage. 
            var x = JSON.stringify({ip:"M.QL","id":12345});
            callback && callback(x, "{}");
            //callback && callback({ip:"M.QL","id":12345});
        }
    })
}