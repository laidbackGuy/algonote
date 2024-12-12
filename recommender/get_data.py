from glob import glob
import os
import csv
import pandas as pd
import json


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