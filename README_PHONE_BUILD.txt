MY MONEY DESK - BUILD ANDROID APK USING ONLY YOUR PHONE

This is SOURCE CODE, not an APK. It builds with GitHub Actions (free-tier usage limits may apply).

1. On your phone, open https://github.com and sign in/create a free account.
2. Create a new PRIVATE repository named my-money-desk-android. Do not add README/gitignore/license.
3. Unzip My_Money_Desk_Android_Phone_Build.zip using your phone file manager.
4. On GitHub, use Add file > Upload files. Upload the contents of the unzipped folder, preserving their directory paths. IMPORTANT: GitHub's mobile web upload may flatten or not support folders. If so, use Desktop site mode in your browser, or use GitHub Codespaces / a computer to upload the folder structure. Do not upload the ZIP alone.
5. Commit files to the main branch. Go to Actions > Build My Money Desk APK > Run workflow (or let the push trigger it).
6. Wait for the workflow result. Open the completed run and download the artifact named MyMoneyDesk-Android-Debug-APK.
7. Extract the downloaded artifact ZIP, tap app-debug.apk, and approve installation of apps from your browser/file manager when prompted.

SAFETY: Do not publish financial data, bank statements, or backups in GitHub. The repository should contain source code only. Keep the repo private.
This debug build is for personal testing. Data is stored locally inside the app, not synced across devices. Uninstalling/clearing app storage can erase transactions. Export JSON backups regularly.
Android supports importing CSV/JSON via native file picker and exporting backups via the Android Save Document picker; device testing still needed.
No compiled APK was produced here because Android SDK/Gradle are not installed in this environment. Cloud build has not yet been run or verified.
