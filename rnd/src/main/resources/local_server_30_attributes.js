var express = require('express');
var app = express();
var randomstring = require("randomstring");


app.get('/metadata', function (req, res) {
    console.log("I am getting request:::::::::::");
    var metadataResponse = {
        "name": req.query.scriptName,
        "entityType": "SAP",
        "resourceType": req.query.resourceType + Math.floor(Math.random() * 100),
        "description": "CC_" + randomstring.generate(5),
        "version": 1,
        "attributes": [{ "attributeId": "CC_" + randomstring.generate(5), "datatype": "STRING", "possibleValues": ["CC_" + randomstring.generate(5), "CC_" + randomstring.generate(5), "CC_" + randomstring.generate(5), "CC_" + randomstring.generate(5)], "default": "CC_" + randomstring.generate(5), "ttl": "in mins" }],
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
        ],
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
    }
    console.log("Got a GET request for the homepage");

    res.send(JSON.stringify(attributeValue));
})


var server = app.listen(8080, function () {

    var host = server.address().address
    var port = server.address().port

    console.log("Example app listening at http://%s:%s", host, port)
})