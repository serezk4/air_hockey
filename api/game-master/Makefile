.PHONY: run down build up clean reset restart logs ps dev cat

# Run the application by building and starting the containers
run: down build up cat
	@echo "✨ Running the full workflow: down -> build -> up"

# Stop and remove containers, networks, and volumes
down:
	@echo "⏬ Stopping and removing containers..."
	docker compose down || $(MAKE) hiz
	@echo "✅ Containers stopped and removed."

# Build or rebuild services
build:
	@echo "🔧 Building services..."
	docker compose build --parallel || $(MAKE) hiz
	@echo "✅ Build completed."

# Start containers in detached mode
up:
	@echo "🚀 Starting containers in detached mode..."
	docker compose up -d || $(MAKE) hiz
	@echo "✅ Containers are up and running."

# Reset by stopping and removing containers, networks, and volumes
reset:
	@echo "♻️ Resetting: removing containers and volumes..."
	docker compose down -v || $(MAKE) hiz
	@echo "✅ Reset done! Everything is clean and ready to restart."

# Restart the containers
restart:
	@echo "🔄 Restarting containers..."
	docker compose restart || $(MAKE) hiz
	@echo "✅ Containers restarted."

# Show logs for the containers
logs:
	@echo "📜 Fetching logs..."
	docker compose logs -f || $(MAKE) hiz

# Show running containers
ps:
	@echo "🔍 Listing running containers..."
	docker compose ps || $(MAKE) hiz
	@echo "✅ Listed all running containers."

# Run Docker Compose with the dev-compose.yaml file and print a cat
dev:
	clear
	@echo "🚧 Running with dev-compose.yaml..."
	@echo "=============================================================="
	docker compose -f dev-compose.yaml down -v || $(MAKE) hiz
	@echo "=============================================================="
	docker compose -f dev-compose.yaml up -d --build || $(MAKE) hiz
	@echo "=============================================================="
	@echo "✨ Launched the application with dev-compose.yaml!"
	@echo " /\_/\  "
	@echo "( o.o ) "
	@echo " > ^ <  "

# Fun cat command
cat:
	@echo " /\_/\  "
	@echo "( o.o ) "
	@echo " > ^ <  "

hiz:
	@echo "🚨 Something went wrong! 🚨"
	@echo "💀💀💀💀 Please check the logs for more information. 💀💀💀💀 fucking dumbass 💀💀💀💀💀"
