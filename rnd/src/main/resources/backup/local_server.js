var express = require('express');
var app = express();
var randomstring = require("randomstring");


app.get('/metadata', function (req, res) {
    console.log("I am getting request:::::::::::");
    var metadataResponse = {
        "name": req.query.scriptName,
        "entityType": "SAP",
        "resourceType": req.query.resourceType + Math.floor(Math.random() * 100),
        "description": "CC_"+randomstring.generate(5),
        "version": 1,
        "attributes": [{ "attributeId": "CC_"+randomstring.generate(5), "datatype": "STRING", "possibleValues": ["CC_"+randomstring.generate(5), "CC_"+randomstring.generate(5), "CC_"+randomstring.generate(5), "CC_"+randomstring.generate(5)], "default": "CC_"+randomstring.generate(5), "ttl": "in mins" }]
    };
    console.log("Got a GET request for the homepage");

    res.send(JSON.stringify(metadataResponse));
})


app.get('/attributeValue', function (req, res) {
    var attributeValue = {
        "attributeId": req.query.attributeId,
        "resourceType": req.query.resourceType,
        "entityType": req.query.entityType,
        "attributeValue": "confidiential level " + Math.floor(Math.random() * 10),
        "otherAttributes": [{ "attributeId": "docType", "attributeValue": "word", "ttl": 3 },
        { "attributeId": "docName", "attributeValue": "namelist", "ttl": 3 },
        { "attributeId": "department", "attributeValue": "IT", "ttl": 3 }
        ]
    }
    console.log("Got a GET request for the homepage");

    res.send(JSON.stringify(attributeValue));
})


var server = app.listen(8081, function () {

    var host = server.address().address
    var port = server.address().port

    console.log("Example app listening at http://%s:%s", host, port)
})