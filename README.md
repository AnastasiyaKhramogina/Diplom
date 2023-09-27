# Процедура запуска автотестов
- Запустить Docker Desktop
- В терминале IDEA выполнить команду <b> docker-compose up </b> для запуска контейнеров
- Выполнить команду для запуска приложения <b>  java -jar artifacts/aqa-shop.jar </b>
- Взаимодействие с базой данных MySQL:  <b>  java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" "-Dspring.datasource.username=app" "-Dspring.da tasource.password=pass" -jar ./artifacts/aqa-shop.jar </b>
- Взаимодействие с базой данных PostgreSQL:   <b> java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" "-Dspring.datasource.username=app" "-Dspri ng.datasource.password=pass" -jar ./artifacts/aqa-shop.jar </b>

  Команды для запуска тестов
- для MySQL:  <b> ./gradlew test "-Ddb.url=jdbc:mysql://localhost:3306/app" "-Ddb.username=app" "-Ddb.password=pass") </b>
- для PostgreSQL:  <b> \./gradlew test "-Ddb.url=jdbc:postgresql://localhost:5432/app" "-Ddb.username=app" "-Ddb.password=pass") </b>
