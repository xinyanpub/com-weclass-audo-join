## PhoneGap/Cordova AudioEncode Plugin
 * Original by Jonathan Zhang

## About this Plugin

This plugin lets you easily join multiple audio files into one file.

## Using the Plugin

The plugin creates a function at window.audioJoin(destinationPath, sourcePaths, success, fail) method.
 * destinationPath: (required) This is a string path to the local file. This is typically the fullPath property of the entry passed to the success of a fileSystem.root.getFile call
 * sourcePaths: (required) This is a Array paths to multiple files.
 * success: (required) This function is called when the encoding has completed successfully.
 * fail: (required) This function is called on encode failure and will be passed a statusCode.

Example:

```
window.audioJoin(destinationPath, sourcePaths, success, fail);

var success = function() {
  // Do something with your joined audio (upload it?  - see notes in Xcode example)
}

var fail = function(statusCode) {
  // Why did it fail? - look in the plug in for source of error codes.
  console.log(statusCode);
}
```

## Adding the Plugin ##

```
  cordova plugin add https://github.com/xinyanpub/com-weclass-audo-join.git
```

## LICENSE ##

The MIT License
