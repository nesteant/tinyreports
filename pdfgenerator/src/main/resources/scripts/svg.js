#! /usr/bin/env phantomjs
try {
    var svgUrl, width, height, outputFile;
    var webpage = require('webpage'), fs = require('fs'), system = require('system');
    svgUrl = phantom.args[0];
    width = phantom.args[1];
    height = Number(phantom.args[2]);
    outputFile = phantom.args[3];
    var svg = fs.read(svgUrl);
    var svgViewPort = webpage.create();
    svgViewPort.viewportSize = {width: width, height: height};
    svgViewPort.content = '<html><body style="margin: 0">' + svg + '</body></html>';
    svgViewPort.render(outputFile);
    phantom.exit();
}
catch
    (err) {
    console.log(err);
    phantom.exit(-1);
}
