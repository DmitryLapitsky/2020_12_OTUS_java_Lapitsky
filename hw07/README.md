# OTUS java 2020 12, курс "Java Developer. Professional"

TODO
Домашнее задание
заполнить все //todo

1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)

--сделано + обработчик вывода в случае, если field13==null
2. Сделать процессор, который поменяет местами значения field11 и field12

--добавлен процессов ProcessorSwapFields11_12
3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
        Секунда должна определяьться во время выполнения.
        
--добавлен процессор ProcessorEvenSecError с дополнительным методом getTime в надежде замокать его и в тестах передавать четное значение, но не вышло
--добавлен тест evenSecException (в его начало вставлен закоментированный код попытки замокать этот объект, не вышло, сделал грубо, но работает)

4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)

--добавлен лиснер ListenerHistory, где для ведения истории введено поле static Map<Integer, List<Message>> history ( цифра - итерация, а лист - предыдущее и измененное значения)
--добавлен тест (его смысл я не понял, т.к. все объекты message в проекте генерируются builder-ом, т.е. представляют собой отдельные сущности. Но задание было - тест добавил.)
