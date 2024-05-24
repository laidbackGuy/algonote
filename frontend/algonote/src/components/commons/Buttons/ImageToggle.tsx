import { StaticImageData } from 'next/image'
import ToggleButton from './ToggleButton'

export interface BookmarkProps {
  isOff: boolean
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => void
  offImg: StaticImageData
  onImg: StaticImageData
  width?: string
  height?: string
}

const ImageToggle = ({
  isOff,
  onClick,
  offImg,
  onImg,
  width,
  height,
}: BookmarkProps) => {
  const imageSrc = isOff ? offImg : onImg
  return (
    <ToggleButton
      imageSrc={imageSrc}
      onClick={onClick}
      width={width}
      height={height}
    />
  )
}

export default ImageToggle
