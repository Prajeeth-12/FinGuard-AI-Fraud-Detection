import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
import joblib  

#load dataset

df=pd.read_csv('fraud_transactions.csv')

#features and target
X=df.drop('fraud',axis=1)
y=df['fraud']

#Train-test split

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

#Train the model

model = RandomForestClassifier(n_estimators=100, random_state=42)   
model.fit(X_train, y_train)

#save the model

joblib.dump(model, 'fraud_model.pkl')
print("Model trained and saved as fraud_model.pkl")