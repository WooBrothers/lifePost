export async function checkAndRefreshToken() {

    if (getAccessToken()) {
        const url = "/auth/new/token";
        let option = {
            method: "GET"
        }
        return await authFetch(url, option);
    }
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

    option.headers["Content-Type"] = "application/json";

    return await fetch(url, option).then(
        async response => {
            if (option.method === "GET" && response.status === 200) {
                const contentType = response.headers.get("content-type");
                if (contentType && contentType.includes("application/json")) {
                    return response.json();
                } else {
                    return response.text();
                }
            } else if (option.method === "POST" && response.status === 201) {
                return response.text;
            } else if (option.method === "DELETE" && response.status === 204) {
                return response.text;
            } else if (option.method === "PATCH" && response.status === 200) {
                return response.text;
            } else if (response.status === 401) {
                // 401 요청 실패 시 리프레쉬 토큰 저장 후 재요청
                option.headers = {"Authorization-refresh": refreshToken};
                await fetch(url, option).then(
                    async response => {
                        if (response.ok || response.status === 201) {
                            option.headers = {"Authorization": tokenPrefix + getAccessToken()};
                            await fetch(url, option).then(
                                response => {
                                    if (response.ok || response.status === 201) {
                                        return response.json();
                                    } else {
                                        throw new Error("요청 실패");
                                    }
                                }
                            )
                        } else {
                            console.log("토큰 만료 실패");
                            window.location.href = "/login/page";
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
