import numpy as np
from numpy.polynomial import Polynomial

# Your real-world calibration testing data
x_data = np.array([0.0, 1.0, 2.0, 3.0, 4.0, 5.0])
y_data = np.array([1.0, 2.1, 3.9, 9.2, 15.8, 25.4])

# Fit a 2nd-degree polynomial
model = Polynomial.fit(x_data, y_data, deg=2)

# IMPORTANT: Convert back to normal coefficients [c0, c1, c2]
coefficients = model.convert().coef
print(f"c0 (Constant): {coefficients[0]}")
print(f"c1 (Linear):   {coefficients[1]}")
print(f"c2 (Quadratic):{coefficients[2]}")