#!/bin/sh
# Enable plugins
rabbitmq-plugins enable rabbitmq_stomp rabbitmq_management

# Wait for RabbitMQ to start
sleep 10

# Download rabbitmqadmin
curl -o /usr/local/bin/rabbitmqadmin http://localhost:15672/cli/rabbitmqadmin
chmod +x /usr/local/bin/rabbitmqadmin

# Wait a bit longer to ensure readiness
sleep 5

# Declare the queue
rabbitmqadmin declare queue name=chat-queue durable=true
