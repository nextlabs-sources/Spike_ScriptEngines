var http = require("http");
var request = require('request')

console.log("Inside test script");

exports.test = function (p1, p2, callback){
    setTimeout(function() {
        callback(p1*p2);
    }, 10000);
    //callback(p1*p2);
}