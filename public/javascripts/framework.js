/*
    Helper functions for the first exercise of the Web Engineering course
*/

/* 
    checks if native form validation is available.
    Source/Further informations: http://diveintohtml5.info/everything.html
*/
function hasFormValidation() {
    return 'noValidate' in document.createElement('form');
}

/* 
    checks if native date input is available.
    Source/Further informations: http://diveintohtml5.info/everything.html
*/
function hasNativeDateInput() {
    var i = document.createElement('input');
    i.setAttribute('type', 'date');
    return i.type !== 'text';
}

var DATE_DELIMITERS = ['/','\\','-'];

/*
    returns the string representation of a date input field in the format dd.mm.yyyy.
    If the value of the input field can't be interpreted as a date, the original value is returned.
*/
function getNormalizedDateString(selector) {
    value = $(selector).val();
    
    // normalize delimiter to .
    for(var i = 0; i < DATE_DELIMITERS.length; i++) 
        value = value.split(DATE_DELIMITERS[i]).join(".");
    
    // check if date might be reverse, i.e., yyyy.mm.dd
    rehtml5 = /^(\d{4})\.(\d{1,2})\.(\d{1,2})$/;
    if(regs = value.match(rehtml5))
        value = regs[3] + "." + regs[2] + "." + regs[1];

    // check if valid date string dd.mm.yyyy
    date = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
    if(value.match(date))
      return value;
    return $(selector).val();
}

/*
    returns the string representation of the given value (seconds) in the format mm:ss.
*/
function secToMMSS(value){
    var minutes = Math.floor(value / 60);
    var seconds = (value % 60);
    
    if(seconds < 10) {
        seconds = "0" + seconds;
    }
    if(minutes < 10) {
        minutes = "0" + minutes;
    }
    return minutes + ":" + seconds;
}

/* 
  checks if native form validation is available.
  Source/Further informations: http://diveintohtml5.info/storage.html
*/
function supportsLocalStorage() {
    try {
        return 'localStorage' in window && window['localStorage'] !== null;
    } catch(e) {
        return false;
    }
}
