/* ------------------------------------------------------------------------
Class: jPath
Use: xpath equivalent for JSON Data
Author: Satyajit Paul
Version: 1.1
Date: 04/30/2017
Location: Bangalore, India
License Terms: https://github.com/satyapaul/jpath/blob/master/license.txt
------------------------------------------------------------------------- */
(function($) {
    // the constructor
    this.jPath = function() {
        //console.log('inside the jPath function');
        
        // Create global element references
        $.fn.isJsonDataUrl = null;
        $.fn.url = null;
        $.fn.jsonData = null;
        
        // Define option defaults
        var defaults = {
            isJsonDataUrl: false,
            url: "",
            jsonData : ""
        }
        

        // Create options by extending defaults with the passed in arugments
        if (arguments[0] && typeof arguments[0] === "object") {
            this.options = extendDefaults(defaults, arguments[0]);
        }
        if( defaults.isJsonDataUrl == true) {
            $.fn.url = defaults.url;
        } else {
            $.fn.jsonData = defaults.jsonData;
        }
    }
    
    function extendDefaults(source, properties) {
        var property;
        for (property in properties) {
            if (properties.hasOwnProperty(property)) {
                source[property] = properties[property];
            }
        }
        return source;
    }
    
    // public method
    jPath.prototype.setJSONData = function(_jsonData) {
        $.fn.jsonData = _jsonData;
    }

    // public method
    jPath.prototype.getJSONData = function() {
        console.log("JSON Data = " + $.fn.jsonData);
        return $.fn.jsonData;
    }
        
    // public method
    jPath.prototype.loadJSONDataFromUrl = function(_url, callback) {
         $.getJSON('http://anyorigin.com/go?url='+_url+'&callback=?', function(data) {
            $.fn.jsonData = JSON.stringify(data.contents);
        }).done(function(json){
            if( callback != null) {
                callback();
            }
        });
    }
    
    // public method
    jPath.prototype.getJPathValue = function(_jpath) {
        if( _jpath.charAt(0) != "/") return "Malformed jpath, doesn't start with '/'";
        var jData = JSON.parse($.fn.jsonData);
        getJPathValueForJSON(_jpath, jData, 1);
        
        var retVal = '';
        for(var g = 0; jPathResult.length; g++) {
            var obj = getJSONValue(jPathResult.pop());
            
            if( typeof obj === 'number' || typeof obj === 'string' ) {
                retVal = retVal + "\n" +  obj;
            } else {
                retVal = retVal + "\n" +  JSON.stringify(obj);
            }
        }
        return retVal;        
    }
    
    var jPathResult = [];
    

    var getJPathValueForJSON = function(_jpath, jData, counter) {
        var jpathSplit = _jpath.split('/');
        var jPathNew = '.';
        for( var h = 1 ; h < counter; h++) {
            jPathNew = jPathNew+jpathSplit[h]+'.';
        }
        var i = counter;
        var size = jpathSplit.length;
        if( i < size) {
            if( jpathSplit[i].indexOf('[') != -1 ) {
                var str = jpathSplit[i].substring(jpathSplit[i].indexOf('[')+1, jpathSplit[i].indexOf(']'));
                var lhs = jpathSplit[i].substring(0, jpathSplit[i].indexOf('['));
                var rhs = jpathSplit[i].substring(jpathSplit[i].indexOf(']')+1);
                
                if( !str || str.length === 0 ) {
                    if( jPathNew.endsWith('.') ) {
                        jPathNew = jPathNew+lhs;
                    } else {
                        jPathNew = jPathNew+'.'+lhs;
                    }
                    var dataSize = eval("jData"+jPathNew).length;
                    for( var j = 0 ; j < dataSize ; j++) {
                        jpathSplit[i] = lhs+'['+j+']'+rhs;
                        getJPathValueForJSON(jpathSplit.join('/'), jData, i+1);
                    }
                } else if( str.indexOf('-') != -1) {
                    var range = str.split('-');
                    var startVal = range[0];
                    var endVal = range[1];
                    console.log(startVal+" :: " + endVal);
                    for(; startVal <= endVal; startVal++) {
                        jpathSplit[i] = lhs+'['+ startVal +']'+rhs;
                        getJPathValueForJSON(jpathSplit.join('/'), jData, i+1);
                    }
                } else if( str.indexOf('=') != -1) {
                    console.log("Not Yet Implemented");
                } else if( str.indexOf('>') != -1) {
                    console.log("Not Yet Implemented");
                } else if( str.indexOf('<') != -1) {
                    console.log("Not Yet Implemented");
                } else if( str.indexOf('>=') != -1) {
                    console.log("Not Yet Implemented");
                } else if( str.indexOf('<=') != -1) {
                    console.log("Not Yet Implemented");
                } else if( str.indexOf('!=') != -1) {
                    console.log("Not Yet Implemented");
                } else if( str.indexOf(',') != -1) {
                    console.log("For list of comma separated items. Not Yet Implemented");
                } else {
                    getJPathValueForJSON(jpathSplit.join('/'), jData, i+1);
                }
            } else {
                getJPathValueForJSON(jpathSplit.join('/'), jData, i+1);
            }
        } else {
            jPathResult.push( jpathSplit.join('/') );
        }
    }
        
    var getJSONValue = function(_jpath) {
        if( _jpath.charAt(0) != "/") return "Malformed jpath, doesn't start with '/'";
        var jData = JSON.parse($.fn.jsonData);
        console.log(_jpath);
        var jpath = _jpath.split('/').join('.');
        try {
            return eval("jData"+jpath);
        } catch(err) {
            return "Malformed jpath, ensure jpath doesn't end with '/'";
        }
    }

})(jQuery);
