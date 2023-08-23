import {authFetch} from "./common/apiUtil.js";
import {convertHyphenToCamelCase, deleteCookie} from "./common/utilTool.js";
import {DivTag, ModalTag} from "./common/tagUtil.js";

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

        bindEventToMyPage();
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

function bindEventToMyPage() {
    const withdrawBtn = document.querySelector("#withdraw-btn");
    withdrawBtn.addEventListener("click", clickWithdrawBtn);
}

async function clickWithdrawBtn() {

    const modal = new ModalTag()
        .setId("modal-parent")
        .setStyle([{
            position: "fixed",
            top: 0,
            left: 0,

        }])
        .addInnerHTML(
            new DivTag()
                .setId("modal-content")
                .setClassName("modal")
                .setStyle([{
                    width: "30%",
                    height: "35%",
                    display: "block",
                    backgroundColor: "white",
                    zIndex: 1001,
                    margin: "1% 20% 1% 20%",
                    position: "fixed",
                    top: "30%",
                    left: "15%"
                }])
        ).getTag();

    const url = "/withdraw/page";
    let option = {method: "GET"};

    const parent = document.querySelector("#my-page-space");
    await authFetch(url, option)
        .then(res => {
            modal.querySelector("#modal-content").innerHTML = res;
            modal.style.display = "block";
            parent.appendChild(modal);

            bindWithdrawEvent();
        });
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
        console.log(res);
    });

    deleteCookie("accessToken");
    deleteCookie("refreshToken");


    // 스탬프 삭제
    // 커스텀 카드 삭제
    // 멤버 카드 삭제
    // 멤버 레터 삭제
    // 멤버 삭제
}
