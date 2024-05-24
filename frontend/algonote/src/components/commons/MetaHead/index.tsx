import Head from 'next/head'

export interface MetaType {
  title?: string
  description?: string
  image?: string
}

const MetaHead = ({
  title = 'Algonote',
  description = '균형잡힌 알고리즘 공부 알고노트로 시작하세요',
  image = '/images/loginLogo.png',
}: MetaType) => {
  return (
    <Head>
      <title>{title}</title>
      <meta name="description" content={description} />
      <meta property="og:title" content={title} />
      <meta property="og:description" content={description} />
      <meta property="og:image" content={image} />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <meta
        name="google-site-verification"
        content="orGf2AsBPWGGpcuzv3_LwDYQl-GDxrDxrYWEO5KPFu4"
      />
      <link rel="icon" href="/favicon.ico" />
    </Head>
  )
}

export default MetaHead
