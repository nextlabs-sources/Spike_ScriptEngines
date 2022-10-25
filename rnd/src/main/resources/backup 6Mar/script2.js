var http = require("http");
var request = require('request')

exports.getMetadata = function (resourceType, callback) {
    request("http://localhost:8081/metadata?resourceType=" + resourceType + "&scriptName=script2", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback && callback(true, body);
        }
    })
}

exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    request("http://localhost:8081/attributeValue?resourceType=" + resourceType + "&entityType=" + entityType + "&attributeId=" + attributeId, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback && callback(true, body);
        }
    })
}

exports.test = function (p1, p2, callback){
    setTimeout(function() {
        callback(p1*p2);
    }, 10000);
    //callback(p1*p2);
}