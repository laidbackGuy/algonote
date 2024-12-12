from glob import glob
import os
import csv
import pandas as pd
import json

def get_user_dict():
    result = {}
    with open("./user_list.txt", "r") as f:
            for line in f:
                columns = line.strip().split()

                key = columns[0]
                value = columns[1]

                result[key] = value
    return result


def get_data(tag):
    path = f"./divide/data_{tag}.csv"
    df = pd.read_csv(path)
    data = json.loads(df.to_json(orient='records'))
    return data

def get_df(tag):
    path = f"./divide/data_{tag}.csv"
    df = pd.read_csv(path)
    df = df.rename(columns={"memberId" : "userID", "problemId":"itemID"})
    # print(df)
    df["rating"] = 5
    return df

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from dotenv import load_dotenv
import os

# 데이터베이스 연결 설정
def get_session():
    load_dotenv()
    username = os.getenv('DB_USERNAME')
    password = os.getenv('DB_PASSWORD')
    host = os.getenv('DB_HOST')
    print(f"username: {username}, password: {password}, host: {host}")
    engine = create_engine(f'mysql+pymysql://{username}:{password}@{host}:3306/algonote')
    Session = sessionmaker(bind=engine)
    session = Session()
    return session