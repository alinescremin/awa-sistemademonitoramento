# awa-sistemademonitoramento
//Trabalho de conclusão de curso - UTFPR
//ALEXANDRE PEDRO SANTANA - ALINE NUNES SCREMIN - WILLIAN PASQUETI DE ALMEIDA 


//Este projeto apresenta um hardware e uma plataforma de software, denominada AWA
//composta por microcontrolador ESP32, responsável pela comunicação, 
//sensores (Célula de carga e MQ-6, para o monitoramento do peso do botijão e a detecção de vazamentos), 
//e o atuador HX711, que amplifica o sinal proveniente dos sensores, transmitindo para o microcontrolador.

//Essas informações coletadas do AWA são enviadas pelo microcontrolador através de um protocolo de comunicação para um aplicativo móvel conectado ao Wi-fi
//o qual disponibiliza os dados ao usuário. Dessa forma, o consumo diário de GLP e o alerta de possíveis vazamentos
//possibilitará ao consumidor um maior controle de gastos e ainda, o mais importante
//contribui para evitar possíveis acidentes envolvendo botijão de gás
