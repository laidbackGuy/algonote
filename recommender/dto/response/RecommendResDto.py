from pydantic import BaseModel
from typing import List

class RecommendResDto(BaseModel):
    count : int
    recommendedProblemIds : List[int]