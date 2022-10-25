var http = require("http");
var request = require('request')
var console = require('console')
var log4js = require('log4js');
log4js.configure({
    appenders: [
        { type: 'file', filename: 'logs' + (new Date().getMinutes()) + '.log', category: 'cheese' }
    ]
});


var logger = log4js.getLogger('cheese');
logger.setLevel('DEBUG');



//logger.debug("Some debug messages");

exports.getMetadata = function (resourceType, callback) {

    http.get('http://localhost:8080/metadata?resourceType=user&scriptName=trireme_0', function (res) {
        res.setEncoding('utf8');
        let rawData = '';
        res.on('data', function (chunk) { rawData += chunk });
        res.on('end', function () {
            try {
                let parsedData = JSON.parse(rawData);

                callback && callback(true, rawData)
            } catch (e) {
                console.log(e.message);
            }
        });

        res.on('close', function () {
            logger.debug("on close event")
        })

        res.on('drain', function () {
            logger.debug("on drain event")
        })

        res.on('error', function () {
            logger.debug("on error event")
        })

        res.on('finish', function () {
            logger.debug("on finish event")
        })

        res.on('pipe', function () {
            logger.debug("on pipe event")
        })

        res.on('unpipe', function () {
            logger.debug("on unpipe event")
        })
    }).on('error', function (e) {
        console.log('Got error: ${e.message}');
    });

    setTimeout(function () {
        callback(true, "Time out happened")
    }, 500);
}

exports.getAttributeValue = function (entityType, resourceType, attributeId, callback) {
    var startTime = new Date().getTime()
    logger.debug("1: " + startTime)
    http.get('http://localhost:8080/metadata?resourceType=user&scriptName=trireme_0', function (res) {
        res.setEncoding('utf8');
        let rawData = '';
        res.on('data', function (chunk) {
            logger.debug("2: " + new Date().getTime())
            rawData += chunk
        });
        res.on('end', function () {
            logger.debug("3: " + new Date().getTime())
            logger.debug("Time taken to get response: " + (new Date().getTime() - startTime));
            logger.debug("----------------------------------------------------")
            try {
                callback && callback(true, rawData)
            } catch (e) {
                console.log(e.message);
            }
        });

        res.on('close', function () {
            logger.debug("on close event")
        })

        res.on('drain', function () {
            logger.debug("on drain event")
        })

        res.on('error', function () {
            logger.debug("on error event")
        })

        res.on('finish', function () {
            logger.debug("on finish event")
        })

        res.on('pipe', function () {
            logger.debug("on pipe event")
        })

        res.on('unpipe', function () {
            logger.debug("on unpipe event")
        })

    }).on('error', function (e) {
        console.log('Got error: ${e.message}');
    });

    setTimeout(function () {
        callback(true, "Time out happened")
    }, 500);
}