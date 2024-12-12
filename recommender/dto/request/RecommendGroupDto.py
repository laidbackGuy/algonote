from pydantic import BaseModel
from typing import List
from .RecommendTagDto import RecommendTagDto

class RecommendGroupDto(BaseModel):
    group: str
    tags: List[RecommendTagDto]
    