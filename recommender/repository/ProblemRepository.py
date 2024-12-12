from util.utils import get_session
from sqlalchemy import select, func
from sqlalchemy.sql import text
from entity.problem import Problem
from entity.solved_problem import SolvedProblem
from dto.response.RecommendResDto import RecommendResDto
from dto.response.RecommendResponseDto import RecommendResponseDto
from sqlalchemy.orm import aliased

def find_by_ids(ids, page, size):
    session = get_session()

    results_query = (
        select(
            Problem
        )
        .where(Problem.id.in_(ids))
        .order_by(Problem.accepted_user_count.desc())
        .limit(size)
        .offset(page* size)
    )

    results = session.execute(results_query).fetchall()

    session.close()


    return [RecommendResponseDto(id=result[0].id, title=result[0].title, tier=result[0].tier, 
                            accepted_user_count=result[0].accepted_user_count, 
                            average_tries=result[0].average_tries) for result in results]

    # return recommend_problem

def find_solved_problems(memberId, tag):
    session = get_session()


    query = text("""
        SELECT sp.problem_id
        FROM solved_problem sp
        JOIN problem p ON sp.problem_id = p.id
        JOIN problem_tag pt ON p.id = pt.problem_id
        JOIN tag t ON pt.tag_id = t.id
        WHERE sp.member_id = :member_id AND t.name_en = :tag
    """)

    result = session.execute(query, {"member_id": memberId, "tag": tag}).fetchall()
    session.close()
    return [row[0] for row in result]