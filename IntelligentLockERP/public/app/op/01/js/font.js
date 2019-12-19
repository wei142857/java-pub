var docEl = document.documentElement;
var width = docEl.getBoundingClientRect().width;
var rem = width / 10;
docEl.style.fontSize = rem + 'px';
//alert(docEl.style.fontSize)
//alert(width)