export async function requestKakaoLogin() {
    const url = "/oauth2/authorization/kakao";

    await fetch(url).then(
        response => {
            if (response.ok) {
                response.json().then(json => {
                    localStorage.setItem("accessToken", json.accessToken);
                });
            }
        }
    );
}

export async function requestAppleLogin() {
    const url = "/oauth2/authorization/apple";

    await fetch(url).then(
        response => {
            if (response.ok) {
                response.json().then(json => {
                    localStorage.setItem("accessToken", json.accessToken);
                });
            }
        }
    );
}

export async function requestGoogleLogin() {
    const url = "/oauth2/authorization/google";

    await fetch(url).then(
        response => {
            if (response.ok) {
                response.json().then(json => {
                    localStorage.setItem("accessToken", json.accessToken);
                });
            }
        }
    );
}