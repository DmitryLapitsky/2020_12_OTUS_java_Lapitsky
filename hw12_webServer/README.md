# OTUS java 2020 12, курс "Java Developer. Professional"

TODO
Домашнее задание с webserver
запуск hw12_webServer/src/main/java/ru/otus/HomeWork_WebServerWithFilterBasedSecurity.java
для проверки hibernate hw12_webServer/src/main/java/ru/otus/hibernate/demo/Try_Hibernate.java
настройки подключения hibernate в resources/hibernate.cfg.xml

1)  Встроить веб-сервер в приложение из ДЗ про Hibernate ORM (или в пример из вебинара встроить ДЗ про Hibernate)
    встроено, в отдельном пакете hibernate 
        done 
    (немного доработан проект hibernate: добавлены методы getPhones, getAddress и toString для корректной работы с freemarker шаблонизатором)
    Странность в том, что для получения, например, client.address, потребовался метод getAddress в классе Client, но вытаскиваемые данные
    соответствовали методу toString в классе Address (ru.otus.hibernate.crm.model), поэтому там убрал все-все, что создает idea по умолчанию
    
2)  Сделать стартовую страницу, на которой админ должен аутентифицироваться
        done, сразу по http://localhost:8080/index.html получаем страницу ввода логина/пароля (admin/pwd)
        
3)  Сделать админскую страницу для работы с клиентами. На этой странице должны быть доступны следующие функции:
        ▪ создать клиента
           done, на странице списка клиентов сверху ссылка добавления
        ▪ получить список клиентов
            done, сразу после входа