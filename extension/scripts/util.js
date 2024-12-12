const convertResultTableHeader = (header) => {
    switch (header) {
        case '문제번호':
        case '문제':
            return 'problemId';
        case '난이도':
            return 'level';
        case '결과':
            return 'result';
        case '문제내용':
            return 'problemDescription';
        case '언어':
            return 'language';
        case '제출 번호':
            return 'submissionId';
        case '아이디':
            return 'username';
        case '제출시간':
        case '제출한 시간':
            return 'submissionTime';
        case '시간':
            return 'runningTime';
        case '메모리':
            return 'memorySize';
        case '코드 길이':
            return 'length';
        default:
            return 'unknown';
    }
};

const convertDate = (input) => {
    const regex = /(\d{4})년 (\d{1,2})월 (\d{1,2})일 (\d{2}):(\d{2}):(\d{2})/;
    const match = input.match(regex);

    if (!match) {
        return 'Invalid date format';
    }

    // Date 객체생성
    const year = parseInt(match[1], 10);
    const month = parseInt(match[2], 10) - 1;
    const day = parseInt(match[3], 10);
    const hours = parseInt(match[4], 10);
    const minutes = parseInt(match[5], 10);
    const seconds = parseInt(match[6], 10);

    const date = new Date(year, month, day, hours, minutes, seconds);

    // ISO 8601로 변환
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date
        .getDate()
        .toString()
        .padStart(2, '0')}T${date.getHours().toString().padStart(2, '0')}:${date
        .getMinutes()
        .toString()
        .padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`;
};
