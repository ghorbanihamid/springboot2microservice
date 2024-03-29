INSERT INTO CST.CUSTOMERS(FIRST_NAME, LAST_NAME,BIRTH_DATE, PHONE_NUMBER, EMAIL_ADDRESS, CREATED_DATE)
VALUES('Hamid','Ghorbani','1979-10-10','7786362221','hamid@a.com','2021-03-10T15:47:48.905');

INSERT INTO CST.CUSTOMERS(FIRST_NAME, LAST_NAME,BIRTH_DATE, PHONE_NUMBER, EMAIL_ADDRESS, CREATED_DATE)
VALUES('Adrian','Ghorbani','2019-10-10','7786362222','adrian@gmail.com','2021-03-10T15:47:48.905');

INSERT INTO CST.USERS(CUSTOMER_ID, USER_NAME, PASSWORD,LOCKED,ENABLED,FAILED_ATTEMPT, CREATED_DATE)
VALUES(1,'hamid','hamid',false,true,0,'2021-03-10T15:47:48.905');

INSERT INTO CST.USERS(CUSTOMER_ID, USER_NAME, PASSWORD,LOCKED,ENABLED,FAILED_ATTEMPT, CREATED_DATE)
VALUES(2,'test','$2a$10$U0jeRx2HttmZFxOpvVP7ee0oqVvF5RShCBvelUovtdGRYpbBhQmGm',false,true,0,'2021-03-10T15:47:48.905');

INSERT INTO CST.AUTHORITIES(USER_ID,ROLE,CREATED_DATE)values(1,'ROLE_ADMIN','2021-03-10T15:47:48.905');

INSERT INTO CST.AUTHORITIES(USER_ID,ROLE,CREATED_DATE)values(2,'ROLE_USER','2021-03-10T15:47:48.905');

INSERT INTO CST.CUSTOMER_ADDRESSES(CUSTOMER_ID, STREET_NUMBER, STREET_NAME, CITY_NAME, STATE_NAME, COUNTRY_NAME, ZIP_CODE, CREATED_DATE)
VALUES(1, 1807, 'Happy Steet', 'Loas angeles', 'California', 'United State', '123456','2021-03-10T15:47:48.905');

INSERT INTO CST.CUSTOMER_ADDRESSES(CUSTOMER_ID, STREET_NUMBER, STREET_NAME, CITY_NAME, STATE_NAME, COUNTRY_NAME, ZIP_CODE, CREATED_DATE)
VALUES(2, 1807, 'Happy Steet', 'Loas angeles', 'California', 'United State', '123456','2021-03-10T15:47:48.905');

SELECT * FROM CST.CUSTOMERS ;
SELECT * FROM CST.USERS;
SELECT * FROM CST.AUTHORITIES;