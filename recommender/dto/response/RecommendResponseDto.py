from pydantic import BaseModel
from typing import List

class RecommendResponseDto(BaseModel):
    id : int
    tier : int
    title : str
    accepted_user_count : int
    average_tries : float