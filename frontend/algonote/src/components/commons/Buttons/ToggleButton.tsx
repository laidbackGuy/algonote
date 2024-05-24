import Image, { StaticImageData } from 'next/image'
import styles from './ToggleButton.module.scss'

export interface ToggleButtonProps {
  imageSrc: StaticImageData
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => void
  width?: string
  height?: string
}

const ToggleButton = ({
  imageSrc,
  onClick,
  width,
  height,
}: ToggleButtonProps) => {
  const buttonStyle = {
    width: width || '100%',
    height: height || '3rem',
  }

  return (
    <button
      type="button"
      onClick={onClick}
      style={buttonStyle}
      className={styles.button}
    >
      <Image src={imageSrc} alt="토글버튼" layout="fill" objectFit="contain" />
    </button>
  )
}

export default ToggleButton
