<p align="center">
  <a href="https://github.com/SergeyIvanovDevelop/Robot-Rolly">
    <img alt="Robot-Rolly" src="./resources/Robot-Rolly.png" />
  </a>
</p>
<h1 align="center">
  Robot Rolly
</h1>

## Robot-Rolly &middot; [![GitHub license](https://img.shields.io/badge/license-CC%20BY--NC--SA%203.0-blue)](./LICENSE) [![Python](https://img.shields.io/badge/blockchain-Ethereum-yellowgreen)](https://ethereum.org/en/) [![Java](https://img.shields.io/badge/EVM-solidity-lightgrey)](https://docs.soliditylang.org/en/v0.8.14/) [![RPI3B+](https://img.shields.io/badge/backend-node.js-red)](https://nodejs.org/en/) [![Android Studio](https://img.shields.io/badge/frontend-javascript-yellow)](https://en.wikipedia.org/wiki/JavaScript) [![ROS](https://img.shields.io/badge/database-mongoDB-blueviolet)](https://www.mongodb.com/) [![LinkedIn](https://img.shields.io/badge/linkedin-Sergey%20Ivanov-blue)](https://www.linkedin.com/in/sergey-ivanov-33413823a/) [![Telegram](https://img.shields.io/badge/telegram-%40SergeyIvanov__dev-blueviolet)](https://t.me/SergeyIvanov_dev) ##

Этот репозиторий содержит код и конфигурацию для развертывания и запуска проекта `Robot Rolly`, который представляет из себя аппаратно-программного робота-ассистента.

Проект является составным и включает в себя следующие компоненты:
- Серверная часть (`ROS`, `Python`, `Java`)
- RPI3B+ часть (`ROS`, `Python`)
- Android часть (`Android Studio`, `Java`)

// ТУТ КАРТИНКА КАК ВСЕ УСТРОЕНО (СЕРВЕР, RPI3B+, ANDROID) СО СТРЕЛОЧКАМИ

## :computer: Серверная часть проекта ##

Данная часть проекта отвечет за выполнение вычислительно затратных операций, например, таких как распознавание лиц, распознавание и синтез речи и т.д.

Полный перечень возможностей:
- Обновление конфигурации платформы `Robot Rolly`
- Парсинг новостей/погоды в Интернете
- Распознование речи (оффлайн)
- Синтез речи (оффлайн)
- Распознавание лиц и идентификация пользователей (оффлайн)
- Прием информации от `Android` части ПО по `WiFi`


## :computer: RPI3B+ часть проекта ##

Данная часть проекта отвечет за детектирование препятствий, управление шасси аппаратной платформы робота, записью голосовых команд и воспроизведение аудиофайлов т.д.

Полный перечень возможностей:
- Фотосъемка
- Видеосъемка
- Аудиозапись
- Проведение тренировок по заданному сценарию
- Воспроизведение напоминаний
- Воспроизведение аудиофайлов
- Воспроизведение времени
- Чтение новостей/погоды, присланных серверной частью ПО
- Управление шасси робота по распознанным серверной частью ПО голосовым командам/командам принятым серверной частью по `WiFi`/командам принятым `RPI3B+` по `Bluetooth` от `Android`-устройства
- Воспроизведение результатов идентификации по распознованию лица, осуществленным серверной частью ПО
- Обновление конфигурации `RPI3B+` части, принимаемой от серверной части по `WiFi` или от `Android` части по `Bluetooth` 


// ТУТ В ОБЩЕМ КАРТИНКИ САМОГО РОБОТА

## :computer: Android часть проекта ##

Данная часть проекта отвечет конфигурирование робота (внесение доверенных лиц, установка режимов тренировок, выставление напоминаний, параметры видеосъемки и т.д.) и передачей команд управления шасси робота по `WiFi`/`Bluetooth`.

Полный перечень возможностей:
- Добавление новых пользователей, чьи команды робот `Rolly` будет выполнять (имя и фото человека)
- Установки тренировок (робот может быть использован в качестве персонального фитнес-тренера)
- Добавить напоминание
- Выбор голоса (муж/жен) робота
- Управление шасси робота по `WiFi`/`Bluetooth`

// ТУТ В ОБЩЕМ КАРТИНКИ ANDROID-ПРИЛОЖУХИ


**:clapper: Example using (GIF):**<br>

This animations demonstrates scenarios for using the Robot Rolly.<br>

![](./resources/Rolly_1.gif)
![](./resources/Rolly_2.gif)

### :bookmark_tabs: Licence ###
Robot Rolly is [CC BY-NC-SA 3.0 licensed](./LICENSE).
