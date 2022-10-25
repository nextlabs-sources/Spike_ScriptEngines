var http = require("http");
var request = require('request')
var console = require('console')
var timer = require('timers')
var httpclient = require('httpclient')
// var sync_request = require('sync-request');

// exports.getMetadata = function (resourceType, callback) {
//     request("http://localhost:8080/metadata?resourceType=" + resourceType + "&scriptName=trireme_0", function (error, response, body) {
//         if (!error && response.statusCode == 200) {
//             callback && callback(true, body);
//         } else {
//             console.log("error", error)
//             console.log(response)
//             callback && callback(false, "{}");
//         }
//     })
//     setTimeout(function () {
//         callback(true, "Time out happened")
//     }, 500);
// }






exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    setTimeout(function() {
        console.log("calling JavaScript:::::::::::::::::::::::")
    }, 0)
    // callback && callback(true, "{}", 0,"normal "+entityType);
    // return
    var startTime = new Date().getTime();
    // var timerInstance = timer.setTimeout(function () {
    //     // callback(true, "Time out happened")
    //     var endTime = new Date().getTime();
    //         //console.log("Call back time in JavaScript: "+(endTime-startTime))
    //     callback && callback(true, "Time out happened", (endTime-startTime),"timeout "+entityType);
    // }, 0);
    // timerInstance.unref()

    request("http://localhost:8080/attributeValue?resourceType=" + resourceType + "&entityType=" + entityType + "&attributeId=" + attributeId, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            // // timer.unref()
            // timer.clearTimeout(timerInstance);
            // // timer.close();
            var endTime = new Date().getTime();
            //console.log("Got response "+body)
            //console.log("Call back time in JavaScript: "+(endTime-startTime))
            callback && callback(true, body, (endTime-startTime),"normal "+entityType);
            //console.log("After callback::::::::::::::::::")
        } else {
            // timer.unref()
            // timer.clearTimeout(timerInstance);
            // console.log("error", error)
            console.log(response)
            callback && callback(false, "{}", 0, "error");
        }
    })

    // setTimeout(function () {
    //     // var endTime = new Date().getTime();
    //     // callback(true, "Time out happened", (endTime - startTime),"timeout "+entityType)
    // }, 500);

}