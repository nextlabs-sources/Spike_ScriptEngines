var http = require("http");
var request = require('request')
var console = require('console')

exports.getMetadata = function (resourceType, callback) {

    request("http://localhost:8081/metadata?resourceType=" + resourceType + "&scriptName=trireme_1", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback && callback(true, body);
        } else {
            console.log("error", error)
            console.log(response)
            callback && callback(false, "{}");
        }
    })
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

}