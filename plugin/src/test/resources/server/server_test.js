var express = require('express'); 
var app = express(); 
var fs = require('fs'); 
var path = require('path')
var http = require('http'); 
var https = require('https'); 

var randomstring = require("randomstring"); 

var testData = require('./testdata')

app.get('/attributeValue', function (req, res) {
    console.log("#####################################################################################"); 
    console.log("received request for attribute [" + req.query.attributeId + "] for entity [" + req.query.entityId + " ] of type [" + req.query.entityType + "], model type [" + req.query.resourceType + "]")
    var startTime = new Date().getTime(); 
    var attributeValue =  {
        "attributeId":req.query.attributeId, 
        "resourceType":req.query.resourceType, 
        "entityType":req.query.entityType, 
        "attributeValue":"confidiential level " + Math.floor(Math.random() * 10), 
        "otherAttributes":[ {"attributeId":"docType", "attributeValue":"word", "ttl":3 },  {"attributeId":"docName", "attributeValue":"namelist", "ttl":3 },  {"attributeId":"department", "attributeValue":"IT", "ttl":3 }
        ], 
        "attribute_3":randomstring.generate(5), 
        "attribute_4":randomstring.generate(5), 
        "attribute_5":randomstring.generate(5), 
        "attribute_6":randomstring.generate(5), 
        "attribute_7":randomstring.generate(5), 
        "attribute_8":randomstring.generate(5), 
        "attribute_9":randomstring.generate(5), 
        "attribute_10":randomstring.generate(5), 
        "attribute_11":randomstring.generate(5), 
        "attribute_12":randomstring.generate(5), 
        "attribute_13":randomstring.generate(5), 
        "attribute_14":randomstring.generate(5), 
        "attribute_15":randomstring.generate(5), 
        "attribute_16":randomstring.generate(5), 
        "attribute_17":randomstring.generate(5), 
        "attribute_18":randomstring.generate(5), 
        "attribute_19":randomstring.generate(5), 
        "attribute_20":randomstring.generate(5), 
        "attribute_21":randomstring.generate(5), 
        "attribute_22":randomstring.generate(5), 
        "attribute_23":randomstring.generate(5), 
        "attribute_24":randomstring.generate(5), 
        "attribute_25":randomstring.generate(5), 
        "attribute_26":randomstring.generate(5), 
        "attribute_27":randomstring.generate(5), 
        "attribute_28":randomstring.generate(5), 
        "attribute_29":randomstring.generate(5), 
        "attribute_30":randomstring.generate(5)
    }
    // console.log("Got a GET request for the %s", req.url); 
    if (testData[req.query.entityId] && testData[req.query.entityId][req.query.attributeId]) {
        attributeValue.attributeValue = testData[req.query.entityId][req.query.attributeId]
    }
    var resp = JSON.stringify(attributeValue); 
    console.log("returning:", resp)
    console.log("it takes " + (new Date().getTime() - startTime) + " ms"); 
    res.send(resp); 
})

http.createServer(app).listen(3000, function () {
    console.log("Example app listening at port 3000")
}); 

https.createServer( {
    key:fs.readFileSync(path.join(__dirname, 'key.pem')), 
    cert:fs.readFileSync(path.join(__dirname, 'cert.pem')),
    passphrase: 'nextlabs'
}, app).listen(3443); 