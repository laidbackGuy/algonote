import { axiosAuthApi } from '@/utils/instance'

const getSearchResult = async (keyword: string, page: number) => {
  const apiUrl = `/api/notes/full-text?keyword=${keyword}&page=${page}`

  try {
    const response = await axiosAuthApi().get(apiUrl)
    return response.data
  } catch (error) {
    throw error
  }
}

export default getSearchResult
