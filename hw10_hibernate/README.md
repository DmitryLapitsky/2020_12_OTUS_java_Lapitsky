# OTUS java 2020 12, курс "Java Developer. Professional"

TODO
Домашнее задание
заполнить все //todo

1)  Работа должна использовать базу данных в docker-контейнере - docker не поддерживается системой, использовал postgresql
done

2)  За основу возьмите пример из вебинара про Hibernate (class DbServiceDemo). Добавьте в Client поля: адрес (OneToOne) class AddressDataSet { private String street; } и телефон (OneToMany) class PhoneDataSet { private String number; }
done

3)  Разметьте классы таким образом, чтобы при сохранении/чтении объека Client каскадно сохранялись/читались вложенные объекты.
разметки в классах AddressDataSet, Client, PhoneDataSet
пример использования в классе DbService_HomeWork

ВАЖНО.

Hibernate должен создать только три таблицы: для телефонов, адресов и клиентов.
При сохранении нового объекта не должно быть update-ов. Посмотрите в логи и проверьте, что эти два требования выполняются.