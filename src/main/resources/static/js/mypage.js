import {authFetch} from "./common/apiUtil.js";
import {convertHyphenToCamelCase, deleteCookie} from "./common/utilTool.js";

await renderMyPageInfo();

async function renderMyPageInfo() {
    await setMemberInfo();
}

async function setMemberInfo() {
    const url = "/api/v1/member/auth/info";
    let options = {method: "GET"};

    await authFetch(url, options).then(res => {
        const memberBoxSpace = document.querySelector("#member-info-box-space");
        const contentsDivList = memberBoxSpace.querySelectorAll(".info-contents-class");

        setInnerHtmlByResponse(res, contentsDivList);

        bindWithdrawEvent();
    });
}

function setInnerHtmlByResponse(res, contentsDivList) {
    let contentsDict = {};

    contentsDivList.forEach(div => {
        const key = convertHyphenToCamelCase(div.id);
        contentsDict[key] = div;
    });

    for (let key in res) {
        if (res.hasOwnProperty(key)) {
            contentsDict[key].innerHTML = res[key];
        }
    }
}

function bindWithdrawEvent() {
    document.querySelector("#agree-check-box")
        .addEventListener("change", changeCheckBox);

    document.querySelector("#withdraw-ok-btn")
        .addEventListener("click", clickWithdrawOkBtn);
}

function changeCheckBox() {
    document.querySelector("#withdraw-ok-btn").disabled = !this.checked;
}

async function clickWithdrawOkBtn() {
    // 토큰 삭제

    const url = "/api/v1/member/auth/withdraw";
    let options = {
        method: "POST"
    };

    await authFetch(url, options).then(res => {
    });

    deleteCookie("accessToken");
    deleteCookie("refreshToken");
}
