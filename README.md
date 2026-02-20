# Credit Card AI Architecture System

Enterprise-grade credit card core system with AI-assisted design automation.

## Project Overview

This project demonstrates:
1. **Credit Card Core System** - Spring Boot implementation of key modules
2. **Architecture Templates** - Message format, API design, screen flow templates
3. **AI Agent System** - Multi-agent architecture for design automation
4. **Quality Assurance** - Comprehensive tests

## Tech Stack

- Java 17 / Spring Boot 3.x
- Python 3.x (AI Agent)
- AWS Bedrock + Claude
- Maven/Gradle
- JUnit 5, MockMvc

## Modules

### 1. Core Domain (`credit-card-core/`)
- Member Management (入会・会員管理)
- Credit Management (与信管理)
- Transaction Management (取引管理)
- Billing/Settlement (請求・清算)
- Fraud Detection (不正検知)

### 2. Architecture Templates (`templates/`)
- Message format definitions (JSON/YAML)
- API specification templates
- Screen flow templates
- Prompt templates for AI agents

### 3. AI Agent System (`ai-agent/`)
- Planner Agent - Requirement analysis
- Architect Agent - Design structure
- Coder Agent - Code generation
- Tester Agent - Test generation

## Getting Started

```bash
# Build core system
cd credit-card-core
./mvnw clean install

# Run AI agent demo
cd ai-agent
python3 agent_runner.py --input "Generate credit card billing module"
```

## Architecture

See `docs/architecture.md` for detailed system architecture.

## License

MIT
