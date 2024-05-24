import { useState, useEffect, KeyboardEvent, ChangeEvent } from 'react'
import ReactDiffViewer, { DiffMethod } from 'react-diff-viewer-continued'
import style from './compare.module.scss'
import { getAllMySolvedList } from '@/apis/problemAxios'
import noteStyle from '@/components/commons/Bookmark/Note.module.scss'
import CodeSelectButton from '@/components/commons/CodeSelectButton'
import ExecuteResult from '@/components/commons/CodeSelectButton/ExcuteResult'
import MetaHead from '@/components/commons/MetaHead'
import Modal from '@/components/commons/Modal'
import cStyle from '@/components/commons/Modal/Modal.module.scss'
import SubmissionList from '@/components/commons/Modal/SubmissionList'
import TierImg from '@/components/commons/Tier'
import useCodeInfo from '@/stores/code-store'

interface Problem {
  id: number
  title: string
  tier: number
  tags: string[]
}

interface ProblemData {
  problem: Problem
  complete: string
  uploadDate: Date
}

export interface DetailProblemType {
  modalStatus: boolean
  problemId: number
}

const ComparePage = () => {
  const [isModalOpened, setIsModalOpened] = useState<boolean>(false)
  const [myProblems, setMyProblems] = useState<ProblemData[]>([])
  const [detailProblems, setDetailProblems] = useState<DetailProblemType>({
    modalStatus: false,
    problemId: 0,
  })
  const codes = useCodeInfo((state) => state.codes)
  const { resetCodes } = useCodeInfo()
  const [language, setLanguage] = useState<string>('java')
  const [inputData, setInputData] = useState<string>('')
  const [expectedOutput, setExpectedOutput] = useState<string>('')

  useEffect(() => {
    const fetchData = async () => {
      const response = await getAllMySolvedList()
      setMyProblems(response)
    }

    fetchData()
    resetCodes()
  }, [])

  useEffect(() => {
    if (isModalOpened) {
      document.body.style.overflow = 'hidden'
    } else {
      document.body.style.overflow = 'auto'
    }

    return () => {
      document.body.style.overflow = 'auto'
    }
  }, [isModalOpened])

  const handleDetailProblems = (
    e: KeyboardEvent<HTMLDivElement>,
    problemId: number,
  ) => {
    if (e.key === 'Enter') {
      setDetailProblems({ modalStatus: true, problemId })
    }
  }

  const handleLanguageChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setLanguage(event.target.value)
  }

  const handleInputChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
    setInputData(event.target.value)
  }

  const handleOutputChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
    setExpectedOutput(event.target.value)
  }

  return (
    <>
      <MetaHead
        title="제출한 코드 내역을 비교해볼 수 있어요"
        description="간단한 클릭으로 코드의 시간복잡도, 공간복잡도, 실행 시간, 메모리를 확인할 수 있어요"
      />
      <div className={style.container}>
        <div className={style.header}>
          <div className={style.headerSentence}>
            <p className={style.headerBold}>코드를 실행해보세요</p>
          </div>
          <div className={style.headerSentence}>
            <p className={style.contentLight}>
              정답과 복잡도를 확인할 수 있어요
            </p>
          </div>
        </div>
        <div className={style.element}>
          <div>
            <div className={style.compareButtons}>
              <div className={style.compareButton}>
                <CodeSelectButton
                  setIsModalOpened={setIsModalOpened}
                  index={0}
                />
              </div>
              <div className={style.compareButton}>
                <CodeSelectButton
                  setIsModalOpened={setIsModalOpened}
                  index={1}
                />
              </div>
            </div>
            <div className={style.codeView}>
              <ReactDiffViewer
                oldValue={codes[0]}
                newValue={codes[1]}
                compareMethod={DiffMethod.WORDS}
              />
            </div>
            <div className={style.insertBox}>
              <select
                value={language}
                onChange={handleLanguageChange}
                className={style.insert}
              >
                <option value="java">Java</option>
                <option value="py">Python</option>
                <option value="c">C</option>
                <option value="cpp">C++</option>
              </select>
              <textarea
                placeholder="테스트 케이스 입력"
                value={inputData}
                onChange={handleInputChange}
                className={style.insert}
              />
              <textarea
                placeholder="예상 출력 결과"
                value={expectedOutput}
                onChange={handleOutputChange}
                className={style.insert}
              />
            </div>
            <div>
              <ExecuteResult
                language={language}
                inputData={inputData}
                expectedOutput={expectedOutput}
                codes={[codes[0], codes[1]]}
              />
            </div>
          </div>
          <div>
            {isModalOpened && (
              <Modal
                onClose={() => {
                  setIsModalOpened(false)
                  setDetailProblems({ modalStatus: false, problemId: 0 })
                }}
              >
                {detailProblems.modalStatus === true ? (
                  <div>
                    <SubmissionList
                      problemId={detailProblems.problemId}
                      setIsModalOpened={setIsModalOpened}
                      setDetailProblems={setDetailProblems}
                    />
                  </div>
                ) : (
                  <div>
                    <div className={cStyle.title}>가져올 코드를 선택하세요</div>
                    <div className={noteStyle.miniNote}>
                      {myProblems.map((it) => (
                        <div
                          role="button"
                          tabIndex={0}
                          key={it.problem.id}
                          className={noteStyle.note}
                          onClick={() =>
                            setDetailProblems({
                              modalStatus: true,
                              problemId: it.problem.id,
                            })
                          }
                          onKeyDown={(e) =>
                            handleDetailProblems(e, it.problem.id)
                          }
                        >
                          <div className={noteStyle.content}>
                            <div className={noteStyle.tierImage}>
                              <TierImg tier={it.problem.tier} />
                            </div>
                            <div className={noteStyle.note_title}>
                              {it.problem.title}
                            </div>
                          </div>

                          <div className={style.tags}>
                            {it.problem.tags.map((tag, tagIdx) => (
                              <span
                                // eslint-disable-next-line react/no-array-index-key
                                key={tagIdx}
                                style={{ marginRight: '10px' }}
                              >
                                #{tag}
                              </span>
                            ))}
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </Modal>
            )}
          </div>
        </div>
      </div>
    </>
  )
}

export default ComparePage
