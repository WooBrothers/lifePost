export async function checkAndRefreshToken() {

    const url = "/auth/new/token";
    let option = {
        method: "GET"
    }
    await authFetch(url, option);
}

export async function authFetch(url, option) {
    const tokenPrefix = "Bearer ";
    const accessToken = tokenPrefix + getAccessToken();
    const refreshToken = tokenPrefix + getRefreshToken();

    if (option.hasOwnProperty("headers")) {
        option.headers["Authorization"] = accessToken;
    } else {
        option.headers = {"Authorization": accessToken};
    }

    return await fetch(url, option).then(
        response => {
            if (response.ok || response.status === 201) {
                return response.json();
            } else if (response.status === 401) {
                // 401 요청 실패 시 리프레쉬 토큰 저장 후 재요청
                option.headers["Authorization-refresh"] = refreshToken;

                fetch(url, option).then(
                    response => {
                        if (response.ok || response.status === 201) {
                            return response.json();
                        } else {
                            console.log("요청 실패");
                            throw new Error("요청 실패");
                        }
                    }
                );
            }
        }
    );
}

export function getCookieValue(name) {
    // 쿠키들을 배열로 분할
    const cookies = document.cookie.split(';');

    for (let i = 0; i < cookies.length; i++) {
        // 앞뒤 공백 제거
        const cookie = cookies[i].trim();
        // 쿠키 이름과 값을 분리
        const [cookieName, cookieValue] = cookie.split('=');

        if (cookieName === name) {
            // 쿠키 값을 반환하고 함수 종료
            return decodeURIComponent(cookieValue);
        }
    }

    // 해당하는 쿠키가 없는 경우 null 반환
    return null;
}

export function getAccessToken() {
    // 쿠키들을 배열로 분할
    return getCookieValue("accessToken");
}

export function getRefreshToken() {
    // 쿠키들을 배열로 분할
    return getCookieValue("refreshToken");
}
