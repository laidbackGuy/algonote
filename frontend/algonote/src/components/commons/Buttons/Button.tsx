'use client'

import { useState } from 'react'
import styles from '@/components/commons/Buttons/Button.module.scss'

interface SimpleButtonProps {
  text: string
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => void
  className?: string
}

const SimpleButton = ({ text, onClick, className }: SimpleButtonProps) => {
  const buttonClass = `${styles.button} ${className ? styles[className] : ' '} `

  return (
    <button type="button" onClick={onClick} className={buttonClass}>
      {text}
    </button>
  )
}

SimpleButton.defaultProps = {
  className: '',
}

const FilterButton = ({ text, onClick, className }: SimpleButtonProps) => {
  const [isClicked, setIsClicked] = useState(false)

  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    setIsClicked(!isClicked)
    onClick(e)
  }

  return (
    <SimpleButton text={text} onClick={handleClick} className={className} />
  )
}

export { SimpleButton, FilterButton }
