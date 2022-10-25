var request = require('request')
var http = require("http"); 
var console = require('console')
var util = require('util')

var debug = function() {}
if (process.env.NODE_DEBUG) {
    debug = function() {
        console.error('TEST %s', util.format.apply(util, arguments))
    }
}

exports.getMetadata = function (callback) {
    debug("getting metadata")
    var resp = [ {
        entityType:"SUBJECT", 
        modelType:"user", 
        attr:[ {
            name:"department", 
        },  {
            name:"citizenship", 
        }]
    }]
    setTimeout(function () {
        callback && callback(true, JSON.stringify(resp))
    }, 10); 
    // setTimeout(function () {
    //     callback(true, "Time out happened")
    // }, 10000);
}

exports.getAttributeValue = function(entityType, resourceType, attributeId, entityId, callback) {
    var timer; 
    // debug(entityType, resourceType, attributeId, entityId)
    debug("sending request")
    var url = "https://localhost:3443/attributeValue?resourceType=" + resourceType + "&entityType=" + entityType + "&attributeId=" + attributeId + "&entityId=" + entityId
    request({url: url, time:true, strictSSL: false}, function (error, response, body) {
        debug("response received")
        // debug(body)
        // clearTimeout(timer);
        if ( ! error && response.statusCode == 200) {
            callback && callback(true, body); 
        }else {
            callback && callback(false, "{}"); 
        }
    })
    // timer = setTimeout(function () {
    //     callback(false, "Time out happened")
    // }, 10000); 
}