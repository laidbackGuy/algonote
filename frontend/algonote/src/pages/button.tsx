'use client'

import { useState } from 'react'
import GPT from '@public/images/btn-new-chat.svg'
import BookmarkToggle from '@/components/commons/Buttons/BookmarkToggle'
import { SimpleButton, FilterButton } from '@/components/commons/Buttons/Button'
import ToggleButton from '@/components/commons/Buttons/ToggleButton'

const ButtonFunction = () => {
  const [count, setCount] = useState(0)
  const [isClicked, setIsClicked] = useState(false)
  const [isOff, setIsOff] = useState(false)

  return (
    <div>
      <SimpleButton text="기본버튼" onClick={() => setCount(count + 1)} />
      <SimpleButton
        text="돌아가기"
        onClick={() => setCount(count + 1)}
        className="back"
      />
      <SimpleButton
        text="추천검색어"
        onClick={() => setCount(count + 1)}
        className="recommend"
      />
      <FilterButton
        text="검색필터"
        onClick={() => setIsClicked(!isClicked)}
        className="search"
      />
      <BookmarkToggle isOff={isOff} onClick={() => setIsOff(!isOff)} />
      <ToggleButton imageSrc={GPT} onClick={() => setCount(count + 1)} />
    </div>
  )
}

export default ButtonFunction
