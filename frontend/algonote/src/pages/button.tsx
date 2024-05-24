'use client'

import { useState } from 'react'
import BookMarkSVG from '@public/images/bookmark.svg'
import BookMarkOffSVG from '@public/images/bookmark_off.svg'
import GPT from '@public/images/btn-new-chat.svg'
import HeartOffSVG from '@public/images/heart.svg'
import HeartSVG from '@public/images/redHeart.svg'
import { SimpleButton, FilterButton } from '@/components/commons/Buttons/Button'
import ImageToggle from '@/components/commons/Buttons/ImageToggle'
import ToggleButton from '@/components/commons/Buttons/ToggleButton'

const ButtonFunction = () => {
  const [count, setCount] = useState(0)
  const [isClicked, setIsClicked] = useState(false)
  const [markIsOff, setMarkIsOff] = useState(false)
  const [heartIsOff, setHeartIsOff] = useState(false)

  return (
    <div style={{ marginTop: '60px' }}>
      <SimpleButton text="기본버튼" onClick={() => setCount(count + 1)} />

      <SimpleButton
        text="돌아가기"
        onClick={() => setCount(count + 1)}
        className="back"
        style={{ width: '100px', height: '100px', fontSize: '1rem' }}
      />
      <SimpleButton
        text="추천검색어"
        onClick={() => setCount(count + 1)}
        className="recommend"
        style={{ fontSize: '1rem' }}
      />
      <FilterButton
        text="검색필터"
        onClick={() => setIsClicked(!isClicked)}
        className="search"
        active
      />
      <ImageToggle
        isOff={markIsOff}
        onClick={() => setMarkIsOff(!markIsOff)}
        offImg={BookMarkSVG}
        onImg={BookMarkOffSVG}
      />
      <ImageToggle
        isOff={heartIsOff}
        onClick={() => setHeartIsOff(!heartIsOff)}
        offImg={HeartOffSVG}
        onImg={HeartSVG}
      />
      <ToggleButton imageSrc={GPT} onClick={() => setCount(count + 1)} />
    </div>
  )
}

export default ButtonFunction
