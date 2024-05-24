import { useState, useEffect } from 'react'
import Image from 'next/image'
import { useRouter } from 'next/router'
import s from './main.module.scss'
import { getRecentSolvedApi, getUserRecordApi } from '@/apis/analysisAxios'
import myInfo from '@/apis/user-infoAxios'
import { SimpleButton } from '@/components/commons/Buttons/Button'
import Radar from '@/components/commons/Main/Radar'
import Wave from '@/components/commons/Main/Wave'
// import useNoteStore from '@/stores/note-store'
import MetaHead from '@/components/commons/MetaHead'
import { getCookie } from '@/utils/cookie'

interface RecordProps {
  noteCnt: number
  notedProblemCnt: number
  solvedProblemCnt: number
}

interface UserInfo {
  memberId: number
  email: string
  nickname: string
  profileImg: string
}

interface Group {
  group: string
  score: number
  lastSolvedDate: string
  problemCount: number
}

const Main = () => {
  const [info, setInfo] = useState<UserInfo | null>(null)
  const [record, setRecord] = useState<RecordProps | null>(null)
  const [recentSolved, setRecentSolved] = useState<Group[]>([])
  // const { setSelectedNoteData } = useNoteStore()

  const router = useRouter()

  useEffect(() => {
    const accessToken = getCookie('access_token')
    if (!accessToken) {
      router.replace('/landing') // 사용자를 '/landing' 페이지로 리다이렉트합니다.
    }
  }, [router])

  useEffect(() => {
    const fetchData = async () => {
      const userRecord = await getUserRecordApi()
      const userInfo = await myInfo()
      const userRecentSolved = await getRecentSolvedApi()
      setRecord(userRecord)
      setInfo(userInfo.data)
      setRecentSolved(userRecentSolved.groups)
    }
    fetchData()
    // setSelectedNoteData(null)
  }, [])

  const goRec = (queryData: string) => {
    router.push({
      pathname: '/recommend',
      query: { queryData },
    })
  }

  const onClickHandler = () => {
    console.log('클릭')
  }
  return (
    <>
      <MetaHead
        title="균형잡힌 알고리즘 공부 알고노트로 시작하세요"
        description="수준별 문제 추천과 간단한 정리노트 템플릿 제공"
        image="/images/loginLogo"
      />
      <main className={s.main}>
        <div className={s.header}>
          <div className={s.headerSentence}>
            <p className={s.headerLight}>당신의 알고리즘</p>
            <p className={s.headerBold}>약점 보완</p>
            <p className={s.headerLight}>을 위한</p>
          </div>
          <div className={s.headerSentence}>
            <p className={s.headerLight}>가장 완벽한 알고리즘 오답노트,</p>
            <p className={s.headerBold}>알고노트</p>
          </div>
        </div>
        <div className={s.container}>
          <h2 className={s.title}>내 기록</h2>
          <div className={s.analysis}>
            <div className={s.radarCont}>
              <p className={s.graphTitle}>내가 푼 문제 분석</p>
              <div className={s.radarBox}>
                <Radar
                  data={recentSolved.map((item) => item.score)}
                  labels={[
                    '수학 및 이론',
                    '그래프',
                    '자료구조',
                    '전략 및 최적화',
                    '구현',
                    '문자열',
                  ]}
                />
              </div>
            </div>
            <div className={s.right}>
              <div className={s.top}>
                <a href="./member">{info?.nickname}</a>
                <p>님이 알고노트를 사용한 기록이에요</p>
              </div>
              <div className={s.elements}>
                <div className={s.elementCont}>
                  <Image
                    src="/images/record/problem.png"
                    width={28}
                    height={21}
                    alt="problemIcon"
                  />
                  <div className={s.descCont}>
                    <p>푼 문제 개수</p>
                  </div>
                  <div className={s.numCont}>
                    <a href="./solvedproblems">{record?.solvedProblemCnt}</a>
                  </div>
                </div>
                <div className={s.elementCont}>
                  <Image
                    src="/images/record/folder.png"
                    width={28}
                    height={21}
                    alt="folderIcon"
                  />
                  <div className={s.descCont}>
                    <p>노트를 작성한 문제 개수</p>
                  </div>
                  <div className={s.numCont}>
                    <a href="./mynote">{record?.notedProblemCnt}</a>
                  </div>
                </div>
                <div className={s.elementCont}>
                  <Image
                    src="/images/record/note.png"
                    width={28}
                    height={21}
                    alt="noteIcon"
                  />
                  <div className={s.descCont}>
                    <p>작성한 노트 개수</p>
                  </div>
                  <div className={s.numCont}>
                    <a href="./mynote">{record?.noteCnt}</a>
                  </div>
                </div>
              </div>
              <div className={s.btnCont}>
                <SimpleButton
                  text="노트 작성하러 가기"
                  style={{
                    fontWeight: '600',
                    fontSize: '1.1rem',
                    height: '3.4rem',
                    borderRadius: '0.8rem',
                  }}
                  onClick={() => router.push('/solvedproblems')}
                />
              </div>
            </div>
          </div>
        </div>

        <hr className={s.divide} />

        <div className={s.container}>
          <h2 className={s.title}>취약 알고리즘 공략하기</h2>
          <div className={s.recommendCont}>
            <p className={s.recommendDesc}>
              감이 떨어졌을 수 있는 알고리즘 유형을 확인하세요
            </p>
            <div className={s.waveCont}>
              {recentSolved?.map((group) => (
                <div
                  key={group.group}
                  onClick={() => goRec(group.group)}
                  onKeyDown={onClickHandler}
                  role="presentation"
                  className={s.wave}
                >
                  <Wave type={group.group} date={group.lastSolvedDate} />
                </div>
              ))}
            </div>
          </div>
          <SimpleButton
            text="추천 문제 보러가기"
            style={{
              fontWeight: '600',
              fontSize: '1.1rem',
              height: '3.4rem',
              borderRadius: '0.8rem',
            }}
            onClick={() => router.push('/recommend')}
          />
        </div>
      </main>
    </>
  )
}

export default Main
