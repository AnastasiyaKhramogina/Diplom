В процессе тестирования были найдены 3 бага:
1. При указании владельца можно указать невалидные данные;
2. При заполнении формы валидными данными (4444 4444 4444 4444) отображаются сообщения и об одобрении операции со стороны банка, и об отказе;
3. При заполнении формы валидными данными карты со статусом 'DECLINED' всплывает сообщение об одобрении операции банком.

Общее количество тест-кейсов: 20

Количество успешных тест-кейсов: 17 (85%), неупешных - 3 (15%).