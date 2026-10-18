<p align="center">
  <img src="docs/assets/readme-hero.svg" alt="Shishu-Sneh — Every feed, every dose, on time. Profile, track, remind, share." width="1200" />
</p>

<p align="center">
  <strong>Smart baby care and growth tracking for Android.</strong><br />
  Growth charts, vaccination reminders, feeding alerts and milestone tracking in one app, in English, Hindi and Kannada.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Android-7F52FF?style=flat-square" alt="Kotlin for Android" />
  <img src="https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-3DDC84?style=flat-square" alt="Minimum SDK 26" />
  <img src="https://img.shields.io/badge/Storage-Room-4285F4?style=flat-square" alt="Room database" />
  <img src="https://img.shields.io/badge/Reminders-WorkManager-0F9D58?style=flat-square" alt="WorkManager" />
  <img src="https://img.shields.io/badge/Auth-Firebase-FFCA28?style=flat-square" alt="Firebase Authentication" />
</p>

<p align="center">
  <a href="#the-product">Product</a> ·
  <a href="#engineering-decisions">Engineering</a> ·
  <a href="#architecture">Architecture</a> ·
  <a href="#screens">Screens</a> ·
  <a href="#run-locally">Run locally</a> ·
  <a href="#creator">Creator</a>
</p>

---

## The product

New parents juggle a lot: vaccination due dates, feeding intervals, weight and height checks, first milestones. Paper cards get lost, and a missed vaccine date is easy to overlook.

**Shishu-Sneh (शिशु स्नेह, "love for the child") keeps a baby's care history and schedule in one place and reminds you before something is due.** Data lives in a local database on the phone, reminders run in the background even after a reboot, and a report can be exported as a PDF to share with a doctor.

| Capability | What you can do |
| :--- | :--- |
| **Baby profiles** | Create and manage a profile for each child. |
| **Growth tracking** | Log weight and height over time and see the trend on a chart (MPAndroidChart). |
| **Vaccination tracker** | Track scheduled vaccines and mark them done; get a notification for any dose due in the next 7 days. |
| **Feeding reminders** | Set a feeding interval in hours and receive periodic reminders. |
| **Milestones & activity log** | Record developmental milestones and daily activities: feeding, sleep, diaper changes and growth checks. |
| **PDF report** | Generate an A4 report of the profile and recent activity and share it through any app. |
| **Voice feedback** | Spoken confirmations on the home, feeding and vaccination screens through Android Text-to-Speech. |
| **Languages** | English, Hindi (`values-hi`) and Kannada (`values-kn`) string resources. |
| **Accounts** | Email/password sign-up and login with Firebase Authentication. |

### A workflow you can follow

**Sign up → add your baby → log growth and activities → get reminders → export a report**

## Engineering decisions

| Engineering problem | Implementation | Inspect the code |
| :--- | :--- | :--- |
| Reminders must fire even when the app is closed | Periodic `WorkManager` jobs check due vaccinations daily and post notifications. | [`VaccinationReminderWorker`](app/src/main/java/com/example/shishusneh/workers/VaccinationReminderWorker.kt), [`FeedingReminderWorker`](app/src/main/java/com/example/shishusneh/workers/FeedingReminderWorker.kt) |
| A phone restart should not cancel reminders | A boot receiver re-enqueues the daily vaccination check after `BOOT_COMPLETED`. | [`BootReceiver`](app/src/main/java/com/example/shishusneh/workers/BootReceiver.kt) |
| Care data should work offline | Room entities and DAOs for profiles, growth, vaccinations, milestones and activities, exposed as Flows. | [`AppDatabase`](app/src/main/java/com/example/shishusneh/data/AppDatabase.kt), [DAOs](app/src/main/java/com/example/shishusneh/data/dao) |
| UI state should survive configuration changes | MVVM with a shared `MainViewModel`, LiveData and coroutines. | [`MainViewModel`](app/src/main/java/com/example/shishusneh/viewmodel/MainViewModel.kt) |
| Doctors want something they can read | Android's `PdfDocument` draws an A4 report that is shared through a `FileProvider` intent. | [`ReportManager`](app/src/main/java/com/example/shishusneh/ui/utils/ReportManager.kt) |
| Not every parent reads English | All user-facing strings have Hindi and Kannada translations. | [`res/values-hi`](app/src/main/res/values-hi), [`res/values-kn`](app/src/main/res/values-kn) |

## Architecture

```mermaid
flowchart TD
    UI[Activities + Fragments<br/>Home, Growth, Vaccination, Feeding, Milestones] --> VM[MainViewModel<br/>LiveData + coroutines]
    VM --> DB[(Room database<br/>6 entities)]
    VM --> WM[WorkManager]
    WM --> VR[Vaccination reminder<br/>daily]
    WM --> FR[Feeding reminder<br/>every N hours]
    BOOT[BootReceiver] --> WM
    VR & FR --> N[Notifications]
    UI --> PDF[ReportManager<br/>PDF + share]
    UI --> AUTH[Firebase Auth]
```

| Layer | Technology |
| :--- | :--- |
| Language & UI | Kotlin, XML layouts with View Binding, Material Components |
| Architecture | MVVM: ViewModel, LiveData, Kotlin coroutines |
| Storage | Room (with KSP) |
| Background work | WorkManager, boot receiver |
| Charts & images | MPAndroidChart, Coil |
| Auth | Firebase Authentication |
| Build | Gradle Kotlin DSL, compile/target SDK 34, Java 17 |

## Screens

<p align="center">
  <img src="https://github.com/user-attachments/assets/8218f0bd-9952-48bc-969c-c7e40f9ec4cb" alt="Shishu-Sneh screen 1" width="24%" />
  <img src="https://github.com/user-attachments/assets/8242628c-9e90-4b4f-b1a9-48ae8c4566a0" alt="Shishu-Sneh screen 2" width="24%" />
  <img src="https://github.com/user-attachments/assets/117ba00a-4136-4a1a-8f6a-e16a3b0e56e5" alt="Shishu-Sneh screen 3" width="24%" />
  <img src="https://github.com/user-attachments/assets/357f6383-f102-4089-a3b6-ffa68bfa9aa1" alt="Shishu-Sneh screen 4" width="24%" />
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/1731675c-ebdf-4fc5-9382-d42ac949f9a1" alt="Shishu-Sneh screen 5" width="24%" />
  <img src="https://github.com/user-attachments/assets/3e0b6f85-103c-4ca2-ac84-137a738e6805" alt="Shishu-Sneh screen 6" width="24%" />
  <img src="https://github.com/user-attachments/assets/d8811f96-0512-423b-b4ef-60ca46a817ea" alt="Shishu-Sneh screen 7" width="24%" />
  <img src="https://github.com/user-attachments/assets/6aeb6475-8b1d-4a85-b78a-2e70c9360593" alt="Shishu-Sneh screen 8" width="24%" />
</p>

<details>
<summary><strong>Scope and known limitations</strong></summary>

- Care records are stored only on the device; there is no cloud backup or sync between phones yet.
- Feeding and vaccination guidance in the app is general information, not medical advice.
- There are no automated tests beyond the default Android test templates.
- No published APK or Play Store listing yet; build it from source.

</details>

## Run locally

Requirements: **Android Studio** (recent stable), **JDK 17**, and an Android device or emulator running **Android 8.0 (API 26)** or newer.

```bash
git clone https://github.com/Lingu17/Shishu-Sneh.git
```

1. Open the folder in Android Studio and let Gradle sync.
2. Firebase: the app uses Firebase Authentication. To use your own Firebase project, replace `app/google-services.json` with the file from your project and enable **Email/Password** sign-in.
3. Run the `app` configuration on a device or emulator.
4. On Android 13+, allow notifications when prompted so reminders can appear.

The Gradle wrapper scripts (`gradlew`, `gradlew.bat`) are not committed, so build from Android Studio, or run `gradle wrapper` once with a local Gradle install before using `./gradlew assembleDebug`.

## Project map

```text
Shishu-Sneh/
└── app/src/main/
    ├── java/com/example/shishusneh/
    │   ├── data/            Room database, entities and DAOs
    │   ├── ui/              Activities, fragments, adapters, bottom sheets
    │   │   └── utils/       PDF report and voice feedback managers
    │   ├── viewmodel/       MainViewModel
    │   └── workers/         Vaccination and feeding reminders, boot receiver
    ├── res/                 Layouts, drawables, strings (en, hi, kn)
    └── AndroidManifest.xml  Activities, permissions, boot receiver
```

## Creator

**Lingraj Malipatil** · [GitHub @Lingu17](https://github.com/Lingu17) · [LinkedIn](https://www.linkedin.com/in/lingraj-malipatil-a2735a241/)

---

<p align="center"><strong>Shishu-Sneh</strong><br />Every feed, every dose, on time.</p>
