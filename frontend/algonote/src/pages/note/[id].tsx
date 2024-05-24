import { ChangeEvent, useEffect, useState } from 'react'
import BookMarkSVG from '@public/images/bookmark.svg'
import BookMarkOffSVG from '@public/images/bookmark_off.svg'
import HeartOffSVG from '@public/images/heart.svg'
import HeartSVG from '@public/images/redHeart.svg'
import Image from 'next/image'
import { useRouter } from 'next/router'
import { bookmarkButtonApi } from '@/apis/bookmarkAxios'
import likeApi from '@/apis/likeAxios'
import { deleteNote, getNoteDetail } from '@/apis/note-detailAxios'
import {
  createReviewApi,
  deleteReviewApi,
  readReviewApi,
  updateReviewApi,
} from '@/apis/reviewAxios'
import myInfo from '@/apis/user-infoAxios'
import { SimpleButton } from '@/components/commons/Buttons/Button'
import ImageToggle from '@/components/commons/Buttons/ImageToggle'
import EditorInNoteDetail from '@/components/commons/Editor/detail'
import { dictionary } from '@/components/commons/QuestionList'
import TierImg from '@/components/commons/Tier'
import style from '@/pages/note/note.module.scss'
// import useNoteStore from '@/stores/note-store'

interface Member {
  memberId: number
  nickname: string
  profileImg: string
}

interface Problem {
  id: number
  title: string
  tier: number
  acceptUserCount: number
  averageTries: number
  tags: string[]
}

interface NoteData {
  noteId: number
  member: Member
  problem: Problem
  noteTitle: string
  content: string
  heartCnt: number
  hearted: boolean
  createdAt: string
  modifiedAt: string
}

// Review에 대한 정보를 나타내는 인터페이스
interface Review {
  reviewId: number
  member: Member
  startLine: number
  endLine: number
  content: string
  createdAt: string // ISO 형식의 날짜 문자열
  modifiedAt: string // ISO 형식의 날짜 문자열
}

// Review[] 형태의 배열을 나타내는 인터페이스
interface ReviewProps extends Array<Review> {}

interface UserInfo {
  memberId: number
  email: string
  nickname: string
  profileImg: string
}

const Note = () => {
  const router = useRouter()
  const { id } = router.query
  const [noteDetail, setNoteDetail] = useState<NoteData>()
  const [reviews, setReviews] = useState<ReviewProps>()
  const [comment, setComment] = useState<string>('')
  const [newComment, setNewComment] = useState<string>('')
  const [markIsOff, setMarkIsOff] = useState(false)
  const [likeIsOff, setLikeIsOff] = useState(false)
  const [userDetails, setUserDetails] = useState<UserInfo | null>(null)
  const [updateId, setUpdateId] = useState<number | null>(null)
  const [updating, setUpdating] = useState(false)
  // const { setSelectedNoteData } = useNoteStore()

  const handleBookmark = async () => {
    const response = await bookmarkButtonApi(id as string)
    setMarkIsOff(response.data.bookmarked)
  }

  const handleLike = async () => {
    const response = await likeApi(id as string)
    if (response.status === 200) {
      setLikeIsOff(response.data.hearted)

      // 좋아요
      if (response.data.hearted) {
        setNoteDetail((prevDetail) => {
          if (prevDetail) {
            return {
              ...prevDetail,
              heartCnt: prevDetail.heartCnt + 1,
            }
          }
          return prevDetail
        })
      } else {
        // 좋아요 취소
        setNoteDetail((prevDetail) => {
          if (prevDetail) {
            return {
              ...prevDetail,
              heartCnt: prevDetail.heartCnt - 1,
            }
          }
          return prevDetail
        })
      }
    }
  }

  useEffect(() => {
    const fetchData = async () => {
      if (id !== undefined) {
        try {
          console.log('노트 상세보기 요청')
          const noteResponse = await getNoteDetail(id as string)
          console.log('노트 상세보기 응답:', noteResponse.data)
          setNoteDetail(noteResponse.data)
          setMarkIsOff(noteResponse.data.bookmarked)
          setLikeIsOff(noteResponse.data.hearted)

          console.log('리뷰 조회 요청')
          const reviewResponse = await readReviewApi(id as string)
          console.log('리뷰 조회 성공:', reviewResponse)
          setReviews(reviewResponse)
        } catch (error) {
          console.error('데이터 가져오기 실패:', error)
        }
      }
    }
    const fetchMyInfo = async () => {
      try {
        const response = await myInfo()
        // API 응답을 상태에 저장하기 전에 형식이 맞는지 확인
        if (response && typeof response === 'object') {
          setUserDetails(response.data)
        }
      } catch (error) {
        console.error('내 정보 가져오기 실패:', error)
      }
    }
    fetchData()
    fetchMyInfo()
  }, [id])

  const fetchReviews = async () => {
    try {
      console.log('댓글 다시 조회 요청')
      const reviewResponse = await readReviewApi(id as string)
      console.log('댓글 다시 조회 성공:', reviewResponse)
      setReviews(reviewResponse)
    } catch (error) {
      console.error('댓글 다시 조회 실패:', error)
    }
  }

  const deleteReview = async (reviewId: number) => {
    try {
      console.log('리뷰삭제 요청')
      await deleteReviewApi(id as string, reviewId) // 리뷰조회 API 호출
      console.log('리뷰삭제 성공!')
      // 리뷰삭제 성공 후 필요한 작업 수행 (예: 페이지 이동 등)
      fetchReviews()
    } catch (e) {
      console.error('리뷰조회 실패:', e)

      // 리뷰삭제 실패 처리
    }
  }

  const createReview = async (
    startLine: number,
    endLine: number,
    content: string,
  ) => {
    try {
      console.log('리뷰생성 요청')
      await createReviewApi(id as string, startLine, endLine, content) // 리뷰조회 API 호출
      console.log('리뷰생성 성공!')
      // 리뷰생성 성공 후 필요한 작업 수행
      fetchReviews()
      setComment('')
    } catch (e) {
      console.error('리뷰생성 실패:', e)

      // 리뷰생성 실패 처리
    }
  }

  const updateReview = async (reviewId: number, content: string) => {
    try {
      console.log('리뷰수정 요청')
      await updateReviewApi(id as string, reviewId, content) // 리뷰조회 API 호출
      console.log('리뷰수정 성공!')
      // 리뷰수정 성공 후 필요한 작업 수행
      fetchReviews()
      setComment('')
      setUpdating(false)
      setNewComment('')
      setUpdateId(null)
    } catch (e) {
      console.error('리뷰수정 실패:', e)
      // 리뷰수정 실패 처리
    }
  }

  const updateCall = (newValue: number) => {
    setUpdateId(newValue)
    setUpdating(true)
  }

  const handleComment = (event: ChangeEvent<HTMLInputElement>) => {
    const newValue = event.target.value
    setComment(newValue)
  }

  const handleNewComment = (event: ChangeEvent<HTMLInputElement>) => {
    const newValue = event.target.value
    setNewComment(newValue)
  }

  // // 노트 수정
  // const handleReviseNote = () => {
  //   setSelectedNoteData(noteDetail || null)
  //   router.push('/revisenote')
  // }

  // 노트 삭제
  const handleDeleteNote = () => {
    deleteNote(Number(noteDetail?.noteId))
    router.push(`/mynote`)
  }

  const dateString = noteDetail?.createdAt ?? ''
  const date = new Date(dateString)

  const year = date.getFullYear()
  const month = String(date.getMonth() + 1)
  const day = String(date.getDate())

  const formattedDate = `${year}년 ${month}월 ${day}일`

  return (
    <div className={style.frame}>
      <div>
        <div className={style.titleSection}>
          <div className={style.tierImg}>
            <TierImg tier={Number(noteDetail?.problem.tier)} />
          </div>
          <span className={style.boj}>백준 {noteDetail?.problem.id}</span>
          <span className={style.probTitle}>{noteDetail?.problem.title}</span>
          {noteDetail?.problem.tags.map((tag) => (
            <div key={tag}>
              <div className={style.tagBox}>{dictionary[tag]}</div>
            </div>
          ))}
        </div>
        {/* <div className={style.tries}>
          <span> 평균 {noteDetail?.problem.averageTries}회 시도</span>
          <span>| 맞은 사람 {noteDetail?.problem.acceptUserCount}명</span>
        </div> */}
        <hr className={style.upperHr} />
      </div>

      <div className={style.noteWrapper}>
        <div className={style.noteTitleSection}>
          {noteDetail?.noteTitle}
          <div className={style.writerWrapper}>
            <div className={style.writerSection}>
              <div className={style.noteWriter}>
                {noteDetail?.member.nickname}{' '}
              </div>
              <div className={style.noteCreatedDay}> · {formattedDate}</div>
            </div>
            <div className={style.buttonSection}>
              {/* <SimpleButton
                text="수정"
                onClick={handleReviseNote}
                style={{
                  width: '3.5rem',
                  height: '2.2rem',
                  fontFamily: 'Pretendard',
                  background: '#ffffff',
                  color: '#b9b9b9',
                  border: 'none',
                }}
              /> */}
              <SimpleButton
                text="삭제"
                onClick={handleDeleteNote}
                style={{
                  width: '3.5rem',
                  height: '2.2rem',
                  background: '#ffffff',
                  color: '#b9b9b9',
                  border: 'none',
                  fontFamily: 'Pretendard',
                  marginLeft: '0.4rem',
                }}
              />
            </div>
          </div>
        </div>

        <div className={style.noteContent}>
          <EditorInNoteDetail content={noteDetail?.content ?? ''} />
        </div>

        <div className={style.likeButtonSection}>
          <div className={style.likeButton}>
            <ImageToggle
              isOff={likeIsOff}
              onClick={() => handleLike()}
              offImg={HeartSVG}
              onImg={HeartOffSVG}
              width="17px"
              height="17px"
            />
            <span>{noteDetail?.heartCnt}</span>
          </div>
          <div className={style.bookmarkButton}>
            <ImageToggle
              isOff={markIsOff}
              onClick={() => handleBookmark()}
              offImg={BookMarkSVG}
              onImg={BookMarkOffSVG}
              width="12.5px"
              height="30px"
            />
          </div>
        </div>

        <div className={style.reviewsContainer}>
          <div className={style.reviewsConTainerTitle}>
            <span>리뷰 </span>
            <span>{reviews?.length} </span>
          </div>
          {reviews?.map((review) => (
            <div className={style.reviewContainer} key={review.reviewId}>
              <div className={style.reviewProfileImg}>
                <Image
                  src={review.member.profileImg} // 이미지 경로
                  alt="profile-image"
                  width={60}
                  height={80}
                />
              </div>
              <div className={style.reviewContentSection}>
                <div className={style.reviewNickname}>
                  <p>{review.member.nickname}</p>
                </div>
                <div className={style.reviewContent}>
                  {review.reviewId === updateId ? (
                    <input
                      type="newComment"
                      placeholder="내용을 입력하세요"
                      value={newComment}
                      onChange={handleNewComment}
                      className={style.reviewInput}
                    />
                  ) : (
                    <p>{review.content}</p>
                  )}
                </div>
              </div>

              {review.member.memberId === userDetails?.memberId && (
                <div>
                  {updating && review.reviewId === updateId ? (
                    <SimpleButton
                      text="확인"
                      style={{
                        marginTop: '9rem',
                        width: '4rem',
                        height: '2rem',
                        fontFamily: 'Pretendard',
                        fontSize: '0.9rem',
                        border: 'none',
                        borderRadius: '2rem',
                      }}
                      onClick={() => updateReview(review.reviewId, newComment)}
                    />
                  ) : (
                    <div className={style.buttonSection}>
                      <button
                        className={style.reviewBtn}
                        type="button"
                        onClick={() => updateCall(review.reviewId)}
                      >
                        수정
                      </button>
                      <button
                        className={style.reviewBtn}
                        type="button"
                        onClick={() => deleteReview(review.reviewId)}
                      >
                        삭제
                      </button>
                    </div>
                  )}
                </div>
              )}
            </div>
          ))}

          <div className={style.reviewsBottom}>
            <div className={style.reviewProfileSection}>
              <div className={style.reviewProfileImg}>
                <Image
                  src={userDetails?.profileImg ?? ''} // 이미지 경로
                  alt="profile-image"
                  width={60}
                  height={80}
                />
              </div>
              <input
                type="comment"
                placeholder="내용을 입력하세요"
                value={comment}
                onChange={handleComment}
                className={style.reviewInput}
              />
            </div>
            <div className={style.registNote}>
              <SimpleButton
                text="등록"
                onClick={() => createReview(1, 3, comment)}
                style={{
                  width: '4rem',
                  height: '2rem',
                  fontFamily: 'Pretendard',
                  fontSize: '0.9rem',
                  border: 'none',
                  borderRadius: '2rem',
                }}
              />
            </div>
          </div>
        </div>
        <div />
      </div>
    </div>
  )
}

export default Note
