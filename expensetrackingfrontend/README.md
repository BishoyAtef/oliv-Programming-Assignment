# 💰 Expense Tracking Frontend

This is the **React + Vite** frontend for the Expense Tracking application. It allows users to:

- Submit expenses with hashtags
- Visualize them using pie charts
- Explore category breakdowns
- View the data in a tree format
- Delete the most recent expense message

This frontend interacts with a Spring Boot backend and a PostgreSQL database via REST APIs.

---

## 📁 Project Structure

expensetrackingfrontend/
├── public/ # Static assets
├── src/
│ ├── components/ # Reusable UI components
│ ├── utils/ # Utility functions (e.g., flattenTree)
│ ├── App.jsx # Main app entry
│ └── main.jsx # React root
├── Dockerfile # Frontend Docker image
└── vite.config.js # Vite config with backend proxy


---

## 🚀 Getting Started

### ✅ Prerequisites

Make sure you have the following installed:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

---

### 🔧 Local Development

1. **Install dependencies:**
npm install


2. Start the development server:
npm run dev

🐳 Docker Compose Setup
This project is meant to run alongside:

expensetrackingbackend: Spring Boot backend

PostgreSQL: Database

You can use a single docker-compose.yml file in the root of your project (sibling to expensetrackingfrontend and expensetrackingbackend):

docker-compose up --build

## fROM The root of the project run:
docker-compose up --build

🖼️ Features
📥 Submit Expenses
Input expenses line-by-line using a simple textbox interface.

📊 Pie Chart Navigation
Clickable donut chart to drill into categories visually.

🌳 Tree View
Explore the entire expense tree hierarchy clearly.

🗑️ Delete Latest Message
Easily remove the most recent submitted record.

📅 Dropdown to Choose Dates
Select a historical snapshot of your expenses.

🛠️ Tech Stack
React (Vite)

TailwindCSS

Recharts (for pie charts)

Axios

Docker


🧪 Future Improvements
Authentication (multi-user support)

More advanced chart filtering

Export data to CSV

Responsive layout improvements