# Produto-Api

### Projeto utilizado para implementar no heroku o microserviço produto-api do projeto microservico-vendas-produto.


# Heroku

### Configuração do projeto para implantar no heroku

criar o arquivo  'system.properties' para definir a execução do java e adicionar a seguinte linha 

```
java.runtime.version=17
```


### Alteração no application.yml

Alterar a configuração do host de venda para :
```
    services:
      sales: ${VENDAS_HOST}
```


Alterar configuração do RabbitMQ :

```
  rabbitmq:
    host: ${RABBIT_MQ_HOST:localhost}
    username: ${RABBIT_MQ_USER:admin}
    password: ${RABBIT_MQ_PASSWORD:admin}
    virtual-host: ${RABBIT_MQ_USER}
```