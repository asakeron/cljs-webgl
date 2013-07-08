var page = require('webpage').create();
var url = phantom.args[0];

page.onConsoleMessage = function(msg) {
	console.log(msg);
};

page.open(url, function (status) {
    var result = page.evaluate(function() {
	console.log(cljs_webgl);
        return cljs_webgl.test.run();
    });

    if (result !== 0) {
        console.log("Test failed!");
        phantom.exit(1);
    }

    console.log("Test succeeded.");
    phantom.exit(0);
});
