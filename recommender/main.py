import uvicorn
from fastapi import FastAPI, UploadFile, Request, status, File, HTTPException, Query
from fastapi.responses import JSONResponse, FileResponse
import os
import uuid
from pathlib import Path
import sys
import platform
import json
from starlette.middleware.cors import CORSMiddleware

from dto.request.RecommendReqDto import RecommendReqDto
from dto.request.RecommendGroupDto import RecommendGroupDto
from dto.response.RecommendResDto import RecommendResDto
from dto.response.RecommendReturnDto import RecommendReturnDto

from dto.response.RecommendResultDto import RecommendResultDto
from repository.ProblemRepository import find_solved_problems, find_by_ids
from util.utils import get_user_dict
from model.model import inference
import pandas as pd


app = FastAPI()

origins = [
    "https://k10b203.p.ssafy.io",
    "http://localhost:8000",
    "https://algnote.duckdns.org",
    "http://0.0.0.0:8000",
    "http://localhost:3000",
    "http://0.0.0.0",
]
user_dict = get_user_dict()
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/python/recommend/test")
async def test():
    return {"message": "Hello World"}

@app.get("/python/recommend")
async def recommend(tag: str = Query(...), page: int=Query(0), memberId: int=Query(), size: int=Query(10)):

    # print(user_dict)
    try:
       print(f"tag : {tag}, page : {page}, size : {size}, memberId : {memberId}")

            
       sovled_problem_ids = find_solved_problems(memberId, tag)

       validation = []
       for problem_id in sovled_problem_ids:
            validation.append([0,problem_id,5])
        
       validation = pd.DataFrame(validation, columns=["userID", "itemID", "rating"])
       
       recommend_problem_ids = inference(validation, tag)

       recommend_problems = find_by_ids(recommend_problem_ids, page, size)

       return recommend_problems
       

        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    

@app.post("/python/recommend/group")
async def recommend(recommendGroupDto : RecommendGroupDto):

    # print(user_dict)
    try:

        group = recommendGroupDto.group
        tagDtos = recommendGroupDto.tags


        response = []
        for tagDto in tagDtos:
            tag = tagDto.tag
            # print("tag", tag)
            solvedProblemIds = tagDto.solvedProblemIds

            validation = []
            for problemId in solvedProblemIds:
                validation.append([0, problemId, 5])
            validation = pd.DataFrame(validation, columns=["userID", "itemID", "rating"])
            result = inference(validation, tag)
            recommendResultDto = RecommendResultDto(count=len(result), 
                                        recommendedProblemIds=result)
            recommendReturnDto = RecommendReturnDto(tag=tag, recommendResultDto=recommendResultDto)
            # response.append(recommendReturnDto)
        print("response: ", response)
        return response

        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/python/recommend")
async def recommend(recommendDto : RecommendReqDto):

    # print(user_dict)
    try:
        nickname = recommendDto.nickname
        tag = recommendDto.tag
        solvedProblemIds = recommendDto.solvedProblemIds
        print(f"nickname : {nickname}, tag : {tag}, solvedProblemIds : {solvedProblemIds}")
        validation = []
        for problemId in solvedProblemIds:
            validation.append([0, problemId, 5])
        validation = pd.DataFrame(validation, columns=["userID", "itemID", "rating"]) 

        result = inference(validation, tag)
        # print("result: ", result)
        recommendResDto = RecommendResDto(count=len(result), 
                                          recommendedProblemIds=result)
        # print("response: ", recommendResultDto)
        return  recommendResDto
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
    # uvicorn.run("main:app", host="localhost", port=8000, reload=True)