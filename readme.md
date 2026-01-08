# 🍅 Domates Hastalık Teşhis Uygulaması

Domates yapraklarındaki hastalıkları yapay zeka ile tespit eden Android uygulaması.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![TensorFlow Lite](https://img.shields.io/badge/TensorFlow_Lite-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)

## 📱 Uygulama Özellikleri

- 🔍 **Yapay Zeka ile Hastalık Teşhisi**: TensorFlow Lite modeli kullanarak domates yapraklarındaki hastalıkları tespit eder
- 📷 **Kamera / Galeri Desteği**: Fotoğrafı kamerayla çekin veya galeriden seçin
- 📚 **Hastalık Kütüphanesi**: Tüm domates hastalıkları hakkında detaylı bilgi
- 📊 **İstatistik Ekranı**: Teşhis istatistiklerinizi görüntüleyin
- 📜 **Teşhis Geçmişi**: Geçmiş teşhisleri saklayın ve inceleyin
- ⚙️ **Ayarlar**: Uygulama tercihlerini özelleştirin
- 🌙 **Karanlık Mod Desteği**

## 🦠 Tespit Edilebilen Hastalıklar

| # | Hastalık Adı |
|---|-------------|
| 1 | Bacterial Spot (Bakteri Lekesi) |
| 2 | Early Blight (Erken Yanıklık) |
| 3 | Late Blight (Geç Yanıklık) |
| 4 | Leaf Mold (Yaprak Küfü) |
| 5 | Septoria Leaf Spot (Septoria Yaprak Lekesi) |
| 6 | Spider Mites (Kırmızı Örümcek) |
| 7 | Target Spot (Hedef Lekesi) |
| 8 | Yellow Leaf Curl Virus (Sarı Yaprak Kıvırcıklık Virüsü) |
| 9 | Mosaic Virus (Mozaik Virüsü) |
| 10 | Healthy (Sağlıklı) |

---

## 🚀 Kurulum Rehberi (Adım Adım)

### 📋 Gereksinimler

Projeyi çalıştırmak için aşağıdaki yazılımların bilgisayarınızda kurulu olması gerekmektedir:

| Yazılım | Minimum Versiyon | İndirme Linki |
|---------|------------------|---------------|
| **Android Studio** | Hedgehog (2023.1.1) veya üzeri | [İndir](https://developer.android.com/studio) |
| **Java JDK** | 11 veya üzeri | [İndir](https://adoptium.net/) |
| **Git** | Herhangi bir versiyon | [İndir](https://git-scm.com/downloads) |

> [!NOTE]
> Android Studio kurulumu sırasında JDK otomatik olarak yüklenir, ayrıca kurmanıza gerek olmayabilir.

---

### 📥 Adım 1: Projeyi İndirin

#### Yöntem A: Git ile Klonlama (Önerilen)

1. Bilgisayarınızda bir terminal (Windows: PowerShell veya Git Bash) açın
2. Projeyi indirmek istediğiniz klasöre gidin
3. Aşağıdaki komutu çalıştırın:

```bash
git clone https://github.com/KULLANICI_ADI/DomatesTeshiYeni.git
```

> [!TIP]
> `KULLANICI_ADI` yerine GitHub kullanıcı adınızı yazın

#### Yöntem B: ZIP Olarak İndirme

1. GitHub sayfasında yeşil **"Code"** butonuna tıklayın
2. **"Download ZIP"** seçeneğini seçin
3. İndirilen ZIP dosyasını istediğiniz bir klasöre çıkarın

---

### 💻 Adım 2: Android Studio'yu Açın

1. Android Studio'yu başlatın
2. **"Open"** butonuna tıklayın (veya `File > Open`)

   ![Open Project](https://developer.android.com/static/images/studio/welcome-screen-open.png)

3. İndirdiğiniz proje klasörünü seçin (`DomatesTeshiYeni`)
4. **"OK"** butonuna tıklayın

---

### ⏳ Adım 3: Gradle Senkronizasyonunu Bekleyin

Proje açıldıktan sonra Android Studio otomatik olarak gerekli bağımlılıkları indirecektir.

1. Sağ alt köşede **"Gradle Sync"** işleminin tamamlanmasını bekleyin
2. Bu işlem internet hızınıza bağlı olarak **5-15 dakika** sürebilir

> [!WARNING]
> Senkronizasyon tamamlanmadan projeyi çalıştırmaya çalışmayın!

**Senkronizasyon hatası alırsanız:**
- `File > Sync Project with Gradle Files` seçeneğine tıklayın
- Veya üst menüdeki 🐘 fil ikonuna tıklayın

---

### 📱 Adım 4: Emulator veya Fiziksel Cihaz Ayarlayın

#### Seçenek A: Android Emulator (Sanal Cihaz)

1. Üst menüden `Tools > Device Manager` seçeneğine tıklayın
2. **"Create Device"** butonuna tıklayın
3. Bir telefon modeli seçin (örn: Pixel 6)
4. **"Next"** butonuna tıklayın
5. Bir sistem imajı seçin:
   - **API 34 (Android 14)** önerilir
   - Yanında "Download" yazıyorsa tıklayarak indirin
6. **"Next"** → **"Finish"** butonlarına tıklayın

#### Seçenek B: Fiziksel Android Cihaz

1. Telefonunuzda **Geliştirici Seçenekleri**'ni aktifleştirin:
   - `Ayarlar > Telefon Hakkında > Yapı Numarası`'na **7 kez** dokunun
2. **USB Hata Ayıklama**'yı açın:
   - `Ayarlar > Geliştirici Seçenekleri > USB Hata Ayıklama`
3. Telefonu USB kablosuyla bilgisayara bağlayın
4. Telefonda çıkan **"USB hata ayıklamaya izin ver"** mesajını onaylayın

> [!IMPORTANT]
> Fiziksel cihaz kullanırken minimum Android sürümü: **Android 7.0 (API 24)**

---

### ▶️ Adım 5: Uygulamayı Çalıştırın

1. Üst menüdeki cihaz seçici dropdown'dan emulator veya fiziksel cihazınızı seçin
2. Yeşil **▶ Run** butonuna tıklayın (veya `Shift + F10`)
3. İlk çalıştırma biraz uzun sürebilir, sabırlı olun

**Başarılı!** 🎉 Uygulama cihazınızda açılacaktır.

---

## 📁 Proje Yapısı

```
DomatesTeshiYeni/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/domatesteshiyeni/    # Java kaynak kodları
│   │   │   ├── MainActivity.java                 # Teşhis ekranı
│   │   │   ├── HomeActivity.java                 # Ana menü
│   │   │   ├── SplashActivity.java               # Açılış ekranı
│   │   │   ├── HistoryActivity.java              # Teşhis geçmişi
│   │   │   ├── DiseaseLibraryActivity.java       # Hastalık kütüphanesi
│   │   │   └── ...
│   │   ├── res/                                  # UI dosyaları (layout, drawable, vb.)
│   │   ├── assets/                               # ML model ve etiketler
│   │   │   ├── best_float32.tflite               # TensorFlow Lite modeli
│   │   │   └── labels.txt                        # Hastalık etiketleri
│   │   └── AndroidManifest.xml                   # Uygulama manifest dosyası
│   └── build.gradle.kts                          # Modül bağımlılıkları
├── gradle/                                       # Gradle wrapper dosyaları
├── build.gradle.kts                              # Proje build dosyası
├── settings.gradle.kts                           # Proje ayarları
└── README.md                                     # Bu dosya
```

---

## 🛠️ Kullanılan Teknolojiler

| Teknoloji | Kullanım Amacı |
|-----------|----------------|
| **Java** | Uygulama geliştirme dili |
| **Android SDK 34** | Hedef Android sürümü |
| **TensorFlow Lite** | Makine öğrenmesi modeli çalıştırma |
| **Material Design** | Modern UI bileşenleri |
| **AndroidX** | Destek kütüphaneleri |

---

## ❓ Sık Karşılaşılan Sorunlar ve Çözümleri

### 🔴 "SDK location not found" Hatası

**Çözüm:** `local.properties` dosyası oluşturulacaktır. Android Studio projeyi açtığında otomatik olarak oluşturur. Manuel oluşturmak için:

1. Proje kök dizininde `local.properties` dosyası oluşturun
2. İçine şunu yazın (kendi SDK yolunuzu yazın):
```
sdk.dir=C\:\\Users\\KULLANICI_ADINIZ\\AppData\\Local\\Android\\Sdk
```

### 🔴 "Gradle sync failed" Hatası

**Çözümler:**
1. İnternet bağlantınızı kontrol edin
2. `File > Invalidate Caches and Restart` seçeneğini deneyin
3. Proxy kullanıyorsanız `gradle.properties` dosyasına proxy ayarlarını ekleyin

### 🔴 Emulator açılmıyor / çok yavaş

**Çözümler:**
1. BIOS'tan **Intel VT-x** veya **AMD-V** sanallaştırmayı aktifleştirin
2. Windows'ta **Hyper-V**'yi devre dışı bırakın
3. Daha düşük çözünürlüklü bir emulator profili seçin

### 🔴 "Model dosyası bulunamadı" Hatası

**Çözüm:** `app/src/main/assets/` klasöründe aşağıdaki dosyaların varlığını kontrol edin:
- `best_float32.tflite`
- `labels.txt`

---

## 📄 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

---

## 💬 İletişim & Destek

Sorularınız için GitHub Issues bölümünü kullanabilirsiniz.

---

<p align="center">
  Made with ❤️ for 🍅
</p>
