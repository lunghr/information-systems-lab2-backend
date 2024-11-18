# First Lab Work | Information Systems | 531677

## Description

Реализовать информационную систему, которая позволяет взаимодействовать с объектами класса BookCreature, описание
которого приведено ниже:

```java
public class BookCreature {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int age; //Значение поля должно быть больше 0
    private BookCreatureType creatureType; //Поле не может быть null
    private MagicCity creatureLocation; //Поле не может быть null
    private Float attackLevel; //Значение поля должно быть больше 0, Поле может быть null
    private Ring ring; //Поле не может быть null
}

public class Coordinates {
    private Integer x; //Максимальное значение поля: 506, Поле не может быть null
    private double y; //Значение поля должно быть больше -376
}

public class MagicCity {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Double area; //Значение поля должно быть больше 0, Поле не может быть null
    private int population; //Значение поля должно быть больше 0
    private java.time.LocalDateTime establishmentDate;
    private BookCreatureType governor; //Поле может быть null
    private boolean capital;
    private double populationDensity; //Значение поля должно быть больше 0
}

public class Ring {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int weight; //Значение поля должно быть больше 0
}

public enum BookCreatureType {
    HOBBIT,
    ELF,
    GOLLUM;
}
```

## Requirements

Разработанная система должна удовлетворять следующим требованиям:

- Основное назначение информационной системы - управление объектами, созданными на основе заданного в варианте класса.
- Необходимо, чтобы с помощью системы можно было выполнить следующие операции с объектами: создание нового объекта,
  получение информации об объекте по ИД, обновление объекта (модификация его атрибутов), удаление объекта. Операции
  должны осуществляться в отдельных окнах (интерфейсах) приложения.При получении информации об объекте класса должна
  также выводиться информация о связанных с ним объектах.
- При создании объекта класса необходимо дать пользователю возможность связать новый объект с объектами вспомогательных
  классов, которые могут быть связаны с созданным объектом и уже есть в системе.
- Выполнение операций по управлению объектами должно осуществляться на серверной части (не на клиенте), изменения должны
  синхронизироваться с базой данных.
- На главном экране системы должен выводиться список текущих объетов в виде таблицы (каждый атрибут объекта - отдельная
  колонка в таблице). При отображении таблицы должна использоваться пагинация (если все объекты не помещаются на одном
  экране).
- Нужно обеспечить возможность фильтровать/сортировать строки таблицы, которые показывают объекты (по значениям любой из
  строковых колонок). Фильтрация элементов должна производиться по неполному совпадению.
- Переход к обновлению (модификации) объекта должен быть возможен из таблицы с общим списком объектов и из области с
  визуализацией объекта (при ее реализации).
- При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться в интерфейсах у
  других пользователей, авторизованных в системе.
- Если при удалении объекта с ним связан другой объект, операция должна быть отменена, пользователю нужно сообщить о
  невозможности удаления объекта.
- Пользователю системы должен быть предоставлен интерфейс для авторизации/регистрации нового пользователя. У каждого
  пользователя должен быть один пароль. Требования к паролю: пароль должен быть содержать не менее n символов. В системе
  предполагается использование следующих видов пользователей (ролей):незарегистрированные пользователи,обычные
  пользователи и администраторы. Если в системе уже создан хотя бы один администратор, зарегистрировать нового
  администратора можно только при одобрении одним из существующих администраторов (у администратора должен быть
  реализован интерфейс со списком заявок и возможностью их одобрения).
- Редактировать и удалять объекты могут только пользователи, которые их создали, и администраторы (администраторы могут
  редактировать все объекты).
- Зарегистрированные пользователи должны иметь возможность просмотра всех объектов, но модифицировать (обновлять) могут
  только принадлежащие им (объект принадлежит пользователю, если он его создал). Для модификации объекта должно
  открываться отдельное диалоговое окно. При вводе некорректных значений в поля объекта должны появляться информативные
  сообщения о соответствующих ошибках.

В системе должен быть реализован отдельный пользовательский интерфейс для выполнения специальных операций над объектами:
- Удалить один (любой) объект, значение поля ring которого эквивалентно заданному.
- Вернуть один (любой) объект, значение поля age которого является максимальным.
- Вернуть массив объектов, значение поля name которых содержит заданную подстроку.
- Переместить всех хоббитов с кольцами в Мордор.
- Уничтожить города эльфов.

## Technologies
При разработке ИС должны учитываться следующие требования:
- В качестве основы для реализации ИС необходимо использовать Spring MVC.
- Для создания уровня хранения необходимо использовать Hibernate.
- Разные уровни приложения должны быть отделены друг от друга, разные логические части ИС должны находиться в отдельных компонентах.