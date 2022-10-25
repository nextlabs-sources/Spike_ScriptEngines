var fs = require('fs')
var request = require("request")

var testFunc = function testFunc(sequence, callback) {
    var req;
    // var timeout = false

    // req.timeout = 1000

    // fs.stat("C:", function () {
    //     setTimeout(function () {
    //         clearTimeout(timer)
    //         callback(1, sequence)
    //     }, 50)
    // })

    req = request("http://localhost:8080/metadata?resourceType=sdfdsfasdf&scriptName=trireme_0", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            // clearTimeout(timer);
            callback && callback(1, sequence);

        } else {
            console.log("error", error)
            console.log(response)
            callback && callback(false, "{}");
        }
    })

    var timer;
    timer = setTimeout(function () {
        // req && req.abort();
        // req.clearTimeout()
        // timeout = true
        callback(2 +" timeout happened", timer['id'])
    }, 500)
    timer['id'] = new Date().getTime()

}

exports.testFunc = testFunc