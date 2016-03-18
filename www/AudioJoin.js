var exec = require('cordova/exec');

var audioJoin = function(dstPath, srcPaths, successCallback, failCallback) {
  console.log("audioJoin");
  console.log(dstPath);
  console.log(srcPaths);
  exec(successCallback, failCallback, 'AudioJoin', 'join', [{dstPath: dstPath, srcPaths: srcPaths}]);
};

module.exports = audioJoin;
