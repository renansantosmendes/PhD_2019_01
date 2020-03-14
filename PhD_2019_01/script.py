import pandas as pd
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

file_name = 'pareto_final_onmoead.txt'

f = open(file_name, 'r',encoding='utf-8')
data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]

data = [(float(i[0]),float(i[0]),float(i[0])) for i in data]
data = pd.DataFrame(data=data).drop_duplicates()

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

x =data[0].tolist()
y =data[1].tolist()
z =data[2].tolist()

ax.scatter(x, y, z, c='r', marker='o')
ax.set_xlabel('X Label')
ax.set_ylabel('Y Label')
ax.set_zlabel('Z Label')

plt.show()