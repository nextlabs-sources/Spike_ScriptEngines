var http = require("http");
var request = require('request')

exports.getMetadata = function (resourceType, callback) {
    // var metadataResponse = {

    //     "name": "unique.script.name",
    //     "entityType": "USER",
    //     "resourceType": resourceType,
    //     "description": "",
    //     "version": 1,
    //     "attributes": [{ "attributeId": "", "datatype": "STRING", "possibleValues": ["some1", "some2", "some3", "some4"], "default": "", "ttl": "in mins" }]
    // };
    // //setTimeout(function () {
    //     callback && callback(true, JSON.stringify(metadataResponse));
    // //}, 1)

    request.get("http://localhost:8081/metadata?resourceType=" + resourceType + "&scriptName=script2", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback && callback(true, body);
        }
    })
}

exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    var attributeValue = {
        "attributeId": attributeId,
        "attributeValue": "some random value",
        "attributes": [{ "attributeId": "docType", "attributeValue": "word", "ttl": 3 },
        { "attributeId": "docName", "attributeValue": "namelist", "ttl": 3 },
        { "attributeId": "department", "attributeValue": "IT", "ttl": 3 }
        ]
    }

    // setTimeout(function () {
    callback && callback(true, JSON.stringify(attributeValue));
    //}, 1)

    // request("https://api.ipify.org?format=json", function (error, response, body) {
    //     if (!error && response.statusCode == 200) {
    //         // console.log(body) // Show the HTML for the Google homepage. 
    //         var x = JSON.stringify({ ip: "M.QL", "id": 12345 });

    //         var attributeValue = {
    //             ""
    //         }

    //         callback && callback(true, body);
    //         //callback && callback({ip:"M.QL","id":12345});
    //     }
    // })


}