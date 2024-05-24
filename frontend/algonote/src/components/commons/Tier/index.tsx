// it.problem.tier
import Image from 'next/image'

interface TierImgProps {
  tier: number
}

const TierImg = ({ tier }: TierImgProps) => {
  let folder
  let file

  switch (true) {
    case tier <= 5 && tier > 0:
      folder = 'bronze'
      file = 6 - tier
      break
    case tier <= 10:
      folder = 'silver'
      file = 11 - tier
      break
    case tier <= 15:
      folder = 'gold'
      file = 16 - tier
      break
    case tier <= 20:
      folder = 'platinum'
      file = 21 - tier
      break
    case tier <= 25:
      folder = 'dia'
      file = 26 - tier
      break
    case tier <= 30:
      folder = 'ruby'
      file = 31 - tier
      break
    default:
      folder = 'unrated'
  }

  return (
    <div>
      <Image
        src={`/images/tier/${folder}/${folder}${file}.png`}
        alt={`문제레벨${folder}${file}`}
        width={20}
        height={15.35}
      />
    </div>
  )
}

export default TierImg
