/*
 Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有
 修改标识：曹建君 2019年8月5日
 功能描述：此为手机端根据1920宽度动态进行缩放。
 */
;(function (doc, win) {
            //文字初始化
            function adbt(designWidth, rem2px) {
            	var h = document.getElementsByTagName('html')[0];
            	var htmlFontSize = parseFloat(window.getComputedStyle(h, null)
                                    .getPropertyValue('font-size')),
        	    docEl = doc.documentElement,
                resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
                recalc = function () {
                    var clientWidth = docEl.clientWidth;
                    if (!clientWidth) return;
                    if (clientWidth > 1920) {
                        docEl.style.fontSize = 1920 / designWidth * rem2px + 'px';
                    } else {
                        docEl.style.fontSize = clientWidth / designWidth * rem2px / htmlFontSize * htmlFontSize+ 'px';
                    }
                };

	            var clientWidth = docEl.clientWidth;
	            docEl.style.fontSize = clientWidth / designWidth * rem2px / htmlFontSize * htmlFontSize+ 'px';
	            // Abort if browser does not support addEventListener
	            if (!doc.addEventListener) return;
	            win.addEventListener(resizeEvt, recalc, false);
	            doc.addEventListener('DOMContentLoaded', recalc, false);
	            return htmlFontSize;
            }
            //设置 设计稿的尺寸和比例
            adbt(1920, 100)

})(document, window);

window.onload=function () {//禁止缩放页面
    document.addEventListener('touchstart',function (event) {
        if(event.touches.length>1){
            event.preventDefault();
        }
    });
    var lastTouchEnd=0;
    document.addEventListener('touchend',function (event) {
        var now=(new Date()).getTime();
        if(now-lastTouchEnd<=300){
            event.preventDefault();
        }
        lastTouchEnd=now;
    },false)
};
