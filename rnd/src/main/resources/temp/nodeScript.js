exports.getMetadata = function (resourceType, callback) {
    var metadataResponse = {
        "name": "unique.script.name",
        "entityType": "USER",
        "resourceType": resourceType,
        "description": "",
        "version": 1,
        "attributes": [{ "attributeId": "", "datatype": "STRING", "possibleValues": ["some1", "some2", "some3", "some4"], "default": "", "ttl": "in mins" }]
    };
    
    callback && callback(true, JSON.stringify(metadataResponse));
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

    callback && callback(true, JSON.stringify(attributeValue));
}