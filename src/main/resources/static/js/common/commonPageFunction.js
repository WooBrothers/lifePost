ifInAppBrowserShowGuide();

function ifInAppBrowserShowGuide() {
    // 인앱 브라우져로 페이지 접근 시 인앱 브라우져 -> 
    // 사파리, 크롬으로 실행 안내 가이드 페이지로 이동

    if (navigator.userAgent.indexOf('KAKAOTALK') > -1) {
        // 카카오 브라우저에서 접근한 경우
        const currentURL = window.location.href;
        location.href = 'kakaotalk://web/openExternal?url=' + encodeURIComponent(currentURL);
    }
}
