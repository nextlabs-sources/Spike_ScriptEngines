var http = require("http");
var request = require('request')
var console = require('console')

exports.getMetadata = function (resourceType, callback) {
    var response = {
        "name": "direct_return_2_attributes_1.js",
        "resourceType": resourceType
    };
    callback && callback (true, JSON.stringify(response));
}

exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    var response = {
        "name": "direct_return_2_attributes_1.js",
        "entityType": entityType
    };
    callback && callback (true, JSON.stringify(response));
}