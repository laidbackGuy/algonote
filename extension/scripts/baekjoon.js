// alert('실행은 됩니다...');

chrome.runtime.onMessage.addListener(
    function(request, sender, sendResponse) { 
    const targetDiv = document.querySelector('.margin-bottom-30');

    if(targetDiv){
        if (request.message === "success") {
            targetDiv.textContent = "알고노트에 업로드 완료!";
        }
        else{
            targetDiv.textContent = "알고노트에 업로드 실패";
        }
    }
    else{
        console.error('지정된 클래스를 가진 div 요소를 찾을 수 없습니다.');
    }
    }
  );

const currentUrl = window.location.href;

const isNull = (value) => {
    return value === null;
};

const isEmpty = (value) => {
    return value === null || value === undefined || value === '';
};

const programStart = async () => {
    // 결과 리스트 파싱
    try {
        const table = await checkResultTable();
        if (table && table.length > 0) {
            for (const [index, ele] of table.entries()) {
                const submissionId = Number(ele.submissionId);
                const code = await getSubmitCode(submissionId);
                table[index].code = code;
                delete table[index].username;
                table[index].submissionTime = convertDate(table[index].submissionTime);
            }

            chrome.runtime.sendMessage({ type: 'sendTable', tableData: table });
        }
    } catch (error) {
        throw error;
    }
};

const getUserName = () => {
    const element = document.querySelector('a.username');
    if (isNull(element)) return null;

    const username = element?.innerText?.trim();
    if (isEmpty(username)) return null;
    return username;
};

const username = getUserName();

// 로그인 상태일 때
if (!isNull(username)) {
    // 현재 페이지가 코드 제출 결과 페이지이면 프로그램 실행
    if (['status', `user_id=${username}`, 'problem_id', 'from_mine=1'].every((key) => currentUrl.includes(key)))
        programStart();
}
