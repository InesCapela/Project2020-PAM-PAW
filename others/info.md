
# Código

## Backend:
* criar users
* criar invoices

## Serviços:
* notifições
* analise OCR
    - RabbitMQ

## Frontend:
* super admin
* visualisar/adicionar/eliminar/editar users
* visualisar/adicionar/eliminar/editar invoices

## Mobile App:
* visualisar/adicionar/eliminar/editar invoices

---

# Requisitos

## Backend:
- API Rest
- Integração simples com RabbitMQ

## Serviço OCR:
- Identificação e analise de termos comuns
		
## Mobile App:
- Guardar dados localmente
- Receção e Envio de dados para o Backend
- Acesso à camera e gestor de ficheiros
- Receber notificações

---

# Fluxo de Dados

## Backend:
1. Recebe invoice de user
2. Adiciona-o á queue do RabbitMQ do Serviço OCR
3. Recebe confirmação do Serviço OCR
4. Envia notificação ao cliente

## Serviço OCR:
1. Recebe invoices na queue
2. Processa invoices
3. Avisa servidor de backend

## Mobile App:
1. User regista-se
2. User login
3. User adiciona invoice de ficheiro/foto
4. Invoice é enviado ao backend para processamento
5. User recebe notificação do processamento
6. User visualiza dados dos invoices


