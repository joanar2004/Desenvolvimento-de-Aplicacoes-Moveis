# AI Agent Guidelines

The AI agent must follow a planning-first approach when assisting with this project.

## Rules

- Always read the full documentation inside `/docs` before generating any code.
- Follow the architecture defined in `docs/06_architecture.md` strictly.
- Generate **Kotlin** code only — no Java.
- UI must use **XML Views** — do not use Jetpack Compose.
- Do not generate large files at once; generate code step-by-step following the implementation plan in `docs/08_implementation_plan.md`.
- Each generated file must correspond to a specific step in the implementation plan.
- After generating each step, wait for confirmation before moving to the next.
- Always use **MVVM** pattern: UI → ViewModel → Repository → API Service.
- Use `LiveData` or `StateFlow` for communication between ViewModel and UI.
- Use `Retrofit` for HTTP requests and `Glide` for image loading.
- Handle loading states and API errors gracefully.
- Do not hardcode strings — use `strings.xml`.
- Follow Android best practices for lifecycle management.

## Context

This project is an Android app that displays dog images from the Dog CEO API.  
See `docs/07_api_usage.md` for full API documentation.
