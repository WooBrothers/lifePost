import {requestKakaoLogin} from "./loginApi.js";

export function bindEventToKakaoLoginBtnClick() {
    /* 카카오 로그인 버튼 클릭 이벤트 바인딩 */


    const kakaoLoginBtn = document.getElementById("kakao-login");
    kakaoLoginBtn.addEventListener("click", requestKakaoLogin);

    // const appleLoginBtn = document.getElementById("apple-login");
    // appleLoginBtn.addEventListener("click", requestAppleLogin);
    //
    // const googleLoginBtn = document.getElementById("google-login");
    // googleLoginBtn.addEventListener("click", requestGoogleLogin);
}