import { axiosAuthApi } from '@/utils/instance'

interface ComplexityProps {
  sourceCode: string
}

interface ExecuteDataProps extends ComplexityProps {
  language: string
  inputData: string
  expectedOutput: string
}

const executeCode = async (props: ExecuteDataProps) => {
  const { language, sourceCode, inputData, expectedOutput } = props
  const apiUrl = `/api/codes/analyze?lang=${language}`
  const data = { sourceCode, inputData, expectedOutput }

  try {
    const response = await axiosAuthApi().post(apiUrl, data)
    console.log('실행 결과 응답', response.data)
    return response.data
  } catch (error) {
    console.error('executeCode 호출 중 오류 발생:', error)
    throw error
  }
}

const getComplexityCode = async (sourceCode: ComplexityProps) => {
  const apiUrl = '/api/codes/complexity'

  try {
    const response = await axiosAuthApi().post(apiUrl, sourceCode)
    return response.data
  } catch (error) {
    console.error('getComplexityCode 호출 중 오류 발생:', error)
    throw error
  }
}

export { executeCode, getComplexityCode }
