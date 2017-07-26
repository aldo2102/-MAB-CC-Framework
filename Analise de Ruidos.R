########## Analise Descritiva #########

summary(aldo)
par(mfrow=c(2,1)) 
hist(aldo$CPU.USED)
hist(log10(aldo$CPU.USED))
plot(density((aldo$CPU.USED)), xlab = "Média de uso do CPU", ylab = "Frequência",  main="(a) Distribuição dos dados de uso de CPU sem transformação sem Logaritmo")
plot(density(log10(aldo$CPU.USED)), xlab = "Média de uso do CPU",  ylab = "Frequência", main="(b) Distribuição dos dados de uso de CPU com transformação em Logaritmo")
plot(sort(log10(aldo$CPU.USED)), pch=".")


hist(aldo$Time)
hist(log10(aldo$Time))
plot(density((aldo$Time)), xlab = "Média de Tempo", ylab = "Frequência",  main="(a) Distribuição dos dados de tempo sem transformação sem Logaritmo")
plot(density(log(aldo$Time)), xlab = "Média de Tempo", ylab = "Frequência",  main="(b) Distribuição dos dados de tempo com transformação em Logaritmo")
plot(sort(log10(aldo$Time)), pch=".")

aldo33 = read.csv(file.choose(),header = T, encoding = "UTF-8")

hist(aldo33$erroLog)

hist(aldo33$Erro)
plot(density((aldo33$erroLog)))
plot(density((aldo33$Erro)))


aldoTime = read.csv(file.choose(),header = T, encoding = "UTF-8")

hist(aldoTime$ErroTimeLog)

hist(aldoTime$ErroTime)
plot(density((aldoTime$ErroTimeLog)),main = "",
     )
plot(density((aldoTime$ErroTime)))
