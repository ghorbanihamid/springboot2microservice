INSERT INTO CST.CUSTOMERS(USER_ID,FIRST_NAME, LAST_NAME,BIRTH_DATE, PHONE_NUMBER, EMAIL_ADDRESS, CREATED_DATE)
VALUES(1,'My first Name','my last name','1979-10-10','7786362222','a@a.com','2021-03-10T15:47:48.905');

INSERT INTO CST.CUSTOMER_ADDRESSES(CUSTOMER_ID, STREET_NUMBER, STREET_NAME, CITY_NAME, STATE_NAME, COUNTRY_NAME, ZIP_CODE, CREATED_DATE)
VALUES(1, 1807, 'Happy Steet', 'Loas angeles', 'California', 'United State', '123456','2021-03-10T15:47:48.905');

INSERT INTO CST.USERS(USER_NAME, PASSWORD,CREATED_DATE)
VALUES('test','$2a$10$U0jeRx2HttmZFxOpvVP7ee0oqVvF5RShCBvelUovtdGRYpbBhQmGm','2021-03-10T15:47:48.905');


INSERT INTO CST.CUSTOMERS(USER_ID,FIRST_NAME, LAST_NAME,BIRTH_DATE, PHONE_NUMBER, EMAIL_ADDRESS, CREATED_DATE)
VALUES(2,'Hamid','Ghorbani','1979-10-10','7786362222','hamid@gmail.com','2021-03-10T15:47:48.905');

INSERT INTO CST.CUSTOMER_ADDRESSES(CUSTOMER_ID, STREET_NUMBER, STREET_NAME, CITY_NAME, STATE_NAME, COUNTRY_NAME, ZIP_CODE, CREATED_DATE)
VALUES(2, 1807, 'Happy Steet', 'Loas angeles', 'California', 'United State', '123456','2021-03-10T15:47:48.905');

INSERT INTO CST.USERS(USER_NAME, PASSWORD,CREATED_DATE)
VALUES('hamid','hamid','2021-03-10T15:47:48.905');
