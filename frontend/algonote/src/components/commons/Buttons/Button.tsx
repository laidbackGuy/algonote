'use client'

import React from 'react'
import styles from '@/components/commons/Buttons/Button.module.scss'

export interface SimpleButtonProps {
  text: string
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => void
  className?: string
  style?: React.CSSProperties
  isDisabled?: boolean
}

const SimpleButton = ({
  text,
  onClick,
  className,
  style = {},
  isDisabled,
}: SimpleButtonProps) => {
  const buttonClass =
    `${styles.button} ${className ? styles[className] || className : ' '}`.trim()

  return (
    <button
      type="button"
      onClick={onClick}
      className={buttonClass}
      style={style}
      disabled={isDisabled}
    >
      {text}
    </button>
  )
}

export interface FilterButtonProps extends SimpleButtonProps {
  active: boolean
}

const FilterButton = ({
  text,
  onClick,
  className,
  style,
  active,
}: FilterButtonProps) => {
  const activeStyle = active ? 'active' : ''
  const fullClassName = `${className || ''}${activeStyle}`

  return (
    <SimpleButton
      text={text}
      onClick={onClick}
      className={fullClassName}
      style={style}
    />
  )
}

export { SimpleButton, FilterButton }
