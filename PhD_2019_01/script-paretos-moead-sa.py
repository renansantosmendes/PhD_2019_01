import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

def get_nondominated_vectors(X):
    X = np.array(X)
    p_is_dominated = 0
    q_is_dominated = 0
    X_bool = np.zeros(X.shape[0])
    
    for q in range(X.shape[0]):
        q_ = X[q]
        for p in X:
            if pareto(q_,p):
                X_bool[q] = 1
    return [list(X[i]) for i,j in enumerate(X_bool) if X_bool[i] == 0]

def pareto(p,q):#p domina q? a resposta é um booleano
    y = False
    if sum(p >= q) == len(p):
        if sum(p == q) != len(p):
            y = True
    return y

file_path = '/home/renansantos/Área de Trabalho/Doutorado/PhD_2019_01/PhD_2019_01/Results_2020/MOEAD/MOEAD_R3_SA/'
file_name = 'moead-combined_pareto_reduced.csv'

f = open(os.path.join(file_path, file_name), 'r',encoding='utf-8')
data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]
data = [(float(i[0]), float(i[1]), float(i[2])) for i in data]
data = pd.DataFrame(data=data).drop_duplicates()

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

x =data[0].tolist()
y =data[1].tolist()
z =data[2].tolist()

ax.scatter(x, y, z, c='r', marker='o', label='MOEAD+ag')

file_path = '/home/renansantos/Área de Trabalho/Doutorado/PhD_2019_01/PhD_2019_01/Results_2020/MOEAD/MOEAD_R8/'
file_name = 'moead-combined_pareto_reduced.csv'

lst = [0.1, 10.0, 12.0, 600.0, 50.0, 12.0, 1.0, 6000.0]
lambdas = np.array([float(i) for i in lst])

f = open(os.path.join(file_path, file_name), 'r',encoding='utf-8')
data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]
data = [[float(j) for j in line] for line in data]

data = [list(line) for line in np.array(data)*lambdas]
data = [[i[0], i[4], i[2]] for i in data]

data = get_nondominated_vectors(data)
data = pd.DataFrame(data=data).drop_duplicates()

print(data)

x =data[0].tolist()
y =data[1].tolist()
z =data[2].tolist()

ax.scatter(x, y, z, c='b', marker='v', label='MOEAD')

ax.set_xlabel('X Label')
ax.set_ylabel('Y Label')
ax.set_zlabel('Z Label')

plt.legend()
plt.show()