var http = require("http");
var request = require('request')
var console = require('console')
var randomstring = require("randomstring");

exports.getMetadata = function (resourceType, callback) {
    var response = {
        "name": "direct_return_30_attributes_0.js",
        "resourceType": resourceType,
        "attribute_3": randomstring.generate(5),
        "attribute_4": randomstring.generate(5),
        "attribute_5": randomstring.generate(5),
        "attribute_6": randomstring.generate(5),
        "attribute_7": randomstring.generate(5),
        "attribute_8": randomstring.generate(5),
        "attribute_9": randomstring.generate(5),
        "attribute_10": randomstring.generate(5),
        "attribute_11": randomstring.generate(5),
        "attribute_12": randomstring.generate(5),
        "attribute_13": randomstring.generate(5),
        "attribute_14": randomstring.generate(5),
        "attribute_15": randomstring.generate(5),
        "attribute_16": randomstring.generate(5),
        "attribute_17": randomstring.generate(5),
        "attribute_18": randomstring.generate(5),
        "attribute_19": randomstring.generate(5),
        "attribute_20": randomstring.generate(5),
        "attribute_21": randomstring.generate(5),
        "attribute_22": randomstring.generate(5),
        "attribute_23": randomstring.generate(5),
        "attribute_24": randomstring.generate(5),
        "attribute_25": randomstring.generate(5),
        "attribute_26": randomstring.generate(5),
        "attribute_27": randomstring.generate(5),
        "attribute_28": randomstring.generate(5),
        "attribute_29": randomstring.generate(5),
        "attribute_30": randomstring.generate(5)
    };
    callback && callback(true, JSON.stringify(response));
}

exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    var response = {
        "name": "direct_return_30_attributes_0.js",
        "entityType": entityType,
        "attribute_3": randomstring.generate(5),
        "attribute_4": randomstring.generate(5),
        "attribute_5": randomstring.generate(5),
        "attribute_6": randomstring.generate(5),
        "attribute_7": randomstring.generate(5),
        "attribute_8": randomstring.generate(5),
        "attribute_9": randomstring.generate(5),
        "attribute_10": randomstring.generate(5),
        "attribute_11": randomstring.generate(5),
        "attribute_12": randomstring.generate(5),
        "attribute_13": randomstring.generate(5),
        "attribute_14": randomstring.generate(5),
        "attribute_15": randomstring.generate(5),
        "attribute_16": randomstring.generate(5),
        "attribute_17": randomstring.generate(5),
        "attribute_18": randomstring.generate(5),
        "attribute_19": randomstring.generate(5),
        "attribute_20": randomstring.generate(5),
        "attribute_21": randomstring.generate(5),
        "attribute_22": randomstring.generate(5),
        "attribute_23": randomstring.generate(5),
        "attribute_24": randomstring.generate(5),
        "attribute_25": randomstring.generate(5),
        "attribute_26": randomstring.generate(5),
        "attribute_27": randomstring.generate(5),
        "attribute_28": randomstring.generate(5),
        "attribute_29": randomstring.generate(5),
        "attribute_30": randomstring.generate(5)
    };
    callback && callback(true, JSON.stringify(response));
}