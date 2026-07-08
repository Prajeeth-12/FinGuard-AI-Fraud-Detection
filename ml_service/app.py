from flask import Flask,request,jsonify
import pandas as pd
import joblib

app=Flask(__name__)

model=joblib.load("fraud_model.pkl")

@app.route("/predict",methods=["POST"])
def predict():

    data=request.json

    features=pd.DataFrame(
        [[
            data["amount"],
            data["timeGapSeconds"],
            data["cityChanged"],
            data["recentTransactionCount"]
        ]],
        columns=[
            "amount",
            "timeGapSeconds",
            "cityChanged",
            "recentTransactionCount"
        ]
    )

    score=model.predict_proba(features)[0][1]

    return jsonify({
        "fraud_score":float(score)
    })

if __name__=="__main__":
    app.run(debug=True)