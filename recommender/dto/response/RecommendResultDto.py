from pydantic import BaseModel
from typing import List

class RecommendResultDto(BaseModel):
    count : int
    recommendedProblemIds : List[int]