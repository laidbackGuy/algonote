from pydantic import BaseModel
from typing import List
from .RecommendResultDto import RecommendResultDto

class RecommendReturnDto(BaseModel):
    tag: str
    recommendResultDto : RecommendResultDto