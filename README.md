Проект: Регистратура больницы (attestation03)

Как запустить:
1. Убедитесь, что у вас установлены Java 17 и Maven.
2. Распакуйте или перейдите в каталог проекта.
3. Запустите: mvn spring-boot:run
4. Откройте http://localhost:8080/ - простая страница
   API patients: http://localhost:8080/api/patients

Особенности:
- Используется H2 in-memory DB
- Soft delete реализован полем 'deleted' в сущности Patient
- CRUD через REST (JSON)
- Lombok используется для сокращения кода моделей
- Включён пример unit/integration теста
