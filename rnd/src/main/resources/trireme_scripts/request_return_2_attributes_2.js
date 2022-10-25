var http = require("http");
var request = require('request')
var console = require('console')

exports.getMetadata = function (resourceType, callback) {

    request("http://localhost:8081/metadata?resourceType=" + resourceType + "&scriptName=trireme_2", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback && callback(true, body);
        } else {
            console.log("error", error)
            console.log(response)
            callback && callback(false, "{}");
        }
    })

    setTimeout(function () {
        callback(true, "Time out happened")
    }, 10000);
}

exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    request("http://localhost:8081/attributeValue?resourceType=" + resourceType + "&entityType=" + entityType + "&attributeId=" + attributeId, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback && callback(true, body);
        } else {
            console.log("error", error)
            console.log(response)
            callback && callback(false, "{}");
        }
    })

    setTimeout(function () {
        callback(true, "Time out happened")
    }, 10000);
}