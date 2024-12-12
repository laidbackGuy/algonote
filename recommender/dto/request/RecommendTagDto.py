from pydantic import BaseModel
from typing import List

class RecommendTagDto(BaseModel):
    tag: str
    solvedProblemIds: List[int]