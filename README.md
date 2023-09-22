# Процедура запуска автотестов
1. Запустить Docker Desktop
2. В терминале IDEA выполнить командау docker-compose up для запуска контейнеров
3. Выполнить команду для запуска приложения java -jar artifacts/aqa-shop.jar
4. Запустить тесты через терминал командой ./gradlew clean test
