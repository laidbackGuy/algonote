from pydantic import BaseModel
from typing import List

class RecommendReqDto(BaseModel):
    nickname: str
    tag: str
    solvedProblemIds: List[int]