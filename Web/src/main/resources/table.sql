-- INSERT INTO fact (content, type) VALUES
-- -- Факты о Java
-- ('Java является языком программирования, который был разработан компанией Sun Microsystems в 1995 году.', 'JAVA'),
-- ('Java является объектно-ориентированным языком программирования.', 'JAVA'),
-- ('Java используется в различных областях, включая веб-разработку, мобильные приложения и встраиваемые системы.', 'JAVA'),
--
-- -- Факты о Украине
-- ('Украина является самой крупной страной в Европе по площади.', 'UKRAINE'),
-- ('Киев, столица Украины, является одним из древнейших городов Европы.', 'UKRAINE'),
-- ('Государственный флаг Украины состоит из двух горизонтальных полос: голубой и желтой.', 'UKRAINE'),
--
-- -- Разные факты
-- ('Планета Земля вращается вокруг своей оси с скоростью около 1670 километров в час.', 'MISC'),
-- ('Существует около 8,7 миллиона видов живых организмов на Земле.', 'MISC'),
-- ('В среднем, человек проводит около 6 лет своей жизни во сне.', 'MISC');

INSERT INTO fact (content, type) VALUES
-- Факты о Java
('Java является языком программирования, который был разработан компанией Sun Microsystems в 1995 году.', 'JAVA'),
('Java является объектно-ориентированным языком программирования.', 'JAVA'),
('Java используется в различных областях, включая веб-разработку, мобильные приложения и встраиваемые системы.', 'JAVA'),
('Java поддерживает платформонезависимость благодаря технологии Java Virtual Machine (JVM).', 'JAVA'),
('Существует множество популярных фреймворков для Java, таких как Spring и Hibernate.', 'JAVA'),
('Java имеет богатую стандартную библиотеку, что облегчает разработку приложений.', 'JAVA'),
('Java является одним из самых популярных языков программирования в мире.', 'JAVA'),
('Java используется в разработке Android-приложений.', 'JAVA'),
('Java позволяет разработчикам писать код один раз и запускать его где угодно.', 'JAVA'),
('Java поддерживает многопоточность, что позволяет выполнять несколько операций одновременно.', 'JAVA'),

-- Факты о Украине
('Украина является самой крупной страной в Европе по площади.', 'UKRAINE'),
('Киев, столица Украины, является одним из древнейших городов Европы.', 'UKRAINE'),
('Государственный флаг Украины состоит из двух горизонтальных полос: голубой и желтой.', 'UKRAINE'),
('Украина имеет богатую культурную историю, включая народные танцы и музыку.', 'UKRAINE'),
('Численность населения Украины составляет около 40 миллионов человек.', 'UKRAINE'),
('Киевская Русь была одним из первых государственных образований на территории Украины.', 'UKRAINE'),
('Украина является одним из крупнейших производителей подсолнечного масла в мире.', 'UKRAINE'),
('Среди известных украинцев - писатель Тарас Шевченко и поэтесса Лесya Украинка.', 'UKRAINE'),
('Днепр — одна из самых больших рек в Европе и протекает через Украину.', 'UKRAINE'),
('Карпаты — одна из крупнейших горных систем Украины, привлекающая туристов.', 'UKRAINE'),

-- Разные факты
('Планета Земля вращается вокруг своей оси с скоростью около 1670 километров в час.', 'MISC'),
('Существует около 8,7 миллиона видов живых организмов на Земле.', 'MISC'),
('В среднем, человек проводит около 6 лет своей жизни во сне.', 'MISC'),
('Человеческое сердце бьется в среднем 100 000 раз в день.', 'MISC'),
('Солнечная система состоит из 8 планет, включая Землю.', 'MISC'),
('Кости человека прочнее стали, если сравнивать их по весу.', 'MISC'),
('Около 70% поверхности Земли покрыто водой.', 'MISC'),
('На Луне нет атмосферы, поэтому звук не может распространяться.', 'MISC'),
('Первое животное, отправившееся в космос, была собака по имени Лайка.', 'MISC'),
('Существует около 3 триллионов деревьев на Земле.', 'MISC');



INSERT INTO fact (content, type) VALUES
-- Дополнительные факты о Java
('Java поддерживает автоматическое управление памятью через сборщик мусора.', 'JAVA'),
('Первоначально Java была разработана для интерактивного телевидения.', 'JAVA'),
('Java-апплеты позволяли запускать программы в веб-браузерах.', 'JAVA'),
('В Java все примитивные типы данных имеют фиксированный размер.', 'JAVA'),
('Java не поддерживает множественное наследование классов.', 'JAVA'),
('Концепция интерфейсов в Java позволяет реализовать полиморфизм.', 'JAVA'),
('Java поддерживает создание анонимных классов.', 'JAVA'),
('Ключевое слово "final" в Java используется для создания констант.', 'JAVA'),
('Java предоставляет встроенную поддержку для работы с потоками.', 'JAVA'),
('Исключения в Java делятся на проверяемые и непроверяемые.', 'JAVA'),
('Java позволяет создавать обобщенные классы и методы.', 'JAVA'),
('Аннотации в Java позволяют добавлять метаданные к коду.', 'JAVA'),
('Java поддерживает лямбда-выражения с Java 8.', 'JAVA'),
('Stream API было введено в Java 8 для обработки коллекций.', 'JAVA'),
('Java поддерживает функциональные интерфейсы.', 'JAVA'),
('В Java существует концепция внутренних классов.', 'JAVA'),
('Ключевое слово "static" в Java используется для создания членов класса.', 'JAVA'),
('Java поддерживает перечисления (enum) для создания констант.', 'JAVA'),
('Рефлексия в Java позволяет исследовать и модифицировать поведение программы во время выполнения.', 'JAVA'),
('Java поддерживает создание и использование пакетов для организации кода.', 'JAVA'),
('Ключевое слово "super" в Java используется для вызова конструктора или методов суперкласса.', 'JAVA'),
('В Java существует концепция абстрактных классов.', 'JAVA'),
('Java поддерживает создание и использование методов с переменным числом аргументов.', 'JAVA'),
('Синхронизация в Java позволяет контролировать доступ к разделяемым ресурсам в многопоточных программах.', 'JAVA'),
('Java поддерживает использование ассертов для отладки кода.', 'JAVA'),

-- Дополнительные факты об Украине
('Украина занимает первое место в Европе по запасам плодородных черноземов.', 'UKRAINE'),
('Украинский язык занимает второе место в мире по мелодичности после итальянского.', 'UKRAINE'),
('Самая длинная пещера в Украине - Оптимистическая, ее длина составляет 240 км.', 'UKRAINE'),
('Украина является одним из крупнейших экспортеров меда в мире.', 'UKRAINE'),
('В Украине находится географический центр Европы (село Деловое, Закарпатская область).', 'UKRAINE'),
('Украинская вышиванка признана ЮНЕСКО нематериальным культурным наследием человечества.', 'UKRAINE'),
('Львов имеет самое большое количество кафе на душу населения в мире.', 'UKRAINE'),
('Украинский "Южмаш" производил самые мощные ракеты-носители в мире.', 'UKRAINE'),
('Украинец Игорь Сикорский изобрел вертолет и основал компанию Sikorsky Aircraft.', 'UKRAINE'),
('Украина входит в топ-3 стран мира по экспорту зерновых культур.', 'UKRAINE'),
('Киево-Печерская лавра - один из первых монастырей Киевской Руси, основанный в 1051 году.', 'UKRAINE'),
('Украинский физик Иван Пулюй сделал первый рентгеновский снимок за 14 лет до открытия рентгеновских лучей.', 'UKRAINE'),
('Украинская писанка (пасхальное яйцо) - уникальное явление в мировой культуре.', 'UKRAINE'),
('Украинский борщ внесен в список нематериального культурного наследия ЮНЕСКО.', 'UKRAINE'),
('Самая старая карта, на которой упоминается Украина, датируется 1650 годом.', 'UKRAINE'),
('Украинский город Умань славится своим парком "Софиевка", одним из самых красивых парков Европы.', 'UKRAINE'),
('Украинский Чернобыль стал местом крупнейшей ядерной катастрофы в истории человечества.', 'UKRAINE'),
('Украинская гривна - одна из самых красивых валют в мире по версии Международного финансового банка.', 'UKRAINE'),
('Украинец Сергей Бубка установил 35 мировых рекордов в прыжках с шестом.', 'UKRAINE'),
('Украинская бандура - уникальный музыкальный инструмент, не имеющий аналогов в мире.', 'UKRAINE'),
('Украинский фильм "Тени забытых предков" входит в список 100 лучших фильмов в истории кинематографа.', 'UKRAINE'),
('Украинская певица Квитка Цисык была обладательницей двух премий "Грэмми".', 'UKRAINE'),
('Украинский Каменец-Подольский замок входит в список семи чудес Украины.', 'UKRAINE'),
('Украинский остров Хортица - крупнейший речной остров в Европе.', 'UKRAINE'),
('Украинский ученый Илья Мечников получил Нобелевскую премию по физиологии и медицине в 1908 году.', 'UKRAINE'),

-- Дополнительные разные факты
('Муравьи никогда не спят и могут поднять вес в 50 раз больше собственного.', 'MISC'),
('Октопусы имеют три сердца и голубую кровь.', 'MISC'),
('Банановые деревья технически являются травами, а не деревьями.', 'MISC'),
('Мед никогда не портится. Археологи нашли мед в египетских гробницах, которому более 3000 лет.', 'MISC'),
('Эйфелева башня может быть на 15 см выше летом из-за теплового расширения металла.', 'MISC'),
('Человеческий мозг генерирует около 70,000 мыслей в день.', 'MISC'),
('Колибри - единственные птицы, которые могут летать назад.', 'MISC'),
('Самая большая пустыня в мире - Антарктида, а не Сахара.', 'MISC'),
('Человеческое тело содержит достаточно углерода для создания 900 карандашей.', 'MISC'),
('Молнии ударяют Землю около 100 раз каждую секунду.', 'MISC'),
('Кокосовые орехи убивают больше людей, чем акулы, каждый год.', 'MISC'),
('Вес облака может достигать миллиона фунтов.', 'MISC'),
('Горячая вода замерзает быстрее, чем холодная, - это называется эффектом Мпембы.', 'MISC'),
('Во вселенной больше звезд, чем песчинок на всех пляжах Земли.', 'MISC'),
('Человеческий нос может различить более триллиона запахов.', 'MISC'),
('Самое сухое место на Земле - долины Мак-Мердо в Антарктиде, где не было дождей уже 2 миллиона лет.', 'MISC'),
('Единственное место на Земле, где можно увидеть американский флаг, развевающийся 24/7, - это на Луне.', 'MISC'),
('Человеческая ДНК на 50% идентична ДНК банана.', 'MISC'),
('Длина кровеносных сосудов взрослого человека около 100 000 километров.', 'MISC'),
('В Японии есть остров, населенный кроликами.', 'MISC'),
('Скорость вращения Земли постепенно замедляется. Каждые 100 лет день становится длиннее примерно на 2 миллисекунды.', 'MISC'),
('Самое длинное зарегистрированное эхо длилось 75 секунд.', 'MISC'),
('В среднем человек проводит 6 месяцев своей жизни, ожидая на красный свет светофора.', 'MISC'),
('На Юпитере и Сатурне идут алмазные дожди.', 'MISC'),
('Язык синего кита весит столько же, сколько взрослый слон.', 'MISC');