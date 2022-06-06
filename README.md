<p align="center">
  <a href="https://github.com/SergeyIvanovDevelop/Robot-Rolly">
    <img alt="Robot-Rolly" src="./resources/logo.png" width="300" height="430"/>
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

**:clapper: Функциональная схема**<br>

<p align="center">
  <a href="https://github.com/SergeyIvanovDevelop/Robot-Rolly">
    <img alt="Architecture" src="./resources/architecture.png" />
  </a>
</p>


**:clapper: Принципиальная схема**<br>

<p align="center">
  <a href="https://github.com/SergeyIvanovDevelop/Robot-Rolly">
    <img alt="Architecture" src="./resources/color.png" width="360" height="640" />
  </a>
   <a href="https://github.com/SergeyIvanovDevelop/Robot-Rolly">
    <img alt="Architecture" src="./resources/black-white.png" width="360" height="640" />
  </a>
</p>

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

## :computer: Android часть проекта ##

Данная часть проекта отвечет конфигурирование робота (внесение доверенных лиц, установка режимов тренировок, выставление напоминаний, параметры видеосъемки и т.д.) и передачей команд управления шасси робота по `WiFi`/`Bluetooth`.

Полный перечень возможностей:
- Добавление новых пользователей, чьи команды робот `Rolly` будет выполнять (имя и фото человека)
- Установки тренировок (робот может быть использован в качестве персонального фитнес-тренера)
- Добавить напоминание
- Выбор голоса (муж/жен) робота
- Управление шасси робота по `WiFi`/`Bluetooth`

**:clapper: Illustrations:**<br>

<p align="center">
<img src="./resources/android_icon.jpeg" />
</p>
<p align="center">
    <img src="./resources/android_1.png" width="180" height="320" />
    <img src="./resources/android_2.png" width="180" height="320"/>
    <img src="./resources/android_3.png" width="180" height="320"/>
</p>
<p align="center">
    <img src="./resources/android_4.png" width="180" height="320"/>
    <img src="./resources/android_5.png" width="180" height="320"/>
    <img src="./resources/android_6.png" width="180" height="320"/>
</p>


**:clapper: Example using (GIF):**<br>

This animations demonstrates scenarios for using the Robot Rolly.<br>
<p align="center">
  <img src="./resources/Rolly.gif" alt="animated" />
</p>

### :bookmark_tabs: Licence ###
Robot Rolly is [CC BY-NC-SA 3.0 licensed](./LICENSE).
