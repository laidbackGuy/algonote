let c = console.log.bind(document);
let timerId = null;

const getAllData = async () => {};

const isExistResultTable = () => {
    return document.getElementById('status-table').length !== null;
};

// 채점중인 데이터가 있는지 확인
const isCheckingResult = () => {
    const problemResult = document.querySelectorAll(
        'span[data-color="wait"], span[data-color="judging"], span[data-color="compile"]'
    );

    if (problemResult.length > 0) return true;

    return false;
};

const checkResultTable = () => {
    return new Promise((resolve, reject) => {
        const check = () => {
            if (!isExistResultTable()) {
                alert('결과가 없습니다');
                reject(new Error('결과 테이블이 존재하지 않습니다.'));
            } else if (isCheckingResult()) {
                clearTimeout(timerId);
                timerId = setTimeout(check, 2000);
            } else {
                resolve(parsingResultTable(document));
            }
        };
        check();
    });
};

const unescapeHtml = (html) => {
    const textArea = document.createElement('textarea');
    textArea.innerHTML = html;
    return textArea.value;
};

// 제출 정보 파싱
const parsingResultTable = () => {
    const table = document.getElementById('status-table');
    // 예외처리 추후 추가예정

    // 테이블 카테고리
    const headers = Array.from(table.rows[0].cells, (x) => convertResultTableHeader(x.innerText.trim()));

    const list = [];
    for (let i = 1; i < table.rows.length; i++) {
        const row = table.rows[i];
        const cells = Array.from(row.cells, (x, index) => {
            switch (headers[index]) {
                case 'submissionId':
                    return Number(x.innerText.trim());
                case 'result':
                    return {
                        result: x.innerText.trim(),
                    };
                case 'language':
                    return unescapeHtml(x.innerText).replace(/\/.*$/g, '').trim();
                case 'memorySize':
                    return Number(x.innerText.trim());
                case 'runningTime':
                    return Number(x.innerText.trim());
                case 'length':
                    return Number(x.innerText.trim());
                case 'submissionTime':
                    const el = x.querySelector('a.show-date');
                    if (isNull(el)) return null;
                    return el.getAttribute('data-original-title');
                case 'problemId':
                    const a = x.querySelector('a.problem_title');
                    if (isNull(a)) return null;
                    return {
                        problemId: a.getAttribute('href').replace(/^.*\/([0-9]+)$/, '$1'),
                    };
                default:
                    return x.innerText.trim();
            }
        });
        let obj = {};
        for (let j = 0; j < headers.length; j++) {
            obj[headers[j]] = cells[j];
        }
        obj = { ...obj, ...obj.result, ...obj.problemId };
        list.push(obj);
    }

    return list;
};

// 제출 코드 직접 파싱
const getSubmitCode = async (submissionId) => {
    const code = await fetch(`https://www.acmicpc.net/source/download/${submissionId}`, { method: 'GET' }).then((res) =>
        res.text()
    );
    console.log(code);
    return code;
};

// 제출 코드 캐시에서 파싱
