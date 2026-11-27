import numpy as np
import pandas as pd
import time
from pathlib import Path

start = time.perf_counter()
data_frame = pd.read_csv(Path(__file__).parent / "data" / "data.csv")

x = data_frame.iloc[:,2] # column 3

for i in (0,1): #Columns 1 and 2
    y = data_frame.iloc[:,i]
    slope, intercept = np.polyfit(x,y,1)
    r_squared = np.corrcoef(x,y)[0,1] ** 2
    print(data_frame.columns[i], f"Slope: {slope}", f"Intercept: {intercept}", f"R^2 {r_squared}")

end = time.perf_counter()
print(f"Elapsed time: {end - start:.4f} seconds")