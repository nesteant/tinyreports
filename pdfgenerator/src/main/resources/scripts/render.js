try {
    var page = require('webpage').create();
    var address = (phantom.args[0]);
    var unwrap = phantom.args[1];
    if (unwrap === undefined) {
        unwrap = 'false';
    }
    if (unwrap === 'true') {
        var shouldUnwrapSvg = Boolean(unwrap);
    } else {
        shouldUnwrapSvg = false;
    }
    page.open(address, function(status) {
        if (status !== 'success') {
            console.error('Unable to access network');
            phantom.exit(-1);
        }
        var interval = setInterval(function() {
            var p = page.evaluate(function() {
                try {
                    console.log(printReady);
                } catch (err) {
                    return undefined;
                }
                return printReady;
            });
            if (p == undefined || p == true) {
                var result = page.evaluate(function(shouldUnwrapSvg) {
                    var scripts = document.getElementsByTagName('script');
                    var i = scripts.length;
                    while (i--) {
                        scripts[i].parentNode.removeChild(scripts[i]);
                    }
                    if (shouldUnwrapSvg) {
                        var svgs = document.getElementsByTagName('svg');
                        for (var k = 0; k < svgs.length; k++) {
                            var svgElem = svgs[k];
                            var onlyChild = true;
                            var parent;
                            var currentEl = svgElem;
                            while (onlyChild) {
                                parent = currentEl.parentNode;
                                var count = parent.childNodes.length;
                                if (count > 1 || parent.hasAttribute('stop-unwrap')) {
                                    onlyChild = false;
                                    break;
                                }
                                currentEl = parent;
                            }
                            parent.replaceChild(svgElem, currentEl);
                        }
                    }
                    return document.getElementsByTagName('html')[0].outerHTML;
                }, shouldUnwrapSvg);
                clearInterval(interval);
                console.info(result);
                phantom.exit()
            }
        }, 100);

        setTimeout(function() {
            var result = page.evaluate(function() {
                var scripts = document.getElementsByTagName('script');
                var i = scripts.length;
                while (i--) {
                    scripts[i].parentNode.removeChild(scripts[i]);
                }
                return document.getElementsByTagName('html')[0].outerHTML;
            });
            console.info(result);
            phantom.exit()
        }, 10 * 60 * 1000);
    });
} catch (err) {
    console.log(err);
    phantom.exit(-1);
}
