# load the lavaan package (only needed once per session)
library(lavaan)

# set file path
setwd("/home/renansantos/Área de Trabalho/Doutorado/PhD_2019_01/PhD_2019_01/RandomSolutions")

# loading the data
dados <- read.table("random_solutions.txt", header=TRUE, sep=',')
library("ggplot2")
library("ggdendro")
attach(dados)
head(dados)

# normalizing the data
obj <- dados

for(i in 1:length(obj)){
  obj$f1 <- (obj$f1 - mean(obj$f1))/(sd(obj$f1) )
  obj$f2 <- (obj$f2 - mean(obj$f2))/(sd(obj$f2) )
  obj$f3 <- (obj$f3 - mean(obj$f3))/(sd(obj$f3) )
  obj$f4 <- (obj$f4 - mean(obj$f4))/(sd(obj$f4) )
  obj$f5 <- (obj$f5 - mean(obj$f5))/(sd(obj$f5) )
  obj$f6 <- (obj$f6 - mean(obj$f6))/(sd(obj$f6) )
  obj$f7 <- (obj$f7 - meakendalln(obj$f7))/(sd(obj$f7) )
  obj$f8 <- (obj$f8 - mean(obj$f8))/(sd(obj$f8) )
}

# Cálculo da correlação para formulação do problema com 5 funções objetivo
FO <- cbind(obj$f1,obj$f2,obj$f3,obj$f4,obj$f5,obj$f6,obj$f7,obj$f8)

d <- cor(FO, method='pearson')
# Determinando a similaridade
I <- diag(rep(1,dim(d)[1]))
#D <- d - I

#min_value = D[which.min(D)]
#max_value = D[which.max(D)]
D <- d
for(i in 1:length(D)){
  D[i] = (D[i] - min(D)) / (max(D) - min(D))
}

D <- d - I

output_cluster<-hclust(as.dist(d),method='single')
dendograma_output_cluster<-plclust(output_cluster,ylab='Correlation')

# daqui pra baixo que está melhor pra plotar
a <- cbind(f1,f2,f3,f4,f5,f6,f7,f8)
dissimilarity_kendall <- 1 - abs(cor(a, method='kendall'))
dissimilarity_pearson <- 1 - abs(cor(a, method='pearson'))
distance_kendall <- as.dist(dissimilarity_kendall)
distance_pearson <- as.dist(dissimilarity_pearson)

plot(hclust(distance_kendall, method = 'single'),
     main="",
     xlab="",
     ylab="",
     sub = "",
     hang = -1,
     cex = 1.5,
     lwd = 1.5)

plot(hclust(distance_pearson, method = 'single'),
     main="",
     xlab="",
     ylab="",
     sub = "",
     hang = -1,
     cex = 1.5,
     lwd = 1.5)

a <- cbind(f1,f2,f3,f4,f5,f6,f7,f8)
a <- head(a)
dissimilarity_kendall <- 1 - abs(cor(a, method='kendall'))

b <- cbind(a[f1])

dissimilarity_kendall
distance_kendall <- as.dist(dissimilarity_kendall)

plot(hclust(distance_kendall, method = 'single'),
     main="",
     xlab="",
     ylab="",
     sub = "",
     hang = -1,
     cex = 1.5,
     lwd = 1.5)

#hc <- hclust(distance, method = 'single')
# USAR ESTE AQUI PARA IMPRIMIR O DENDOGRAMA -> FOI O MELHOR ATÉ AGORA
#ggdendrogram(hc,
#             main="",
#             xlab="",
#             ylab="",
#             sub = "",
#             hang = -1,
#             cex = 1.5,
#             lwd = 2)
