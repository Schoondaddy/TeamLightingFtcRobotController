import pandas as pd
from scipy.stats import linregress

data_frame = pd.read_csv("data/data.csv")

x = df.iloc[:,2] # column 3

for i in (0,1): #Columns 1 and 2
    y = data_frame.iloc[:,i]
    regression = linregress(x,y)
    print(data_frame.columns[i], f"Slope: {regression.slope}", f"Intercept: {regression.intercept}", f"R^2 {regression.rvalue**2}")