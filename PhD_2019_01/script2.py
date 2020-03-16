import os
import pandas as pd
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

file_name = 'onmoead-initial_population_reduced.csv'
file_path = '/home/renansantos/Área de Trabalho/Doutorado/PhD_2019_01/PhD_2019_01/Results_2020/ONMOEAD/r050n12tw10k11_2020_3_16/'

f = open(os.path.join(file_path, file_name), 'r',encoding='utf-8')
data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]
data = [(float(i[0]),float(i[1]),float(i[2])) for i in data]
data = pd.DataFrame(data=data).drop_duplicates()

print(data)
print(data.info())
print(data.describe())

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

x =data[0].tolist()
y =data[1].tolist()
z =data[2].tolist()

ax.scatter(x, y, z, c='r', marker='o', label='Initial Population')

file_name = 'onmoead-combined_pareto_reduced.csv'

f = open(os.path.join(file_path, file_name), 'r',encoding='utf-8')
data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]
data = [(float(i[0]),float(i[1]),float(i[2])) for i in data]
data = pd.DataFrame(data=data).drop_duplicates()

print(data)
print(data.info())
print(data.describe())

# data = data[data[2] < 4000]

x =data[0].tolist()
y =data[1].tolist()
z =data[2].tolist()

ax.scatter(x, y, z, c='b', marker='v', label='Combined Pareto')
ax.set_xlabel('X Label')
ax.set_ylabel('Y Label')
ax.set_zlabel('Z Label')

plt.legend()
plt.show()