package com.gitlab.tulliocba.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RSAKeyUtils {
    public static String getPublicKey() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhJpSv5JKfWfHlkAxjxhzHZvfZrFL63B+" +
                "iVhTOdoMxtYQJ+SFB/eLXkW9LEhdVK12JE7+BbqvfaYDXUk4DHmvRtq8csk7tJUF0zHd7TM0XHGg" +
                "880adFwMIlDE2v0Rop6EFnsH/Ll/cdZgRo5+12FVXcYQil1KZP4Qcm854K39mjUO4l9rbXV2fNsg1+" +
                "W/Ytt6mQQlkhdXSUP8mjbz5mo7l66WIW99ZMfn4ui38F0jSJvIuPGZa/kPPOCjnY7R4ca4I05qmQsU" +
                "vXOl08UGhUMwrFzdlHNgSEsNEMjx+D/P7Uy4fszuueJ7vSRNIVZI1QkLh1YAYF05+fradgtH+uaCKQIDAQAB";
    }

    public static String getPrivate() {
        return "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCEmlK/kkp9Z8eWQDGPGHMdm99msUvrcH6J" +
                "WFM52gzG1hAn5IUH94teRb0sSF1UrXYkTv4Fuq99pgNdSTgMea9G2rxyyTu0lQXTMd3tMzRccaDzzRp0XAwiU" +
                "MTa/RGinoQWewf8uX9x1mBGjn7XYVVdxhCKXUpk/hBybzngrf2aNQ7iX2ttdXZ82yDX5b9i23qZBCWSF1dJ" +
                "Q/yaNvPmajuXrpYhb31kx+fi6LfwXSNIm8i48Zlr+Q884KOdjtHhxrgjTmqZCxS9c6XTxQaFQzCsXN2Uc2" +
                "BISw0QyPH4P8/tTLh+zO654nu9JE0hVkjVCQuHVgBgXTn5+tp2C0f65oIpAgMBAAECggEAGEUSniONZLSI8D" +
                "Qhnfown5u24NnUdmwpjMPQv8bsDbyCSnrl6lZheMi03hUEmAoDlJOspKnNkua+uOU8z/O5o/NHCI9SUTPxU/1" +
                "NjCuSCtV1SwogLGnogqNb1GT7RFNVA7RS+uuMqu48ws0z5AHZmQc2Ycy/YAqsQfWfl0LhJscJDMAKqmeEtPVRSz" +
                "Zuuz1Iqdnr3j02Imq3NZO4W8JMzxjH7GKJ47dd4yrY1AHXS0GHcMTwPMIA0jt9xZhrBjfQmhTb9zgS4f58yeIO" +
                "khDamv2UwmVVwrp9IDgcMnK15VxyvEbb0/v7rsM+Z30IlHU3bcszNveC7MIEgHrwQWJBcQKBgQDWu8Pat/UT//" +
                "hh3F4qFHZ9v7ZZ/8XMnTu6Ii9FD2B+Qe7HX6d2T+QTgX97LAQjPEupcx5fnJ0mWXeGTXNaMgMd4+sbRM1K60Pp" +
                "SjX92yejpLkdWIk9gtagYyd3+2J8DaQmjjSNyYf+vCiN1qWceK5KjyCkmLmnCuuFk2yjDzDnFQKBgQCeFfrp2JA" +
                "XGM++/hi2KK60sBnVNv5SBC3JiZ4VQEiqatKhjPMRrulNOupWKtcP8iwqv536ETdLoj8BlmePiJrB4eC3PiFF4" +
                "cecxa7d5Fzi74TaSxu14cVFoNIFbQqQ2MzfAzFmctKRF6EKCbcNm1b6h4IP8oV2X0dOg75+Ei2zxQKBgQCZv18" +
                "AH26unzvsdahGYNhmOPoLfoLVek7lesx1mgjw3axlvCtfHtyiyrnIKz/bKrTR4scAllY+uxhFZPcOwXhR0nrnz" +
                "19eIQ1C4AvAc0XyeWLBjD7Xd/LMa7I6yhc901aQ9IjUbFOfJIG07NcvLYk9gTVSJLfoznzL6z6BkcLl8QKBgGa" +
                "B2jHIO6a+b/dozJ1Rw/dmOVoITuDf1NDaa6dcB8J5ghs/Sl4tDRsHGOzG2q18WyddGRMOFsz0Z+afoIdyDOerk" +
                "AtCPlESFl2R0dIQQirlJUBnc5B81W5Q2d6q1i8WZcI2T9Ieomty5vmbUxd/oubQ94VfvSsEnThMNTmKkBgBAoG" +
                "AS8ZVe/i6tSbj+6YzgbYp3yjWFJR+L7+yIawUrQ52eh+cIQlxKHPVdxoShdN/8X2VxAPMgmkQVaD1EFTWALts0" +
                "WZsRqlOVi1v6EYBZB8H8OSr6YIT0cIvJ2FyO7KEdt0y1GOn8Fzcs5REpTaw3hAiAtTs5XW2QgMCpNb+3CgMSOs=";
    }

}
