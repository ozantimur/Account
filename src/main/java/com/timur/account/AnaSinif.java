package com.timur.account;

import com.timur.account.model.Kullanici;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AnaSinif {

    public static List<Kullanici> kullaniciList = new ArrayList<>();
    private static final int DOSYA_SATIRDAKI_VERI_SAYISI = 3;
    private static final String FULL_PATH_USERS_TXT = "src/main/resources/users.txt";

    public static void main(String[] args) {
        try {

            System.out.println("Hoşgeldiniz.");

            welcomePageAndLoginSignUpSelection();

        } catch (Exception e) {
            System.out.println("main: Bir hata oluştu.");
        }
    }

    public static void welcomePageAndLoginSignUpSelection() {
        try {
            System.out.println("Sisteme login olmak için 1, kaydolmak için 2 yazınız.");
            Scanner scanner = new Scanner(System.in);
            int secim = scanner.nextInt();
            switch (secim) {
                case 1: // login
                    login();
                    break;
                case 2: // sign up
                    signup();
                    break;
                default:
                    System.out.println("Lütfen doğru seçim yapınız!");
                    welcomePageAndLoginSignUpSelection();
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("welcomePageAndLoginSignUpSelection: Seçim esnasında lütfen rakam giriniz.");
            welcomePageAndLoginSignUpSelection();
        } catch (Exception e) {
            System.out.println("welcomePageAndLoginSignUpSelection: Hatasız kod olmaz: " + e);
            welcomePageAndLoginSignUpSelection();
        }
    }

    public static void login() {
        try {
            dosyayiOkuVeListeyiDoldur();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Lütfen kullanıcı adınızı giriniz:");
            String username = scanner.nextLine();
            System.out.println("Lütfen şifrenizi giriniz:");
            String password = scanner.nextLine();

            for (Kullanici kullanici : kullaniciList) {
                boolean userBulunduMu = userCheck(kullanici, username, password);
                if (userBulunduMu) {
                    return;
                }
            }

            System.out.println("Böyle bir kayıtlı kullanıcı bulunamadı lütfen kayıt yaptırınız");

        } catch (Exception e) {
            System.out.println("login: login olurken bir hata oluştu: " + e);
        }
    }

    public static boolean userCheck(Kullanici kullanici, String username, String password) {
        if (kullanici.getKullaniciAdi().equals(username)) {
            if (kullanici.getSifre().equals(password)) {
                System.out.println("Hoşgeldiniz Sayın " + kullanici.getAdSoyad());
                return true;
            } else {
                System.out.println("Şifrenizi yanlış girdiniz. Lütfen şifrenizi tekrar giriniz:");
                Scanner scanner = new Scanner(System.in);
                String passwordNew = scanner.nextLine();
                boolean userSifresiOkMi = userCheck(kullanici, username, passwordNew);
                if (userSifresiOkMi) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static void dosyayiOkuVeListeyiDoldur() throws Exception {
        try {

            // dosyayı açıp okuyoruz
            BufferedReader reader = new BufferedReader(new FileReader(FULL_PATH_USERS_TXT));

            // dosyadaki her bir satırı tek tek dönüyoruz
            for (String line; (line = reader.readLine()) != null; ) { // line -> Mehmet Akşahin;maksahin;1234

                // burada satırı ; işaretine göre bölüyoruz
                String[] satirBilgiArray = line.split(";"); // satirBilgiArray[0] -> Mehmet Akşahin // satirBilgiArray[1] -> maksahin // satirBilgiArray[2] -> 1234

                // ve her satırdaki bilgileri modele ekledik, ayrıca her satırda toplam 3 adet veri bulunmakta, ilki adı, ikinci username, üçüncü şifre
                if (satirBilgiArray != null && satirBilgiArray.length >= DOSYA_SATIRDAKI_VERI_SAYISI) {
                    Kullanici kullanici = new Kullanici();
                    kullanici.setAdSoyad(satirBilgiArray[0]);
                    kullanici.setKullaniciAdi(satirBilgiArray[1]);
                    kullanici.setSifre(satirBilgiArray[2]);
                    // modeli de ana listeye ekledik
                    kullaniciList.add(kullanici);
                }
            }

        } catch (Exception e) {
            System.out.println("dosyayiOkuVeListeyiDoldur: Dosya okurken bir problem olustu: " + e);
            throw e;
        }
    }

    public static void signup() {
        try {
            dosyayiOkuVeListeyiDoldur();

            Scanner scanner = new Scanner(System.in);
            // ad soyad
            System.out.println("Ad Soyad bilginizi giriniz: ");
            String name = scanner.nextLine();
            // username
            String username = scanUsernameAndCheckIfAlreadyExists();
            // password
            System.out.println("Sifre bilginizi giriniz: ");
            String password = scanner.nextLine();

            saveNewUserInfos(name, username, password);

        } catch (Exception e) {
            System.out.println("Bir hata olustu: " + e);
        }
    }

    public static void saveNewUserInfos(String name, String username, String password) {
        try {
            File file = new File(FULL_PATH_USERS_TXT);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);

            br.write("\n" + name + ";" + username + ";" + password);

            br.close();
            fr.close();

            System.out.println("Kaydınız başarıyla tamamlanmıştır...");
        } catch (IOException e) {
            System.out.println("Dosya yazma işleminde bir hata oluştu :" + e);
        }
    }

    public static String scanUsernameAndCheckIfAlreadyExists() {
        String username = null;
        boolean isUsernameExists = false;
        try {
            System.out.println("Username : ");
            Scanner scanner = new Scanner(System.in);
            username = scanner.nextLine();

            isUsernameExists = checkUsernameIfAlreadyExists(username);

            if (isUsernameExists) {
                System.out.println("Bu kullanici adi baskasi tarafindan kullaniliyor\nLutfen baska bir tane seciniz");
                username = scanUsernameAndCheckIfAlreadyExists();
            }

        } catch (Exception e) {
            System.out.println("Hata: " + e);
        }
        return username;
    }

    public static boolean checkUsernameIfAlreadyExists(String username) {
        try {

            for (Kullanici kullanici : kullaniciList) {
                if (kullanici.getKullaniciAdi().equals(username)) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("Hata: " + e);
        }
        return false;
    }

}
