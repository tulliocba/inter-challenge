insert into user(ID, EMAIL, NAME, PUBLIC_KEY, STATUS, UUID)
values (nextval('HIBERNATE_SEQUENCE'),
        'ZnPh/h9Bg5P71yY8MnaP5fZ6yh2wjKp8bCinU2khh1rGrGW1vqVhZhhnBcusfNy1rK6IgUng/sBYbgPM3lA3VMqn/yhOkM6pTWSLkHyPao0hCFj2DpOClJ9a5OH94D+NKWmXDCaq/nzy5zRNmCJ8p4QbkcP6+ooW4pN8VwZqttyj76g4NOHz+VOwq+SdrQnJ2CsLWsQ9nZkPH+KhZVGbBbvTh0U4pR89QNI/shAqV6z3wXSQ9ViAagCN0G2fhMV3Ob6pOFZTqUw+VqfVGOrHnw3R8B2bvaiJKjIEL0KTFrLIItPFtcT5I7J1xT1PnUQVjUqrSHmSgw427cBeqvk2DA==',
        'bXzYBAv57xCKSmy+Hj7atqPQDENLfdFUULMXJMLSpeJSWrjsylftPusm3YMGjuAbjW/XlZOd6hziopRN223bBGrA/mxEcqppgAzBlBJMmBlvehKhhLQjU/jvfZfLoFfUcrb7DqfYUGJBVQFWts0/j3VXfWlErN4tiCDNbiM48nsv17nKyz/WORXG5AHAPdfxCIdsy09Ve1tE6CNR8iyseo29SFu66W51cjyI4SC3bUY9csErB9Y99vpf4bdb+0ZXdBU7v6w4x10gh9zx8y/f/MYTC5MwhkP06LRBB7hXzW/hH5jmN6b6atwQLmGI7P7wqyYGn/Qo7iZ/8SgWUmDrdA==',
        'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhJpSv5JKfWfHlkAxjxhzHZvfZrFL63B+iVhTOdoMxtYQJ+SFB/eLXkW9LEhdVK12JE7+BbqvfaYDXUk4DHmvRtq8csk7tJUF0zHd7TM0XHGg880adFwMIlDE2v0Rop6EFnsH/Ll/cdZgRo5+12FVXcYQil1KZP4Qcm854K39mjUO4l9rbXV2fNsg1+W/Ytt6mQQlkhdXSUP8mjbz5mo7l66WIW99ZMfn4ui38F0jSJvIuPGZa/kPPOCjnY7R4ca4I05qmQsUvXOl08UGhUMwrFzdlHNgSEsNEMjx+D/P7Uy4fszuueJ7vSRNIVZI1QkLh1YAYF05+fradgtH+uaCKQIDAQAB',
        'ACTIVE',
        '3fc0313d-9217-4543-9788-7530ac3a0d8e');

insert into unique_number(ID, CONCAT_QUANTITY, INPUT_NUMBER, UNIQUE_NUMBER_RESULT, USER_ID)
values (nextval('HIBERNATE_SEQUENCE'),
        4,
        '9875',
        8,
        (select u.id from user u where u.uuid = '3fc0313d-9217-4543-9788-7530ac3a0d8e'));
