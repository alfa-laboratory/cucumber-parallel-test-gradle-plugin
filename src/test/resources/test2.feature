#language:ru

@middle
@debit-cards
@alfa-travel
@desktop
Функциональность: Заказ дебетовой карты Alfa-Travel

  Предыстория: Открылась страница дебетовой карты Alfa-Travel
    Дано совершен переход на страницу "Страница заказа Alfa-Travel" по ссылке "alfaTravel"

  @desktop-positive
  Сценарий: Заказ дебетовой карты Alfa-Travel Premium
    Когда элемент "Заказать карту" существует на странице
    Тогда выполнено нажатие на кнопку "Заказать карту"
    И выполнено нажатие на кнопку "Отправить заявку"
    То выполнено переключение на следующую вкладку
    И вкладка "Анкета: первый шаг" загрузилась