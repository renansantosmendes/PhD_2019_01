import os
import methods as mt
import numpy as np
import pandas as pd
import matplotlib. pyplot as plt
from pygmo import *

lst = [0.1, 10.0, 12.0, 600.0, 50.0, 12.0, 1.0, 6000.0]
lambdas = np.array([float(i) for i in lst])

def plot_hypervolume_convergence(file_path, folder, file, dimension, reference_point=None, plot=True):
    number_of_executions = 10
    file_path = file_path
    hv_df = pd.DataFrame()

    for i in range(number_of_executions):
        file_name = file + str(i) + '.txt'

        f = open(os.path.join(file_path, folder, file_name),'r',encoding='utf-8')
        print(os.path.join(file_path, folder, file_name))
        data = f.readlines()
        splitted_data = ' '.join([i for i in data]).split('#\n')
        splitted_data = [i for i in splitted_data if len(i) > 1]

        hv_pareto = []
        indexes = []
        for k in range(len(splitted_data)):
            converted = [i.strip().split(',') for i in splitted_data[k].split('\n') if len(i) > 1]
            pop = [[float(j) for j in i] for i in converted]
            hv = hypervolume(pop)
            if reference_point is None:
                hv_pareto.append(hv.compute([200000,150000,150000,150000,150000,150000,150000,1]))
            else:
                hv_pareto.append(hv.compute(reference_point))
            indexes.append(100*k)
        df = pd.DataFrame(data=[indexes, hv_pareto]).T
        columns_string = 'Evaluation HV'+str(i)
        df.columns = columns_string.split()
        hv_df = pd.concat([hv_df, df[columns_string.split()[1]]], axis=1, sort=False)
    if plot:
    	hv_df.T.mean().plot(x='Evaluation', y='HV',figsize=(10,8))
    return hv_df.T.mean(), hv_df

def plot_hypervolume_convergence_reduced(file_path, folder, file, dimension, reference_point=None, plot=True):
    number_of_executions = 10
    file_path = file_path
    hv_df = pd.DataFrame()

    for i in range(number_of_executions):
        file_name = file + str(i) + '.txt'

        f = open(os.path.join(file_path, folder, file_name),'r',encoding='utf-8')
        data = f.readlines()
        splitted_data = ' '.join([i for i in data]).split('#\n')
        splitted_data = [i for i in splitted_data if len(i) > 1]
        
        hv_pareto = []
        indexes = []
        for k in range(len(splitted_data)):
            converted = [i.strip().split(',') for i in splitted_data[k].split('\n') if len(i) > 1]
            pop = [[float(j) for j in i] for i in converted]
            pop = [list(line) for line in np.array(pop)*lambdas]
            
            if dimension == 1:
                pop = [[i[0], i[4], i[2]] for i in pop]
                pop = get_nondominated_vectors(pop)
                hv = hypervolume(pop)
                hv_pareto.append(hv.compute([150000,150000,150000])) 
            elif dimension == 2:
                pop = [[i[0] + i[3] + i[6] + i[7], i[1] + i[4], i[2] + i[5]] for i in pop]
                pop = get_nondominated_vectors(pop)
                hv = hypervolume(pop)
                hv_pareto.append(hv.compute([150000,150000,150000]))
            indexes.append(100*k)
        df = pd.DataFrame(data=[indexes, hv_pareto]).T
        columns_string = 'Evaluation HV'+str(i)
        df.columns = columns_string.split()
        hv_df = pd.concat([hv_df, df[columns_string.split()[1]]], axis=1, sort=False)
    if plot:
    	hv_df.T.mean().plot(x='Evaluation', y='HV',figsize=(10,8))
    return hv_df.T.mean(), hv_df

def get_hv(file_name):
    with open(file_name) as file:
        hv = []
        
        for line in file:
            hv.append(float(line))
            
    return pd.DataFrame(data=hv, columns=['HV'])

def random_test(df1, df2, number_of_samples=30, plot_hist = True):
    DORAND = 2300
    
    spread2 = df2.values
    spread1 = df1.values
    
    medianSpreadDiff = np.median(spread2) - np.median(spread1)
    meanSpreadDiff = np.mean(spread2) - np.mean(spread1)
    
    totalSpread = np.append(spread1.tolist(), spread2.tolist())
    
    randMedianSpreadDiff = np.nan * np.ones((DORAND,1))
    randMeanSpreadDiff = np.nan * np.ones((DORAND,1))
    
    for randPool in range(0, DORAND-1):
        new1Index = np.random.permutation(number_of_samples)
        newSpread1 = totalSpread[new1Index[0:int(number_of_samples/2)]]
        newSpread2 = totalSpread[new1Index[int(number_of_samples/2):number_of_samples]]
        
        randMedianSpreadDiff[randPool] = np.median(newSpread2) - np.median(newSpread1)
        randMeanSpreadDiff[randPool] = np.mean(newSpread2) - np.mean(newSpread1)
        
    randMedianSpreadDiff[DORAND - 1] = medianSpreadDiff
    randMeanSpreadDiff[DORAND - 1] = meanSpreadDiff
    
    z = (meanSpreadDiff - np.mean(randMeanSpreadDiff) )/ np.std(randMeanSpreadDiff)
        
    limiar = 1.96 * np.std(randMeanSpreadDiff) + np.mean(randMeanSpreadDiff)
    
    if z <= -1.96:
        print('H1-')
    elif z >= 1.96:
        print('H1+')
    else:
        print('H0')
    print('Limiar =',limiar)
    print('z =',z)
    print('Mean Spread Diff',meanSpreadDiff)
    if plot_hist:
        plt.figure(figsize=(9,6))
        plt.hist(randMeanSpreadDiff, bins=60,color='gray', label='Distribution')
        plt.scatter(x=meanSpreadDiff,y=0,color='red',s=100,label='Observed Mean Difference')
        plt.scatter(x=limiar,y=0,color='black',s=100,label='Confidence Limiars (95%)')
        plt.scatter(x=-limiar,y=0,color='black',s=100,)
        plt.xticks(fontsize=(20))
        plt.yticks(fontsize=(18))
        plt.legend(fontsize = 'large')
        plt.show()

def pareto(p,q):#p domina q? a resposta é um booleano
    y = False
    if sum(p >= q) == len(p):
        if sum(p == q) != len(p):
            y = True
    return y

def set_coverage_metric(X,Y):
    p_is_dominated = 0
    q_is_dominated = 0
    X_bool = np.zeros(X.shape[0])
    Y_bool = np.zeros(Y.shape[0])
    
    #q dominates p?
    for p in range(Y.shape[0]):
        p_ = Y[p]
        for q in X:
            if pareto(p_,q):
                Y_bool[p] = 1
                
    #q dominates p?
    for q in range(X.shape[0]):
        q_ = X[q]
        for p in Y:
            if pareto(q_,p):
                X_bool[q] = 1
   
    print('Returning C(A,B) and C(B,A)')
    return sum(Y_bool)/len(Y), sum(X_bool)/len(X)

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

def get_combined_pareto(path):
    file_path = path #'/home/renansantos/Área de Trabalho/Doutorado/PhD_2019_01/PhD_2019_01/Results_2020/MOEAD/MOEAD_R3_CA/'
    file_name = 'moead-combined_pareto_reduced.csv'

    f = open(os.path.join(file_path, file_name), 'r',encoding='utf-8')
    data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]
    data = [(float(i[0]), float(i[1]), float(i[2])) for i in data]
    data = pd.DataFrame(data=data).drop_duplicates()

    return data

def get_combined_pareto_moead_agr(path):
    file_path = path #'/home/renansantos/Área de Trabalho/Doutorado/PhD_2019_01/PhD_2019_01/Results_2020/MOEAD/MOEAD_R3_CA/'
    file_name = 'moead-combined_pareto_reduced.csv'

    f = open(os.path.join(file_path, file_name), 'r',encoding='utf-8')
    data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]
    data = [[float(j) for j in line] for line in data]

    data = [list(line) for line in np.array(data)*lambdas]
    data = [[i[0] + i[3] + i[6] + i[7], i[1] + i[4], i[2] + i[5]] for i in data]

    data = get_nondominated_vectors(data)
    data = pd.DataFrame(data=data).drop_duplicates()
    return data

def get_combined_pareto_moead_esc(path):
    file_path = path #'/home/renansantos/Área de Trabalho/Doutorado/PhD_2019_01/PhD_2019_01/Results_2020/MOEAD/MOEAD_R3_CA/'
    file_name = 'moead-combined_pareto_reduced.csv'

    f = open(os.path.join(file_path, file_name), 'r',encoding='utf-8')
    data = [i.split(']')[0].replace('[','').replace(',','').split() for i in f.readlines()]
    data = [[float(j) for j in line] for line in data]

    data = [list(line) for line in np.array(data)*lambdas]
    data = [[i[0], i[4], i[2]] for i in data]

    data = get_nondominated_vectors(data)
    data = pd.DataFrame(data=data).drop_duplicates()
    return data

def plot_combined_pareto(combined_pareto_1, combined_pareto_2, label_1, label_2):
    fig = plt.figure(figsize=(10,8))
    ax = fig.add_subplot(111, projection='3d')
    data_1 = combined_pareto_1
    x_1 = data_1[0].tolist()
    y_1 = data_1[1].tolist()
    z_1 = data_1[2].tolist()
    ax.scatter(x_1, y_1, z_1, c='r', marker='o', label=label_1, s=60)
    data_2 = combined_pareto_2
    x_2 = data_2[0].tolist()
    y_2 = data_2[1].tolist()
    z_2 = data_2[2].tolist()
    ax.scatter(x_2, y_2, z_2, c='b', marker='v', label=label_2, s=60)

    ax.set_xlabel('$FO_1$')
    ax.set_ylabel('$FO_2$')
    ax.set_zlabel('$FO_3$')
    plt.legend()
    plt.show()