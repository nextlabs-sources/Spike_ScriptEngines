var http = require("http");
var request = require('request')

exports.getMetadata = function (resourceType, callback) {
         http = require("http");
    var metadataResponse = {

        "name": "unique.script.name",
        "entityType": "USER",
        "resourceType": resourceType,
        "description": "",
        "version": 1,
        "otherAttributes": [{ "attributeId": "", "datatype": "STRING", "possibleValues": ["", "", "", ""], "default": "", "ttl": "in mins" }]
    };
    setTimeout(function () {
        callback && callback(true, JSON.stringify(metadataResponse));
    }, 1000)
}

exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    request("https://api.ipify.org?format=json", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            // console.log(body) // Show the HTML for the Google homepage. 
            var x = JSON.stringify({ ip: "M.QL", "id": 12345 });
            callback && callback(true, body);
            //callback && callback({ip:"M.QL","id":12345});
        }
    })


}