package com.timur.account;

import com.timur.account.model.Kullanici;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AnaSinif {

    public static List<Kullanici> kullaniciList = new ArrayList<>();

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
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\ozant\\Desktop\\Java\\Account\\src\\main\\resources\\users.txt"));

            // dosyadaki her bir satırı tek tek dönüyoruz
            for (String line; (line = reader.readLine()) != null; ) { // line -> Mehmet Akşahin;maksahin;1234

                // burada satırı ; işaretine göre bölüyoruz
                String[] satirBilgiArray = line.split(";"); // satirBilgiArray[0] -> Mehmet Akşahin // satirBilgiArray[1] -> maksahin // satirBilgiArray[2] -> 1234

                // ve her satırdaki bilgileri modele ekledik
                if (satirBilgiArray != null && satirBilgiArray.length == 3) {
                    Kullanici kullanici = new Kullanici();
                    kullanici.setAdSoyad(satirBilgiArray[0]);
                    kullanici.setKullaniciAdi(satirBilgiArray[1]);
                    kullanici.setSifre(satirBilgiArray[2]);
                    // modeli de ana listeye ekledik
                    kullaniciList.add(kullanici);
                }
            }

            /*
            // liste doldu şimdi check etmek için bir yazdıralım
            for (Kullanici kullanici : kullaniciList) {
                System.out.println("Kullanıcı: " + kullanici.getAdSoyad() + kullanici.getKullaniciAdi() + kullanici.getSifre());
            }
             */

        } catch (Exception e) {
            System.out.println("dosyayiOkuVeListeyiDoldur: Dosya okurken bir problem olustu: " + e);
            throw e;
        }
    }

    public static void signup() {
        try {
            dosyayiOkuVeListeyiDoldur();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Isim-Soyad: ");
            String name = scanner.nextLine();
            String username = thisUsernameAlreadyExists();
            System.out.println("Sifre: ");
            String password = scanner.nextLine();

            bilgiYazdirma(name, username, password);


        } catch (Exception e) {
            System.out.println("Bir hata olustu: " + e);
        }
    }

    public static void bilgiYazdirma(String name, String username, String password) {
        try {
            File file = new File("C:\\Users\\ozant\\Desktop\\Java\\Account\\src\\main\\resources\\users.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write("\n" + name + ";" + username + ";" + password);

            br.close();
            fr.close();

        } catch (IOException e) {
            System.out.println("Hata :" + e);
        }
    }

    public static String thisUsernameAlreadyExists() {
        String username = null;
        try {
            System.out.println("Kullanici adi: ");
            Scanner scanner = new Scanner(System.in);
            username = scanner.nextLine();
            for (Kullanici kullanici : kullaniciList) {
                if (kullanici.getKullaniciAdi().equals(username)) {
                    System.out.println("Bu kullanici adi baskasi tarafindan kullaniliyor\\nLutfen baska bir tane seciniz");
                    System.out.println("Kullanici adi: ");
                    scanner = new Scanner(System.in);
                    String newUsername = scanner.nextLine();
                    thisUsernameAlreadyExists();
                }
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e);
        } finally {
            return username;
        }
    }

}