// insert style tag in head and inject css content from parameter
var styleTag = document.createElement('style');
document.getElementsByTagName('head')[0].appendChild(styleTag);
styleTag.innerHTML = arguments[0];

// create an overlay div in body
var divTag = document.createElement('div');
// divTag.style.position = '-webkit-sticky';
divTag.style.position = 'fixed';
divTag.style.left = '0';
divTag.style.top = '0';
divTag.style.backgroundColor = '#2bc03e';
divTag.style.border = '3px dashed red';
divTag.style.zIndex = 10000;

document.getElementsByTagName('body')[0].appendChild(divTag);
divTag.innerHTML = '<span><input type="submit" value="next" /></span>'
		// + '<span><input type="text" value="1000"
		// onChange="window.autoInterval=this.value"></span>'
		+ '<span><label for="autoIntervalInput"></label></span>'
		+ '<span><input id="autoIntervalInput" type="range" min="100" max="1000" value="50" class="slider" style="appearance: none;" onChange="document.getElementById(\'autoIntervalInput\').value = this.value"></span>';

window.highlightElement = function(selector) {
	console.log('highlight element: ' + selector);
	var element = document.querySelector(selector);
	if (element.offsetParent === null) {
		console.log('element not visible');
	} else {
		element.scrollIntoView(false);
		element.style.backgroundColor = 'red';
	}
}