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
    jPath.prototype.loadJSONDataFromUrl = function(_url, callback) {
         $.getJSON('http://anyorigin.com/go?url='+_url+'&callback=?', function(data) {
            $.fn.jsonData = JSON.stringify(data.contents);
        }).done(function(json){
            if( callback != null) {
                callback();
            }
        });
    }
    
    jPath.prototype.getJPathValue = function(_jpath) {
        if( _jpath.charAt(0) != "/") return "Malformed jpath, doesn't start with '/'";
        var jData = JSON.parse($.fn.jsonData);
        console.log("jpath = " + _jpath);
        var jpath = _jpath.split('/').join('.');
        var val = eval("jData"+jpath);
        //console.log('value = ' + JSON.stringify(val) );
        return val;
    }
        
    jPath.prototype.getJSONData = function() {
        console.log("JSON Data = " + $.fn.jsonData);
        return $.fn.jsonData;
    }
    
})(jQuery);
