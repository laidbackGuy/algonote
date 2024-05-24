import { useEffect, useState } from 'react'
import Image from 'next/image'
import Link from 'next/link'
import { v4 as uuidv4 } from 'uuid'
import s from './Category.module.scss'
import recommendApi from '@/apis/recommendAxios'
import MetaHead from '@/components/commons/MetaHead'
import QuestionListRec from '@/components/commons/QuestionListRec'
import QuestionListRecTitle from '@/components/commons/QuestionListRecTitle'
import { getCookie } from '@/utils/cookie'

interface CategoryProps {
  categoryName: string
}

interface Problem {
  id: number
  tier: number
  title: string
  accepted_user_count: number
  average_tries: number
}

const dictionary: { [key: string]: string } = {
  math_theory: '수학 및 이론',
  graph_theory: '그래프',
  data_structure: '자료구조',
  optimization: '전략 및 최적화',
  implementation: '구현',
  string: '문자열',
  math: '수학',
  // arithmetic: '사칙연산',
  primality_test: '소수 판정',
  sieve: '에라토스테네스의 체',
  graphs: '그래프 이론',
  graph_traversal: '그래프 탐색',
  bfs: '너비 우선 탐색',
  dfs: '깊이 우선 탐색',
  shortest_path: '최단 경로',
  mst: '최소 스패닝 트리',
  topological_sorting: '위상 정렬',
  dijkstra: '다익스트라',
  floyd_warshall: '플로이드-워셜',
  tsp: '외판원 순회 문제',
  lca: '최소 공통 조상',
  data_structures: '자료구조',
  segtree: '세그먼트 트리',
  // hash_set: '해시를 사용한 집합과 맵',
  prefix_sum: '누적 합',
  stack: '스택',
  queue: '큐',
  hashing: '해싱',
  priority_queue: '우선순위 큐',
  deque: '덱',
  tree_set: '트리를 사용한 집합과 맵',
  dp: '다이나믹 프로그래밍',
  greedy: '그리디 알고리즘',
  divide_and_conquer: '분할 정복',
  binary_search: '이분 탐색',
  parametric_search: '매개 변수 탐색',
  knapsack: '배낭 문제',
  // lis: '가장 긴 증가하는 부분수열',
  recursion: '재귀',
  two_pointer: '두 포인터',
  simulation: '시뮬레이션',
  sorting: '정렬',
  bruteforcing: '브루트포스 알고리즘',
  backtracking: '백트래킹',
  bitmask: '비트마스킹',
  sliding_window: '슬라이딩 윈도우',
  manacher: '매내처 알고리즘',
  rabin_karp: '라빈-카프',
  kmp: 'KMP',
  trie: '트라이',
}

const category: { [key: string]: Array<string> } = {
  math_theory: ['math', 'primality_test', 'sieve'],
  graph_theory: [
    'graphs',
    'graph_traversal',
    'bfs',
    'dfs',
    'shortest_path',
    'mst',
    'topological_sorting',
    'dijkstra',
    'floyd_warshall',
    'tsp',
    'lca',
  ],
  data_structure: [
    'data_structures',
    'segtree',
    // 'hash_set',
    'prefix_sum',
    'stack',
    'queue',
    'hashing',
    'priority_queue',
    'deque',
    'tree_set',
  ],
  optimization: [
    'dp',
    'greedy',
    'divide_and_conquer',
    'binary_search',
    'parametric_search',
    'knapsack',
    // 'lis',
    'recursion',
    'two_pointer',
  ],
  implementation: [
    'implementation',
    'simulation',
    'sorting',
    'bruteforcing',
    'backtracking',
    'bitmask',
    'sliding_window',
  ],
  string: ['manacher', 'rabin_karp', 'kmp', 'string', 'trie'],
}

const Category = ({ categoryName }: CategoryProps) => {
  const [tags, setTags] = useState<Array<Array<Problem>>>([])
  const [loading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    const memberId = getCookie('memberId')

    const fetchData = () => {
      const getTags = async () => {
        const updatedProblems: Array<Array<Problem>> = []

        // 비동기적으로 각 태그에 대한 recommendApi 호출 및 결과 수집
        await Promise.all(
          category[categoryName].map(async (tag) => {
            const tagResponse = await recommendApi(tag, 0, 3, memberId)
            updatedProblems.push(tagResponse) // 결과를 배열에 추가
            // console.log('', tagResponse)
          }),
        )

        // 모든 결과를 한 번에 업데이트
        setTags([...updatedProblems])
        setLoading(false)
        // console.log([...updatedProblems])
      }
      getTags()
    }
    fetchData()
  }, [])

  return (
    <>
      <MetaHead
        title="내가 푼 문제를 바탕으로 유형별 추천 문제를 알려드려요"
        description="BPR을 사용하여 개인 맞춤 추천 문제를 선별해줘요"
      />
      <div
        className={
          categoryName === 'math_theory' ? s.firstCategoryCont : s.categoryCont
        }
      >
        {categoryName !== 'math_theory' ? (
          <hr className={s.divide} id={categoryName} />
        ) : null}
        <p className={s.categoryTitle}>{dictionary[categoryName]}</p>
        {loading ? (
          <div className={s.loadingCont}>
            <p>적합한 추천 문제를 선별 중이에요...</p>
            <div className={s.spinnerCont}>
              <div className={s.spinner} />
            </div>
          </div>
        ) : (
          <div>
            {tags.map((tag, index) => (
              <div className={s.tagCont} key={uuidv4()}>
                <div className={s.tagTitleCont}>
                  <Image
                    src="/images/record/folder.png"
                    width={24}
                    height={18}
                    alt="folder"
                  />
                  <p className={s.tagTitle}>
                    {dictionary[category[categoryName][index]]}
                  </p>
                </div>
                <QuestionListRecTitle />
                {tag.map((problem: Problem) => (
                  <Link
                    href={`https://www.acmicpc.net/problem/${problem.id}`}
                    key={problem.id}
                    style={{ textDecoration: 'none', color: 'black' }}
                    target="_blank"
                  >
                    <div className={s.problem}>
                      <QuestionListRec
                        id={problem.id}
                        title={problem.title}
                        tier={problem.tier}
                        acceptedUserCount={problem.accepted_user_count}
                        averageTries={problem.average_tries}
                      />
                    </div>
                  </Link>
                ))}
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  )
}

export { dictionary, category }
export default Category
