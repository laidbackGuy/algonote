import pickle
import os
from recommenders.models.cornac.cornac_utils import predict_ranking
def inference(validation, tag):
    path = os.path.join(os.getcwd(), 'model', 'models', f"{tag}_bpr.pkl")
    with open(path, "rb") as f:
        model = pickle.load(f)
    
    solved = validation["itemID"].unique()
    print("solved", solved)

    prediction = predict_ranking(model, validation, usercol='userID', itemcol='itemID', remove_seen=True)
    prediction = prediction.sort_values("prediction", ascending=False).drop_duplicates(subset="itemID")
    threshold = prediction["prediction"].quantile(0.95)
    top_k = prediction[prediction["prediction"] > threshold]
    top_k = top_k[~top_k["itemID"].isin(solved)]
    return top_k["itemID"]
